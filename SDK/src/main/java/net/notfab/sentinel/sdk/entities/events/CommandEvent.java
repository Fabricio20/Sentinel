package net.notfab.sentinel.sdk.entities.events;

import lombok.Data;
import net.notfab.sentinel.sdk.entities.SentinelMessage;
import net.notfab.sentinel.sdk.entities.discord.Member;
import net.notfab.sentinel.sdk.entities.discord.TextChannel;

@Data
public class CommandEvent implements SentinelMessage {

    private String name;
    private Member member;
    private TextChannel channel;
    private String[] args;

}