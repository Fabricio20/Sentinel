package net.notfab.sentinel.worker;

import net.notfab.sentinel.sdk.discord.entities.Guild;
import net.notfab.sentinel.sdk.discord.entities.Member;
import net.notfab.sentinel.sdk.rpc.RPCAction;
import net.notfab.sentinel.sdk.rpc.RPCManager;
import net.notfab.sentinel.sdk.rpc.RPCRequest;
import net.notfab.sentinel.sdk.rpc.RPCResponse;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class RPC {

    private final RPCManager manager;

    RPC(RPCManager manager) {
        this.manager = manager;
    }

    public List<Member> getMembers(Guild guild) {
        RPCRequest roleRequest = new RPCRequest();
        roleRequest.setMethod("getMembers");
        roleRequest.addParam("guild", guild.getId());
        RPCAction roleAction = this.manager.ask(roleRequest);
        RPCResponse response = roleAction.complete();
        if (response != null && response.getResponse() != null) {
            return (List<Member>) response.getResponse();
        }
        return new ArrayList<>();
    }

}