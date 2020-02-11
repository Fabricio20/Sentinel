package net.notfab.sentinel.example.worker;

import net.notfab.sentinel.example.worker.commands.EmbedCommand;
import net.notfab.sentinel.example.worker.commands.HelloCommand;
import net.notfab.sentinel.sdk.MessageBroker;
import net.notfab.sentinel.sdk.actions.ActionRegistry;
import net.notfab.sentinel.sdk.discord.command.CommandFramework;

public class Worker {

    public Worker() {
        MessageBroker broker = new MessageBroker();
        ActionRegistry registry = new ActionRegistry(broker);
        CommandFramework commandFramework = new CommandFramework(broker);
        commandFramework.register(new HelloCommand());
        commandFramework.register(new EmbedCommand());
    }

}