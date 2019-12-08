package net.notfab.sentinel.worker.command;

import net.notfab.sentinel.sdk.core.rabbit.RabbitSentinelListener;
import net.notfab.sentinel.sdk.core.redis.RedisSentinelListener;
import net.notfab.sentinel.sdk.entities.events.CommandEvent;
import net.notfab.sentinel.worker.SentinelCommand;

public class CommandListenerRabbit extends RabbitSentinelListener<CommandEvent> {

    private final SentinelCommand command;

    public CommandListenerRabbit(SentinelCommand command) {
        super(CommandEvent.class);
        this.command = command;
    }

    @Override
    public boolean isAutoAck() {
        return false;
    }

    @Override
    public boolean onMessage(String channel, CommandEvent message) {
        command.onCommand(message.getMember(), message.getChannel(), message.getArgs());
        return true;
    }

}