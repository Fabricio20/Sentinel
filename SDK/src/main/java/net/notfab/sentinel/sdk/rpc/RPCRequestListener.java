package net.notfab.sentinel.sdk.rpc;

import net.notfab.sentinel.sdk.core.SentinelListener;
import net.notfab.sentinel.sdk.rpc.RPCManager;
import net.notfab.sentinel.sdk.rpc.RPCRequest;

public class RPCRequestListener implements SentinelListener<RPCRequest> {

    private final RPCManager rpcManager;

    RPCRequestListener(RPCManager rpcManager) {
        this.rpcManager = rpcManager;
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