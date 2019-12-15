package net.notfab.sentinel.sdk.discord.messenger;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Embed {

    private String title;
    private String titleURL;
    private String description;
    private String author;
    private String authorURL;
    private String authorIcon;
    private String footer;
    private String footerIcon;
    private String image;
    private String thumbnail;
    private Integer color;
    private List<EmbedField> fieldList = new ArrayList<>();

}