package net.notfab.sentinel.sdk.entities.messenger;

import lombok.Data;

@Data
public class Embed {

    private String title;
    private String titleURL;
    private String description;
    private String author;
    private String authorURL;
    private String authorIcon;

}