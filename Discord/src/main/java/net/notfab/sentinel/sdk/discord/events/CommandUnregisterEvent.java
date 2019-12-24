package net.notfab.sentinel.sdk.discord.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.notfab.eventti.Event;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommandUnregisterEvent extends Event {

    private List<String> names;

}