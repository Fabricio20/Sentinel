package net.notfab.sentinel.gateway.jda.rpc;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.notfab.sentinel.gateway.mapper.JDAMapper;
import net.notfab.sentinel.sdk.rpc.RPCFunction;
import net.notfab.sentinel.sdk.rpc.RPCRequest;

import java.util.stream.Collectors;

public class MembersRPC implements RPCFunction {

    private final JDA api;

    public MembersRPC(JDA api) {
        this.api = api;
    }

    @Override
    public String getMethod() {
        return "getMembers";
    }

    @Override
    public Object onRequest(RPCRequest request) {
        long guildId = (long) request.getParam("guild");
        Guild guild = this.api.getGuildById(guildId);
        if (guild == null) {
            return false;
        }
        return guild.getMembers().parallelStream()
                .map(JDAMapper::map)
                .collect(Collectors.toList());
    }

}