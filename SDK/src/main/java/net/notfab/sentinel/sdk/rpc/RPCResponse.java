package net.notfab.sentinel.sdk.rpc;

import lombok.Data;
import net.notfab.sentinel.sdk.entities.SentinelMessage;

@Data
public class RPCResponse implements SentinelMessage {

    private String tag;
    private String response;

}