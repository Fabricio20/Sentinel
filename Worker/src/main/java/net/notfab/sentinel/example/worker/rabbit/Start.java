package net.notfab.sentinel.example.worker.rabbit;

import net.notfab.sentinel.example.worker.commands.EmbedCommand;
import net.notfab.sentinel.example.worker.commands.HelloCommand;
import net.notfab.sentinel.sdk.MessageBroker;
import net.notfab.sentinel.sdk.core.rabbit.RabbitMessageBroker;
import net.notfab.sentinel.worker.SentinelWorker;

public class Start {

    public static void main(String[] args) throws InterruptedException {
        MessageBroker broker = new RabbitMessageBroker();
        // --
        SentinelWorker worker = new SentinelWorker(broker);
        worker.getCommandManager().add(new HelloCommand(worker));
        worker.getCommandManager().add(new EmbedCommand(worker));
        while (true) {
            Thread.sleep(60000);
        }
    }

}