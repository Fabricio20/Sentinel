package net.notfab.sentinel.sdk.example;

import lombok.Getter;
import lombok.Setter;
import net.notfab.eventti.Event;

@Getter
@Setter
public class ExampleEvent extends Event {

    private boolean test = false;

}