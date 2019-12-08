package net.notfab.sentinel.worker;

import net.notfab.sentinel.sdk.Channels;
import net.notfab.sentinel.sdk.MessageBroker;

public class CommandManager {

    private MessageBroker broker;

    public CommandManager(MessageBroker broker) {
        this.broker = broker;
    }

    public void add(SentinelCommand command) {
        CommandListenerRedis listener = new CommandListenerRedis(command);
        String[] channelNames = command.getNames().stream()
                .map(x -> Channels.COMMAND_PREFIX + x.toUpperCase())
                .toArray(String[]::new);
        this.broker.addListener(listener, channelNames);
    }

}