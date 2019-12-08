package net.notfab.sentinel.example.gateway.rabbit;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message.MentionType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.notfab.sentinel.sdk.core.rabbit.RabbitSentinelListener;
import net.notfab.sentinel.sdk.entities.messenger.Embed;
import net.notfab.sentinel.sdk.entities.messenger.Message;
import net.notfab.sentinel.sdk.entities.requests.GuildMessengerRequest;

public class MessengerListenerRabbit extends RabbitSentinelListener<GuildMessengerRequest> {

    private final JDA jda;

    public MessengerListenerRabbit(JDA jda) {
        super(GuildMessengerRequest.class);
        this.jda = jda;
    }

    @Override
    public boolean isAutoAck() {
        return true;
    }

    @Override
    public boolean onMessage(String channel, GuildMessengerRequest message) {
        TextChannel textChannel = jda.getTextChannelById(message.getChannel().getId());
        if (textChannel != null) {
            textChannel.sendMessage(this.prepareMessage(message.getMessage(), message.isOverride()).build()).queue();
        } else {
            System.err.println("Unknown TextChannel");
        }
        return true;
    }

    private MessageBuilder prepareMessage(Message message, boolean isOverride) {
        MessageBuilder builder = new MessageBuilder();
        if (message.getContent() != null) {
            builder.setContent(message.getContent());
        }
        if (message.getEmbed() != null) {
            builder.setEmbed(this.prepareEmbed(message.getEmbed()).build());
        }
        if (!isOverride) {
            builder.stripMentions(this.jda, MentionType.HERE, MentionType.EVERYONE);
        }
        return builder;
    }

    private EmbedBuilder prepareEmbed(Embed embed) {
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
