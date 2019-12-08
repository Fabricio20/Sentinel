package net.notfab.sentinel.sdk.rpc;

import net.notfab.sentinel.sdk.core.SentinelListener;
import net.notfab.sentinel.sdk.rpc.RPCManager;
import net.notfab.sentinel.sdk.rpc.RPCResponse;

public class RPCResponseListener implements SentinelListener<RPCResponse> {

    private final RPCManager rpcManager;

    RPCResponseListener(RPCManager rpcManager) {
        this.rpcManager = rpcManager;
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