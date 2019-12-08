package net.notfab.sentinel.worker.command;

import net.notfab.sentinel.sdk.core.SentinelListener;
import net.notfab.sentinel.sdk.entities.events.CommandEvent;
import net.notfab.sentinel.worker.SentinelCommand;

public class CommandListener implements SentinelListener<CommandEvent> {

    private final SentinelCommand command;

    CommandListener(SentinelCommand command) {
        this.command = command;
    }

    @Override
    public Class<CommandEvent> getClazz() {
        return CommandEvent.class;
    }

    @Override
    public boolean isAutoAck() {
        return true;
    }

    @Override
    public boolean onMessage(String channel, CommandEvent message) {
        command.onCommand(message.getMember(), message.getChannel(), message.getArgs());
        return true;
    }

}