package net.notfab.sentinel.sdk.discord.command;

import lombok.Getter;
import net.notfab.sentinel.sdk.discord.entities.Member;
import net.notfab.sentinel.sdk.discord.entities.TextChannel;

import java.util.Arrays;
import java.util.List;

public abstract class SentinelCommand {

    @Getter
    private final List<String> names;

    public SentinelCommand(String... names) {
        this.names = Arrays.asList(names);
    }

    public abstract void onCommand(Member member, TextChannel channel, String[] args);

}