package net.notfab.sentinel.example.gateway.redis;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.notfab.sentinel.gateway.BotPrefix;
import net.notfab.sentinel.gateway.SentinelGateway;
import net.notfab.sentinel.gateway.jda.JDASentinelGatewayBuilder;
import net.notfab.sentinel.gateway.jda.SentinelListener;
import net.notfab.sentinel.sdk.Environment;
import net.notfab.sentinel.sdk.MessageBroker;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class Start {

    public static void main(String[] args) throws Exception {
        SentinelListener listener = new SentinelListener();
        JDA jda = new JDABuilder()
                .setToken(Environment.get("TOKEN", null))
                .addEventListeners(listener)
                .build();
        // --
        MessageBroker broker = new MessageBroker();
        Function<Long, List<BotPrefix>> prefix = (id) -> Collections
                .singletonList(new BotPrefix("L!", "L!"));
        Function<Long, JDA> shard = (id) -> jda;
        SentinelGateway gateway = new JDASentinelGatewayBuilder()
                .setMessageBroker(broker)
                .setPrefixFunction(prefix)
                .setSentinelListener(listener)
                .setShardCount(1)
                .setShardFunction(shard)
                .build();
    }

}