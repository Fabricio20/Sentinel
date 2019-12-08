package net.notfab.sentinel.example.gateway.rabbit;

import net.notfab.sentinel.gateway.jda.JDASentinelGateway;
import net.notfab.sentinel.sdk.MessageBroker;
import net.notfab.sentinel.sdk.core.rabbit.RabbitMessageBroker;

public class Start {

    public static void main(String[] args) {
        MessageBroker broker = new RabbitMessageBroker();
        new JDASentinelGateway(broker);
    }

}