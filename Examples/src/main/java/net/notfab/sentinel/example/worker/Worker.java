package net.notfab.sentinel.example.worker;

import net.notfab.sentinel.example.worker.commands.EmbedCommand;
import net.notfab.sentinel.example.worker.commands.HelloCommand;
import net.notfab.sentinel.sdk.MessageBroker;
import net.notfab.sentinel.sdk.discord.command.CommandFramework;
import net.notfab.sentinel.sdk.rpc.RPCManager;

public class Worker {

    public Worker() {
        MessageBroker broker = new MessageBroker();
        RPCManager manager = new RPCManager(broker);
        CommandFramework commandFramework = new CommandFramework(broker);
        commandFramework.add(new HelloCommand());
        commandFramework.add(new EmbedCommand());
    }

}