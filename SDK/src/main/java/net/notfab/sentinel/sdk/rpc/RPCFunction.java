package net.notfab.sentinel.sdk.rpc;

public interface RPCFunction {

    String getMethod();

    Object onRequest(RPCRequest request);

}