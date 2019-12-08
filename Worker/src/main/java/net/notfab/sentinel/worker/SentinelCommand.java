package net.notfab.sentinel.worker;

import lombok.Getter;
import net.notfab.sentinel.sdk.entities.discord.Member;
import net.notfab.sentinel.sdk.entities.discord.TextChannel;

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