package net.notfab.sentinel.worker;

import lombok.Getter;
import net.notfab.sentinel.sdk.MessageBroker;
import net.notfab.sentinel.sdk.rpc.RPCManager;

public class SentinelWorker {

    private final MessageBroker broker;

    @Getter
    private final CommandFramework commandFramework;
    @Getter
    private final RPCManager rpcManager;
    @Getter
    private final RPC RPC;

    public SentinelWorker(MessageBroker broker) {
        this.broker = broker;
        this.commandFramework = new CommandFramework(this.broker);
        this.rpcManager = new RPCManager(this.broker);
        this.RPC = new RPC(this.rpcManager);
    }

    /**
     * Shuts down this SentinelWorker and all underlying systems.
     */
    public void shutdown() {
        this.commandFramework.shutdown();
        this.rpcManager.shutdown();
        this.broker.shutdown();
    }

}