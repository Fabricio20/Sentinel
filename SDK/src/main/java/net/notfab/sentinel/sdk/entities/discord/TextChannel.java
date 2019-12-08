package net.notfab.sentinel.sdk.entities.discord;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class TextChannel implements Mentionable {

    private long id;
    private Guild guild;
    private String name;
    private String topic;
    private int slowMode;

    @Override
    @JsonIgnore
    public String getAsMention() {
        return "<#" + id + ">";
    }

}