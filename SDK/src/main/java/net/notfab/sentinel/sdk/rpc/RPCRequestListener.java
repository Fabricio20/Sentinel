package net.notfab.sentinel.sdk.rpc;

import net.notfab.sentinel.sdk.core.SentinelListener;

public class RPCRequestListener implements SentinelListener<RPCRequest> {

    private final RPCManager rpcManager;

    RPCRequestListener(RPCManager rpcManager) {
        this.rpcManager = rpcManager;
    }

    @Override
    public Class<RPCRequest> getClazz() {
        return RPCRequest.class;
    }

    @Override
    public boolean isAutoAck() {
        return false;
    }

    @Override
    public boolean onMessage(String channel, RPCRequest message) {
        return this.rpcManager.onRequest(message);
    }

}