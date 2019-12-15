package net.notfab.sentinel.sdk.discord.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Member implements Mentionable {

    private User user;
    private Guild guild;
    private String nickname;

    @Override
    @JsonIgnore
    public String getAsMention() {
        return "<@!" + user.getId() + ">";
    }

}