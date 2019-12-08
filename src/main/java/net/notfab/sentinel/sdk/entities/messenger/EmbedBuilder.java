package net.notfab.sentinel.sdk.entities.messenger;

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

}