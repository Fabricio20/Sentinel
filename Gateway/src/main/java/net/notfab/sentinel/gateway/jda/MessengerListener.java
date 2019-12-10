package net.notfab.sentinel.gateway.jda;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.notfab.eventti.EventHandler;
import net.notfab.eventti.Listener;
import net.notfab.sentinel.gateway.jda.mapper.SentinelToJDA;
import net.notfab.sentinel.sdk.entities.events.MessengerEvent;

public class MessengerListener implements Listener {

    private JDASentinelGateway gateway;

    MessengerListener(JDASentinelGateway gateway) {
        this.gateway = gateway;
    }

    @EventHandler
    public void onMessage(MessengerEvent message) {
        JDA jda = this.gateway.getJDA(message.getChannel().getId());
        if (jda == null) {
            return;
        }
        TextChannel textChannel = jda.getTextChannelById(message.getChannel().getId());
        if (textChannel != null) {
            textChannel.sendMessage(SentinelToJDA.map(message.getMessage(), message.isOverride()).build()).queue();
        }
    }

}