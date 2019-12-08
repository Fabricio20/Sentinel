package net.notfab.sentinel.worker.commands;

import net.notfab.sentinel.sdk.entities.SentinelCommand;
import net.notfab.sentinel.sdk.entities.discord.Member;
import net.notfab.sentinel.sdk.entities.discord.TextChannel;
import net.notfab.sentinel.sdk.entities.messenger.EmbedBuilder;
import net.notfab.sentinel.worker.Messenger;

public class EmbedCommand extends SentinelCommand {

    public EmbedCommand() {
        super("embed");
    }

    @Override
    public void onCommand(Member member, TextChannel channel, String[] args) {
        Messenger.send(channel, new EmbedBuilder().setTitle("Embed Title")
                .setDescription("Description Field")
                .setAuthor(member.getNickname(), "https://google.com/", "https://via.placeholder.com/256")
                .build());
    }

}