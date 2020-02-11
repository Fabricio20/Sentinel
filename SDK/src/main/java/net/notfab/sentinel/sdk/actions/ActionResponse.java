package net.notfab.sentinel.sdk.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.notfab.eventti.Event;

import java.util.List;

public class ActionResponse extends Event {

    @Getter
    @Setter
    private String tag;

    @Setter
    @Getter
    private Object payload;

    @Setter
    private List<Object> payloadList;

    @JsonIgnore
    public <T> T get() {
        return (T) payload;
    }

    @JsonProperty("payloadList")
    public <T> List<T> getList() {
        return (List<T>) this.payloadList;
    }

}