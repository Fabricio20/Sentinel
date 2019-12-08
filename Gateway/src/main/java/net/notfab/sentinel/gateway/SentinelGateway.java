package net.notfab.sentinel.gateway;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.notfab.sentinel.sdk.Channels;
import net.notfab.sentinel.sdk.MessageBroker;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;

public class SentinelGateway extends ListenerAdapter {

    private MessageBroker broker;

    public SentinelGateway() {
        this.broker = new MessageBroker();
        try {
            JDA jda = new JDABuilder()
                    .setToken(System.getenv("TOKEN"))
                    .addEventListeners(new CommandListener(this.broker))
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
        System.out.println("JDA Ready");
        this.broker.addListener(new MessengerListener(event.getJDA()), Channels.MESSENGER);
    }

}
