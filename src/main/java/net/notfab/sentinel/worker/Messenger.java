package net.notfab.sentinel.worker;

import net.notfab.sentinel.sdk.Channels;
import net.notfab.sentinel.sdk.MessageBroker;
import net.notfab.sentinel.sdk.entities.discord.TextChannel;
import net.notfab.sentinel.sdk.entities.messenger.Message;
import net.notfab.sentinel.sdk.entities.messenger.MessageBuilder;
import net.notfab.sentinel.sdk.entities.requests.GuildMessengerRequest;

public class Messenger {

    public static void send(TextChannel channel, String message) {
        GuildMessengerRequest request = new GuildMessengerRequest();
        request.setChannel(channel);
        request.setMessage(new MessageBuilder().setContent(message).build());
        MessageBroker.getInstance().sendMessage(request, Channels.MESSENGER);
    }

    public static void send(TextChannel channel, MessageBuilder builder) {
        GuildMessengerRequest request = new GuildMessengerRequest();
        request.setChannel(channel);
        request.setMessage(builder.build());
        MessageBroker.getInstance().sendMessage(request, Channels.MESSENGER);
    }

    public static void send(TextChannel channel, Message message) {
        GuildMessengerRequest request = new GuildMessengerRequest();
        request.setChannel(channel);
        request.setMessage(message);
        MessageBroker.getInstance().sendMessage(request, Channels.MESSENGER);
    }

}