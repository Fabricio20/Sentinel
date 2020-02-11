package net.notfab.sentinel.sdk.actions;

import lombok.Getter;
import net.notfab.eventti.EventHandler;
import net.notfab.eventti.Listener;
import net.notfab.sentinel.sdk.ExchangeType;
import net.notfab.sentinel.sdk.MessageBroker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/**
 * The RPC Manager for Sentinel. Treat it as a Singleton.
 */
public class ActionRegistry implements Listener {

    private final Map<String, Action> futures = new HashMap<>();
    private final Map<String, Function<ActionRequest, ?>> functionMap = new HashMap<>();
    private final MessageBroker broker;

    private static final String RPC_HUB = "Sentinel:Hub:RPC";

    @Getter
    private static ActionRegistry Instance;

    public ActionRegistry(MessageBroker broker) {
        Instance = this;
        this.broker = broker;
        this.broker.addListener(this, ExchangeType.DIRECT, RPC_HUB);
    }

    /**
     * Requests a remote procedure from the network.
     *
     * @param request - Action Request.
     */
    public void ask(ActionRequest request, Action future) {
        request.setTag(UUID.randomUUID().toString());
        this.futures.put(request.getTag(), future);
        this.broker.publish(request, ExchangeType.DIRECT, RPC_HUB);
    }

    /**
     * Executes a non-returning remote procedure asynchronously.
     *
     * @param request - RPC Request.
     */
    public void send(ActionRequest request) {
        request.setTag(UUID.randomUUID().toString());
        this.broker.publish(request, ExchangeType.DIRECT, RPC_HUB);
    }

    /**
     * Adds an action to the network.
     *
     * @param name     - Name of this action.
     * @param function - The function implementation.
     */
    public void register(String name, Function<ActionRequest, ?> function) {
        this.functionMap.put(name.toLowerCase(), function);
    }

    /**
     * Unregisters an action.
     *
     * @param name - Name of the action.
     */
    public void unregister(String name) {
        this.functionMap.remove(name.toLowerCase());
    }

    @EventHandler
    public void onRequest(ActionRequest request) {
        if (this.functionMap.containsKey(request.getMethod())) {
            Function<ActionRequest, ?> function = this.functionMap.get(request.getMethod());
            Object payload = function.apply(request);
            // Non-returning RPC
            if (payload == null) {
                return;
            }
            ActionResponse response = new ActionResponse();
            response.setTag(request.getTag());
            if (payload instanceof List) {
                response.setPayloadList((List<Object>) payload);
            } else {
                response.setPayload(payload);
            }
            this.broker.publish(response, ExchangeType.DIRECT, RPC_HUB);
        }
    }

    @EventHandler
    public void onResponse(ActionResponse response) {
        if (this.futures.containsKey(response.getTag())) {
            Action action = this.futures.get(response.getTag());
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