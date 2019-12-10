package net.notfab.sentinel.sdk.rpc;

import lombok.Data;
import net.notfab.eventti.Event;

import java.util.HashMap;
import java.util.Map;

@Data
public class RPCRequest extends Event {

    private String tag;
    private String method;
    private Map<String, Object> parameters = new HashMap<>();

    public RPCRequest addParam(String name, Object value) {
        this.parameters.put(name.toLowerCase(), value);
        return this;
    }

    public boolean hasParam(String name) {
        return this.parameters.containsKey(name.toLowerCase());
    }

    public Object getParam(String name) {
        return this.parameters.get(name.toLowerCase());
    }

}