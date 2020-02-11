package net.notfab.sentinel.sdk.actions;

import lombok.Getter;
import lombok.Setter;
import net.notfab.eventti.Event;

import java.util.HashMap;
import java.util.Map;

public class ActionRequest extends Event {

    @Getter
    @Setter
    private String tag;

    @Getter
    @Setter
    private String method;

    @Getter
    @Setter
    private Map<String, Object> parameters = new HashMap<>();

}