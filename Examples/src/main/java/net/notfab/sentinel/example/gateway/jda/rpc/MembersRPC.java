package net.notfab.sentinel.example.gateway.jda.rpc;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.notfab.sentinel.example.gateway.jda.mapper.JDAtoSentinel;
import net.notfab.sentinel.sdk.rpc.RPCFunction;
import net.notfab.sentinel.sdk.rpc.RPCRequest;

import java.util.stream.Collectors;

public class MembersRPC implements RPCFunction {

    private final ShardManager shardManager;

    public MembersRPC(ShardManager shardManager) {
        this.shardManager = shardManager;
    }

    @Override
    public String getMethod() {
        return "getMembers";
    }

    @Override
    public Object onRequest(RPCRequest request) {
        long guildId = (long) request.getParam("guild");
        Guild guild = this.shardManager.getGuildById(guildId);
        if (guild == null) {
            return null;
        }
        return guild.getMembers().parallelStream()
                .map(JDAtoSentinel::map)
                .collect(Collectors.toList());
    }

}