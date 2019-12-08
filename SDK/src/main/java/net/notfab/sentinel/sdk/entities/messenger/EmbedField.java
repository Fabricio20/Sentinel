package net.notfab.sentinel.sdk.entities.messenger;

import lombok.Data;

@Data
public class EmbedField {

    private String name;
    private String value;
    private boolean blank;
    private boolean inline;

}