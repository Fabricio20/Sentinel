package net.notfab.sentinel.sdk;

import net.notfab.sentinel.sdk.entities.SentinelCommand;
import net.notfab.sentinel.sdk.entities.SentinelListener;
import net.notfab.sentinel.sdk.entities.events.CommandEvent;

public class CommandListener extends SentinelListener<CommandEvent> {

    private final SentinelCommand command;

    public CommandListener(SentinelCommand command) {
        super(CommandEvent.class);
        this.command = command;
    }

    @Override
    public void onMessage(String channel, CommandEvent message) {
        command.onCommand(message.getMember(), message.getChannel(), message.getArgs());
    }

}