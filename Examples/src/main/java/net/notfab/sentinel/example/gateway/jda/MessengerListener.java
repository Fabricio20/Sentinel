package net.notfab.sentinel.example.gateway.jda;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.notfab.eventti.EventHandler;
import net.notfab.eventti.Listener;
import net.notfab.sentinel.example.gateway.jda.mapper.SentinelToJDA;
import net.notfab.sentinel.sdk.discord.events.MessengerEvent;

public class MessengerListener implements Listener {

    private ShardManager shardManager;

    public MessengerListener(ShardManager shardManager) {
        this.shardManager = shardManager;
    }

    @EventHandler
    public void onMessage(MessengerEvent message) {
        TextChannel textChannel = this.shardManager
                .getTextChannelById(message.getChannel().getId());
        if (textChannel != null) {
            textChannel.sendMessage(SentinelToJDA.map(message.getMessage(), message.isOverride()).build()).queue();
        }
    }

}