package net.notfab.sentinel.gateway.jda;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.notfab.sentinel.example.gateway.rabbit.MessengerListenerRabbit;
import net.notfab.sentinel.example.gateway.redis.MessengerListenerRedis;
import net.notfab.sentinel.gateway.SentinelGateway;
import net.notfab.sentinel.sdk.Channels;
import net.notfab.sentinel.sdk.Environment;
import net.notfab.sentinel.sdk.MessageBroker;
import net.notfab.sentinel.sdk.core.ExchangeType;
import net.notfab.sentinel.sdk.core.redis.RedisMessageBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class JDASentinelGateway extends ListenerAdapter implements SentinelGateway {

    private static final Logger logger = LoggerFactory.getLogger(SentinelGateway.class);
    private final MessageBroker broker;

    public JDASentinelGateway(MessageBroker broker) {
        this.broker = broker;
        this.broker.registerChannels(ExchangeType.Fanout, Channels.MESSENGER);
        try {
            new JDABuilder()
                    .setToken(Environment.get("TOKEN", null))
                    .addEventListeners(new CommandListener(this, this.broker))
                    .addEventListeners(this)
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        logger.info("JDA Ready");
        if (broker instanceof RedisMessageBroker) {
            this.broker.addListener(new MessengerListenerRedis(event.getJDA()), Channels.MESSENGER);
        } else {
            this.broker.addListener(new MessengerListenerRabbit(event.getJDA()), Channels.MESSENGER);
        }
    }

    @Override
    public Function<Long, List<String>> getPrefixProvider() {
        return guild -> Arrays.asList("L!", "!", "<@147409622603399168>", "<@!147409622603399168>");
    }

}