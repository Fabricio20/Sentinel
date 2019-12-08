package net.notfab.sentinel.sdk.entities.messenger;

import lombok.Data;

@Data
public class Message {

    private String content;
    private Embed embed;

}