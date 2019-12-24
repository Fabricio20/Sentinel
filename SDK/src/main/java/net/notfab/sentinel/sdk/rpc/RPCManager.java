package net.notfab.sentinel.sdk.rpc;

import lombok.Getter;
import net.notfab.eventti.EventHandler;
import net.notfab.eventti.Listener;
import net.notfab.sentinel.sdk.DiscordChannels;
import net.notfab.sentinel.sdk.ExchangeType;
import net.notfab.sentinel.sdk.MessageBroker;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The RPC Manager for Sentinel. Treat it as a Singleton.
 */
public class RPCManager implements Listener {

    private final Map<String, RPCAction> futures = new HashMap<>();
    private final Map<String, RPCFunction> functionMap = new HashMap<>();
    private final MessageBroker broker;

    @Getter
    private static RPCManager Instance;

    public RPCManager(MessageBroker broker) {
        Instance = this;
        this.broker = broker;
        this.broker.addListener(this, ExchangeType.DIRECT, DiscordChannels.RPC_REQUESTS);
        this.broker.addListener(this, ExchangeType.DIRECT, DiscordChannels.RPC_RESPONSES);
    }

    /**
     * Requests a remote procedure from the network.
     *
     * @param request - RPC Request.
     * @return RPCAction (Async/Sync).
     */
    public RPCAction ask(RPCRequest request) {
        request.setTag(UUID.randomUUID().toString());
        RPCAction future = new RPCAction();
        this.futures.put(request.getTag(), future);
        this.broker.publish(request, ExchangeType.DIRECT, DiscordChannels.RPC_REQUESTS);
        return future;
    }

    /**
     * Executes a non-returning remote procedure asynchronously.
     *
     * @param request - RPC Request.
     */
    public void send(RPCRequest request) {
        request.setTag(UUID.randomUUID().toString());
        this.broker.publish(request, ExchangeType.DIRECT, DiscordChannels.RPC_REQUESTS);
    }

    /**
     * Adds an RPC function to the network.
     *
     * @param function - The function implementation.
     */
    public void addFunction(RPCFunction function) {
        this.functionMap.put(function.getMethod(), function);
    }

    @EventHandler
    public void onRequest(RPCRequest request) {
        if (this.functionMap.containsKey(request.getMethod())) {
            RPCFunction function = this.functionMap.get(request.getMethod());
            Object payload = function.onRequest(request);
            // Non-returning RPC
            if (payload == null) {
                return;
            }
            RPCResponse response = new RPCResponse();
            response.setTag(request.getTag());
            response.setResponse(payload);
            this.broker.publish(response, ExchangeType.DIRECT, DiscordChannels.RPC_RESPONSES);
        }
    }

    @EventHandler
    public void onResponse(RPCResponse response) {
        if (this.futures.containsKey(response.getTag())) {
            RPCAction action = this.futures.get(response.getTag());
            action.signal(response);
            this.futures.remove(response.getTag());
        }
    }

    /**
     * Shuts down this RPC Manager and all underlying systems.
     */
    public void shutdown() {
        this.futures.clear();
        this.functionMap.clear();
    }

}