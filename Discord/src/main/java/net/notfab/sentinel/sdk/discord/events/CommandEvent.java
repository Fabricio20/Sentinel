package net.notfab.sentinel.sdk.discord.events;

import lombok.Data;
import net.notfab.eventti.Event;
import net.notfab.sentinel.sdk.discord.entities.Member;
import net.notfab.sentinel.sdk.discord.entities.TextChannel;

@Data
public class CommandEvent extends Event {

    private String name;
    private Member member;
    private TextChannel channel;
    private String[] args;

}