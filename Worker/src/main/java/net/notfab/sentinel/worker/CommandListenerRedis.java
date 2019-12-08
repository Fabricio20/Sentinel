package net.notfab.sentinel.worker;

import net.notfab.sentinel.sdk.core.redis.RedisSentinelListener;
import net.notfab.sentinel.sdk.entities.events.CommandEvent;

public class CommandListenerRedis extends RedisSentinelListener<CommandEvent> {

    private final SentinelCommand command;

    public CommandListenerRedis(SentinelCommand command) {
        super(CommandEvent.class);
        this.command = command;
    }

    @Override
    public void onMessage(String channel, CommandEvent message) {
        command.onCommand(message.getMember(), message.getChannel(), message.getArgs());
    }

}