package net.notfab.sentinel.sdk.rpc;

import lombok.Data;
import net.notfab.eventti.Event;

@Data
public class RPCResponse extends Event {

    private String tag;
    private Object response;

}