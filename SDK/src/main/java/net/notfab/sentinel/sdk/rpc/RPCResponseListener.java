package net.notfab.sentinel.sdk.rpc;

import net.notfab.sentinel.sdk.core.SentinelListener;

public class RPCResponseListener implements SentinelListener<RPCResponse> {

    private final RPCManager rpcManager;

    RPCResponseListener(RPCManager rpcManager) {
        this.rpcManager = rpcManager;
    }

    @Override
    public Class<RPCResponse> getClazz() {
        return RPCResponse.class;
    }

    @Override
    public boolean isAutoAck() {
        return false;
    }

    @Override
    public boolean onMessage(String channel, RPCResponse message) {
        return this.rpcManager.onResponse(message);
    }

}