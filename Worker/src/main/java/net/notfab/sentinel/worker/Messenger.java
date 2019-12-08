package net.notfab.sentinel.worker;

import net.notfab.sentinel.sdk.Channels;
import net.notfab.sentinel.sdk.MessageBroker;
import net.notfab.sentinel.sdk.entities.discord.TextChannel;
import net.notfab.sentinel.sdk.entities.messenger.Embed;
import net.notfab.sentinel.sdk.entities.messenger.EmbedBuilder;
import net.notfab.sentinel.sdk.entities.messenger.Message;
import net.notfab.sentinel.sdk.entities.messenger.MessageBuilder;
import net.notfab.sentinel.sdk.entities.requests.GuildMessengerRequest;

public class Messenger {

    public static void send(TextChannel channel, String message) {
        send(channel, new MessageBuilder().setContent(message).build());
    }

    public static void send(TextChannel channel, EmbedBuilder builder) {
        send(channel, new MessageBuilder().setEmbed(builder.build()).build());
    }

    public static void send(TextChannel channel, Embed embed) {
        send(channel, new MessageBuilder().setEmbed(embed).build());
    }

    public static void send(TextChannel channel, MessageBuilder builder) {
        send(channel, builder.build());
    }

    public static void send(TextChannel channel, Message message) {
        GuildMessengerRequest request = new GuildMessengerRequest();
        request.setChannel(channel);
        request.setMessage(message);
        MessageBroker.getInstance().sendMessage(request, Channels.MESSENGER);
    }

}