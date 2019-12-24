package net.notfab.sentinel.example.worker;

import net.notfab.sentinel.sdk.DiscordChannels;
import net.notfab.sentinel.sdk.ExchangeType;
import net.notfab.sentinel.sdk.MessageBroker;
import net.notfab.sentinel.sdk.discord.entities.TextChannel;
import net.notfab.sentinel.sdk.discord.messenger.Embed;
import net.notfab.sentinel.sdk.discord.messenger.EmbedBuilder;
import net.notfab.sentinel.sdk.discord.messenger.Message;
import net.notfab.sentinel.sdk.discord.messenger.MessageBuilder;
import net.notfab.sentinel.sdk.discord.events.MessengerEvent;

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
        MessengerEvent request = new MessengerEvent();
        request.setChannel(channel);
        request.setMessage(message);
        MessageBroker.getInstance().publish(request, ExchangeType.DIRECT, DiscordChannels.MESSENGER);
    }

}