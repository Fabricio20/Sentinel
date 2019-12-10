package net.notfab.sentinel.gateway;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BotPrefix {

    /**
     * This is the actual prefix content, when seen from a raw message.
     */
    private String rawContent;

    /**
     * This is the display content as seen by a user.
     */
    private String displayContent;

}