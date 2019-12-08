package net.notfab.sentinel.worker.commands;

import net.notfab.sentinel.worker.SentinelCommand;
import net.notfab.sentinel.sdk.entities.discord.Member;
import net.notfab.sentinel.sdk.entities.discord.TextChannel;
import net.notfab.sentinel.worker.Messenger;

import java.util.Arrays;

public class HelloCommand extends SentinelCommand {

    public HelloCommand() {
        super("hello");
    }

    @Override
    public void onCommand(Member member, TextChannel channel, String[] args) {
        System.out.println("Received Command from " + member.getNickname() + " on " + channel.getName());
        Messenger.send(channel, "Hello " + member.getNickname() + " - " + Arrays.toString(args));
    }

}