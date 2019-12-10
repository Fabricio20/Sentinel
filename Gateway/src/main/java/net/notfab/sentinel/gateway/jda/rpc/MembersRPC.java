package net.notfab.sentinel.gateway.jda.rpc;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.notfab.sentinel.gateway.jda.JDASentinelGateway;
import net.notfab.sentinel.gateway.jda.mapper.JDAtoSentinel;
import net.notfab.sentinel.sdk.rpc.RPCFunction;
import net.notfab.sentinel.sdk.rpc.RPCRequest;

import java.util.stream.Collectors;

public class MembersRPC implements RPCFunction {

    private final JDASentinelGateway gateway;

    public MembersRPC(JDASentinelGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public String getMethod() {
        return "getMembers";
    }

    @Override
    public Object onRequest(RPCRequest request) {
        long guildId = (long) request.getParam("guild");
        JDA jda = this.gateway.getJDA(guildId);
        if (jda == null) {
            return null;
        }
        Guild guild = jda.getGuildById(guildId);
        if (guild == null) {
            return null;
        }
        return guild.getMembers().parallelStream()
                .map(JDAtoSentinel::map)
                .collect(Collectors.toList());
    }

}