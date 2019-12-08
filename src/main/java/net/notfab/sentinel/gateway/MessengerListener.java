package net.notfab.sentinel.gateway;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.notfab.sentinel.sdk.entities.SentinelListener;
import net.notfab.sentinel.sdk.entities.requests.GuildMessengerRequest;

public class MessengerListener extends SentinelListener<GuildMessengerRequest> {

    private final JDA jda;

    public MessengerListener(JDA jda) {
        super(GuildMessengerRequest.class);
        this.jda = jda;
    }

    @Override
    public void onMessage(String channel, GuildMessengerRequest message) {
        TextChannel textChannel = jda.getTextChannelById(message.getChannel().getId());
        if (textChannel != null) {
            textChannel.sendMessage(message.getMessage()).queue();
        } else {
            System.err.println("Unknown TextChannel");
        }
    }

}
