package net.notfab.sentinel.sdk.rpc;

import net.notfab.sentinel.sdk.Channels;
import net.notfab.sentinel.sdk.MessageBroker;
import net.notfab.sentinel.sdk.core.ExchangeType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RPCManager {

    private final Map<String, RPCAction> futures = new HashMap<>();
    private final Map<String, RPCFunction> functionMap = new HashMap<>();
    private final MessageBroker broker;

    public RPCManager(MessageBroker broker) {
        this.broker = broker;
        this.broker.registerChannels(ExchangeType.Fanout, Channels.RPC_REQUESTS, Channels.RPC_RESPONSES);
        this.broker.addListener(new RPCRequestListener(this), Channels.RPC_REQUESTS);
        this.broker.addListener(new RPCResponseListener(this), Channels.RPC_RESPONSES);
    }

    /**
     * Requests a remote procedure from tne network.
     *
     * @param request - RPC Request.
     * @return RPCAction (Async/Sync).
     */
    public RPCAction ask(RPCRequest request) {
        request.setTag(UUID.randomUUID().toString());
        RPCAction future = new RPCAction();
        this.futures.put(request.getTag(), future);
        this.broker.publish(request, Channels.RPC_REQUESTS);
        return future;
    }

    /**
     * Executes a non-returning remote procedure asynchronously.
     *
     * @param request - RPC Request.
     */
    public void send(RPCRequest request) {
        request.setTag(UUID.randomUUID().toString());
        this.broker.publish(request, Channels.RPC_REQUESTS);
    }

    /**
     * Adds an RPC function to the network.
     *
     * @param function - The function implementation.
     */
    public void addFunction(RPCFunction function) {
        this.functionMap.put(function.getMethod(), function);
    }

    boolean onRequest(RPCRequest request) {
        if (this.functionMap.containsKey(request.getMethod())) {
            RPCFunction function = this.functionMap.get(request.getMethod());
            Object payload = function.onRequest(request);
            // Non-returning RPC
            if (payload == null) {
                return true;
            }
            // Possible error
            if (payload instanceof Boolean) {
                return (boolean) payload;
            }
            RPCResponse response = new RPCResponse();
            response.setTag(request.getTag());
            response.setResponse(payload);
            this.broker.publish(response, Channels.RPC_RESPONSES);
            return true;
        } else {
            return true;
        }
    }

    boolean onResponse(RPCResponse response) {
        if (this.futures.containsKey(response.getTag())) {
            RPCAction action = this.futures.get(response.getTag());
            action.signal(response);
            this.futures.remove(response.getTag());
            return true;
        } else {
            return true;
        }
    }

}