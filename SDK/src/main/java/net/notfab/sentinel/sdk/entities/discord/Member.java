package net.notfab.sentinel.sdk.entities.discord;

import lombok.Data;

@Data
public class Member {

    private User user;
    private Guild guild;
    private String nickname;

}