package net.notfab.sentinel.example.worker.commands;

import net.notfab.sentinel.worker.SentinelCommand;
import net.notfab.sentinel.sdk.entities.discord.Member;
import net.notfab.sentinel.sdk.entities.discord.TextChannel;
import net.notfab.sentinel.sdk.entities.messenger.EmbedBuilder;
import net.notfab.sentinel.worker.Messenger;
import net.notfab.sentinel.worker.SentinelWorker;

public class EmbedCommand extends SentinelCommand {

    public EmbedCommand(SentinelWorker worker) {
        super(worker, "embed");
    }

    @Override
    public void onCommand(Member member, TextChannel channel, String[] args) {
        Messenger.send(channel, new EmbedBuilder().setTitle("Embed Title")
                .setDescription("Description Field")
                .setAuthor(member.getNickname(), "https://google.com/", "https://via.placeholder.com/256")
                .setColorHex("#FF00AA")
                .setImage("https://i.kym-cdn.com/entries/icons/original/000/016/546/hidethepainharold.jpg")
                .build());
    }

}