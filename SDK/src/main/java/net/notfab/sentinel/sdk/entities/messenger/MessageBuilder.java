package net.notfab.sentinel.sdk.entities.messenger;

public class MessageBuilder {

    private final Message message = new Message();
    private StringBuilder content = new StringBuilder();

    public Message build() {
        message.setContent(this.content.toString());
        return message;
    }

    public MessageBuilder append(String message) {
        this.content.append(message);
        return this;
    }

    public MessageBuilder setContent(String message) {
        this.content = new StringBuilder(message);
        return this;
    }

    public MessageBuilder setEmbed(Embed embed) {
        this.message.setEmbed(embed);
        return this;
    }

}