package net.notfab.sentinel.worker.command;

import net.notfab.sentinel.sdk.Channels;
import net.notfab.sentinel.sdk.MessageBroker;
import net.notfab.sentinel.sdk.core.ExchangeType;
import net.notfab.sentinel.sdk.core.SentinelListener;
import net.notfab.sentinel.worker.SentinelCommand;

public class CommandManager {

    private MessageBroker broker;

    public CommandManager(MessageBroker broker) {
        this.broker = broker;
    }

    public void add(SentinelCommand command) {
        SentinelListener listener = new CommandListener(command);
        String[] channelNames = command.getNames().stream()
                .map(x -> Channels.COMMAND_PREFIX + x.toUpperCase())
                .toArray(String[]::new);
        this.broker.registerChannels(ExchangeType.Direct, channelNames);
        this.broker.addListener(listener, channelNames);
    }

}