package net.notfab.sentinel.sdk.entities.discord;

import lombok.Data;

@Data
public class TextChannel {

    private long id;
    private String name;
    private Guild guild;

}