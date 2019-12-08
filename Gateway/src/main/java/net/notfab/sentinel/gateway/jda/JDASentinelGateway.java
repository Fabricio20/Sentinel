package net.notfab.sentinel.gateway.jda;

import lombok.Getter;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.notfab.sentinel.gateway.SentinelGateway;
import net.notfab.sentinel.gateway.jda.rpc.MembersRPC;
import net.notfab.sentinel.sdk.Channels;
import net.notfab.sentinel.sdk.Environment;
import net.notfab.sentinel.sdk.MessageBroker;
import net.notfab.sentinel.sdk.core.ExchangeType;
import net.notfab.sentinel.sdk.rpc.RPCManager;
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

    @Getter
    private final RPCManager rpcManager;

    public JDASentinelGateway(MessageBroker broker) {
        this.broker = broker;
        this.broker.registerChannels(ExchangeType.Fanout, Channels.MESSENGER);
        this.rpcManager = new RPCManager(this.broker);
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
        this.broker.addListener(new MessengerListener(event.getJDA()), Channels.MESSENGER);
        this.rpcManager.addFunction(new MembersRPC(event.getJDA()));
    }

    @Override
    public Function<Long, List<String>> getPrefixProvider() {
        return guild -> Arrays.asList("L!", "!", "<@147409622603399168>", "<@!147409622603399168>");
    }

}