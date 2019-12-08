package net.notfab.sentinel.worker;

import lombok.Getter;
import net.notfab.sentinel.sdk.MessageBroker;

public class SentinelWorker {

    private final MessageBroker broker;

    @Getter
    private final CommandManager commandManager;

    public SentinelWorker() {
        this.broker = new MessageBroker();
        this.commandManager = new CommandManager(this.broker);
    }

    public void shutdown() {
        this.broker.shutdown();
    }

}