package net.notfab.sentinel.sdk.entities.events;

import lombok.Data;
import net.notfab.eventti.Event;
import net.notfab.sentinel.sdk.entities.discord.Member;
import net.notfab.sentinel.sdk.entities.discord.TextChannel;

@Data
public class CommandEvent extends Event {

    private String name;
    private Member member;
    private TextChannel channel;
    private String[] args;

}