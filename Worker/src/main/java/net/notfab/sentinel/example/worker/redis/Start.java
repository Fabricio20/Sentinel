package net.notfab.sentinel.example.worker.redis;

import net.notfab.sentinel.example.worker.commands.EmbedCommand;
import net.notfab.sentinel.example.worker.commands.HelloCommand;
import net.notfab.sentinel.sdk.MessageBroker;
import net.notfab.sentinel.sdk.core.redis.RedisMessageBroker;
import net.notfab.sentinel.worker.SentinelWorker;

public class Start {

    public static void main(String[] args) throws InterruptedException {
        MessageBroker broker = new RedisMessageBroker();
        // --
        SentinelWorker worker = new SentinelWorker(broker);
        worker.getCommandManager().add(new HelloCommand());
        worker.getCommandManager().add(new EmbedCommand());
        while (true) {
            Thread.sleep(60000);
        }
    }

}