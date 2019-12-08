package net.notfab.sentinel.example.worker.commands;

import net.notfab.sentinel.sdk.entities.discord.Member;
import net.notfab.sentinel.sdk.entities.discord.TextChannel;
import net.notfab.sentinel.worker.Messenger;
import net.notfab.sentinel.worker.SentinelCommand;
import net.notfab.sentinel.worker.SentinelWorker;

import java.util.Arrays;
import java.util.List;

public class HelloCommand extends SentinelCommand {

    public HelloCommand(SentinelWorker worker) {
        super(worker, "hello");
    }

    @Override
    public void onCommand(Member member, TextChannel channel, String[] args) {
        System.out.println("Received Command from " + member.getNickname() + " on " + channel.getName());
        List<Member> members = this.getWorker().getRPC().getMembers(channel.getGuild());
        Messenger.send(channel, "Hello " + member.getNickname() + " - " + Arrays.toString(args) + " - " + members.size());
    }

}