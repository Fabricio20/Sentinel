package net.notfab.sentinel.worker;

import net.notfab.sentinel.worker.commands.EmbedCommand;
import net.notfab.sentinel.worker.commands.HelloCommand;

public class Start {

    public static void main(String[] args) throws InterruptedException {
        SentinelWorker worker = new SentinelWorker();
        worker.getCommandManager().add(new HelloCommand());
        worker.getCommandManager().add(new EmbedCommand());
        while (true) {
            Thread.sleep(60000);
        }
    }

}