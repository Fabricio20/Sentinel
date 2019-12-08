package net.notfab.sentinel.worker;

import lombok.Getter;
import net.notfab.sentinel.sdk.Channels;
import net.notfab.sentinel.sdk.MessageBroker;
import net.notfab.sentinel.sdk.core.ExchangeType;
import net.notfab.sentinel.worker.command.CommandManager;

public class SentinelWorker {

    private final MessageBroker broker;

    @Getter
    private final CommandManager commandManager;

    public SentinelWorker(MessageBroker broker) {
        this.broker = broker;
        this.broker.registerChannels(ExchangeType.Fanout, Channels.MESSENGER);
        this.commandManager = new CommandManager(this.broker);
    }

    public void shutdown() {
        this.broker.shutdown();
    }

}