package net.notfab.sentinel.worker;

import net.notfab.sentinel.sdk.Channels;
import net.notfab.sentinel.sdk.MessageBroker;
import net.notfab.sentinel.sdk.entities.discord.TextChannel;
import net.notfab.sentinel.sdk.entities.requests.GuildMessengerRequest;

public class Messenger {

    public static void send(TextChannel channel, String message) {
        GuildMessengerRequest request = new GuildMessengerRequest();
        request.setChannel(channel);
        request.setMessage(message);
        MessageBroker.getInstance().sendMessage(request, Channels.MESSENGER);
    }

}