package net.notfab.sentinel.sdk.entities.messenger;

import java.awt.*;

public class EmbedBuilder {

    private final Embed embed = new Embed();

    public Embed build() {
        return this.embed;
    }

    public EmbedBuilder setAuthor(String name) {
        return this.setAuthor(name, null, null);
    }

    public EmbedBuilder setAuthor(String name, String url) {
        return this.setAuthor(name, url, null);
    }

    public EmbedBuilder setAuthor(String name, String url, String icon) {
        this.embed.setAuthor(name);
        this.embed.setAuthorURL(url);
        this.embed.setAuthorIcon(icon);
        return this;
    }

    public EmbedBuilder setTitle(String title) {
        return this.setTitle(title, null);
    }

    public EmbedBuilder setTitle(String title, String url) {
        this.embed.setTitle(title);
        this.embed.setTitleURL(url);
        return this;
    }

    public EmbedBuilder setDescription(String description) {
        this.embed.setDescription(description);
        return this;
    }

    public EmbedBuilder addField(String title, String content, boolean inLine) {
        EmbedField field = new EmbedField();
        field.setInline(inLine);
        field.setValue(content);
        field.setName(title);
        this.embed.getFieldList().add(field);
        return this;
    }

    public EmbedBuilder addBlankField(boolean inLine) {
        EmbedField field = new EmbedField();
        field.setBlank(true);
        field.setInline(inLine);
        this.embed.getFieldList().add(field);
        return this;
    }

    public EmbedBuilder setFooter(String title) {
        return this.setFooter(title, null);
    }

    public EmbedBuilder setFooter(String title, String icon) {
        this.embed.setFooter(title);
        this.embed.setFooterIcon(icon);
        return this;
    }

    public EmbedBuilder setImage(String url) {
        this.embed.setImage(url);
        return this;
    }

    public EmbedBuilder setThumbnail(String thumbnail) {
        this.embed.setThumbnail(thumbnail);
        return this;
    }

    public EmbedBuilder setColor(int color) {
        this.embed.setColor(color);
        return this;
    }

    /**
     * Sets the Embed color via HEX. The # in front is MANDATORY.
     *
     * @param colorHex - Color to Use.
     */
    public EmbedBuilder setColorHex(String colorHex) {
        Color color = new Color(Integer.valueOf(colorHex.substring(1, 3), 16),
                Integer.valueOf(colorHex.substring(3, 5), 16),
                Integer.valueOf(colorHex.substring(5, 7), 16));
        this.embed.setColor(color.getRGB());
        return this;
    }

}