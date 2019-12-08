package net.notfab.sentinel.sdk.rpc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.notfab.sentinel.sdk.Channels;
import net.notfab.sentinel.sdk.MessageBroker;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RPCManager {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, RPCAction> futures = new HashMap<>();
    private final Map<String, RPCFunction> functionMap = new HashMap<>();
    private final MessageBroker broker;

    public RPCManager(MessageBroker broker) {
        this.broker = broker;
        this.broker.addListener(new RPCRequestListener(this), Channels.RPC);
        this.broker.addListener(new RPCResponseListener(this), Channels.RPC);
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
        this.broker.publish(request, Channels.RPC);
        return future;
    }

    /**
     * Executes a non-returning remote procedure asynchronously.
     *
     * @param request - RPC Request.
     */
    public void send(RPCRequest request) {
        request.setTag(UUID.randomUUID().toString());
        this.broker.publish(request, Channels.RPC);
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
            try {
                // Non-returning RPC
                if (payload == null) {
                    return true;
                }
                RPCResponse response = new RPCResponse();
                response.setTag(request.getTag());
                response.setResponse(this.objectMapper.writeValueAsString(payload));
                this.broker.publish(response, request.getMethod());
                return true;
            } catch (JsonProcessingException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    boolean onResponse(RPCResponse response) {
        if (this.futures.containsKey(response.getTag())) {
            RPCAction action = this.futures.get(response.getTag());
            action.signal(response);
            this.futures.remove(response.getTag());
            return true;
        } else {
            return false;
        }
    }

}