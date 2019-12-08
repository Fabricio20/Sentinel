package net.notfab.sentinel.worker;

import net.notfab.sentinel.sdk.CommandListener;
import net.notfab.sentinel.sdk.MessageBroker;
import net.notfab.sentinel.sdk.entities.SentinelCommand;

public class CommandManager {

    private MessageBroker broker;

    public CommandManager(MessageBroker broker) {
        this.broker = broker;
    }

    public void add(SentinelCommand command) {
        CommandListener listener = new CommandListener(command);
        String[] channelNames = command.getNames().stream()
                .map(x -> "Sentinel:Commands:" + x)
                .toArray(String[]::new);
        this.broker.addListener(listener, channelNames);
    }

}