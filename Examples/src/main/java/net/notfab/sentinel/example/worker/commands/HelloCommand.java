package net.notfab.sentinel.example.worker.commands;

import net.notfab.sentinel.sdk.actions.Action;
import net.notfab.sentinel.sdk.discord.command.SentinelCommand;
import net.notfab.sentinel.sdk.discord.entities.Member;
import net.notfab.sentinel.sdk.discord.entities.TextChannel;
import net.notfab.sentinel.sdk.discord.messenger.Messenger;

import java.util.Arrays;
import java.util.List;

public class HelloCommand extends SentinelCommand {

    public HelloCommand() {
        super("hello");
    }

    @Override
    public void onCommand(Member member, TextChannel channel, String[] args) {
        List<Member> members = Action
                .from("getMembers")
                .put("guild", channel.getGuild().getId())
                .awaitList();
        Messenger.send(channel, "Hello " + member.getNickname() + " - " + Arrays.toString(args) + " - " + members.size());
    }

}