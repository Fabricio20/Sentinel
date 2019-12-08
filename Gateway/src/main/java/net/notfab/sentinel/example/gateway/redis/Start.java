package net.notfab.sentinel.example.gateway.redis;

import net.notfab.sentinel.gateway.jda.JDASentinelGateway;
import net.notfab.sentinel.sdk.MessageBroker;
import net.notfab.sentinel.sdk.core.redis.RedisMessageBroker;

public class Start {

    public static void main(String[] args) {
        MessageBroker broker = new RedisMessageBroker();
        new JDASentinelGateway(broker);
    }

}