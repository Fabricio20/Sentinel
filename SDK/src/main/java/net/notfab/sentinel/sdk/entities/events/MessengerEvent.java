package net.notfab.sentinel.sdk.entities.events;

import lombok.Data;
import net.notfab.eventti.Event;
import net.notfab.sentinel.sdk.entities.discord.TextChannel;
import net.notfab.sentinel.sdk.entities.messenger.Message;

@Data
public class MessengerEvent extends Event {

    private TextChannel channel;
    private Message message;
    private boolean override = false;

}