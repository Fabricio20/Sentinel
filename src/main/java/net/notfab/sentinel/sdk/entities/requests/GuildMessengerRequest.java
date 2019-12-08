package net.notfab.sentinel.sdk.entities.requests;

import lombok.Data;
import net.notfab.sentinel.sdk.entities.SentinelMessage;
import net.notfab.sentinel.sdk.entities.discord.TextChannel;
import net.notfab.sentinel.sdk.entities.messenger.Message;

@Data
public class GuildMessengerRequest implements SentinelMessage {

    private TextChannel channel;
    private Message message;
    private boolean override = false;

}