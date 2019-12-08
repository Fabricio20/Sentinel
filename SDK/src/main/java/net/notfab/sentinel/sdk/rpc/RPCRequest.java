package net.notfab.sentinel.sdk.rpc;

import lombok.Data;
import net.notfab.sentinel.sdk.entities.SentinelMessage;

import java.util.HashMap;
import java.util.Map;

@Data
public class RPCRequest implements SentinelMessage {

    private String tag;
    private String method;
    private Map<String, Object> parameters = new HashMap<>();

    public RPCRequest addParam(String name, Object value) {
        this.parameters.put(name, value);
        return this;
    }

}