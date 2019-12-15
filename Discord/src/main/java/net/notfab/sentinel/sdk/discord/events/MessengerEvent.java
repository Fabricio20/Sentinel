package net.notfab.sentinel.sdk.discord.events;

import lombok.Data;
import net.notfab.eventti.Event;
import net.notfab.sentinel.sdk.discord.entities.TextChannel;
import net.notfab.sentinel.sdk.discord.messenger.Message;

@Data
public class MessengerEvent extends Event {

    private TextChannel channel;
    private Message message;
    private boolean override = false;

}