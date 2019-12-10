package net.notfab.sentinel.example.worker;

import net.notfab.sentinel.example.worker.commands.EmbedCommand;
import net.notfab.sentinel.example.worker.commands.HelloCommand;
import net.notfab.sentinel.sdk.MessageBroker;
import net.notfab.sentinel.worker.SentinelWorker;

public class Start {

    public static void main(String[] args) {
        SentinelWorker worker = new SentinelWorker(new MessageBroker());
        worker.getCommandFramework().add(new HelloCommand(worker));
        worker.getCommandFramework().add(new EmbedCommand(worker));
    }

}