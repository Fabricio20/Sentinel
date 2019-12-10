package net.notfab.sentinel.gateway.jda.mapper;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message.MentionType;
import net.notfab.sentinel.sdk.entities.messenger.Embed;
import net.notfab.sentinel.sdk.entities.messenger.Message;

public class SentinelToJDA {

    public static MessageBuilder map(Message message, boolean isOverride) {
        MessageBuilder builder = new MessageBuilder();
        if (message.getContent() != null) {
            builder.setContent(message.getContent());
        }
        if (message.getEmbed() != null) {
            builder.setEmbed(map(message.getEmbed()).build());
        }
        if (!isOverride) {
            builder.stripMentions((JDA) null, MentionType.HERE, MentionType.EVERYONE);
        }
        return builder;
    }

    public static EmbedBuilder map(Embed embed) {
        EmbedBuilder builder = new EmbedBuilder();
        if (embed.getDescription() != null) {
            builder.setDescription(embed.getDescription());
        }
        if (embed.getTitle() != null) {
            builder.setTitle(embed.getTitle(), embed.getTitleURL());
        }
        if (embed.getAuthor() != null) {
            builder.setAuthor(embed.getAuthor(), embed.getAuthorURL(), embed.getAuthorIcon());
        }
        if (!embed.getFieldList().isEmpty()) {
            embed.getFieldList().forEach(x -> {
                if (x.isBlank()) {
                    builder.addBlankField(x.isInline());
                } else {
                    builder.addField(x.getName(), x.getValue(), x.isInline());
                }
            });
        }
        if (embed.getFooter() != null) {
            builder.setFooter(embed.getFooter(), embed.getFooterIcon());
        }
        if (embed.getImage() != null) {
            builder.setImage(embed.getImage());
        }
        if (embed.getThumbnail() != null) {
            builder.setThumbnail(embed.getThumbnail());
        }
        if (embed.getColor() != null) {
            builder.setColor(embed.getColor());
        }
        return builder;
    }

}
