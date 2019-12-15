package net.notfab.sentinel.sdk.discord.messenger;

import lombok.Data;

@Data
public class Message {

    private String content;
    private Embed embed;

}