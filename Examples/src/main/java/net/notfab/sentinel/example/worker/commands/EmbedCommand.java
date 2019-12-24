package net.notfab.sentinel.example.worker.commands;

import net.notfab.sentinel.example.worker.Messenger;
import net.notfab.sentinel.sdk.discord.command.SentinelCommand;
import net.notfab.sentinel.sdk.discord.entities.Member;
import net.notfab.sentinel.sdk.discord.entities.TextChannel;
import net.notfab.sentinel.sdk.discord.messenger.EmbedBuilder;

public class EmbedCommand extends SentinelCommand {

    public EmbedCommand() {
        super("embed");
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