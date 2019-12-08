package net.notfab.sentinel.sdk.entities.discord;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class User implements Mentionable {

    private long id;
    private String name;

    @Override
    @JsonIgnore
    public String getAsMention() {
        return "<@" + id + ">";
    }

}