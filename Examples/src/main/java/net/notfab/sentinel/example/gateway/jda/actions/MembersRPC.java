package net.notfab.sentinel.example.gateway.jda.actions;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.notfab.sentinel.example.gateway.jda.mapper.JDAtoSentinel;
import net.notfab.sentinel.sdk.actions.ActionRequest;
import net.notfab.sentinel.sdk.discord.entities.Member;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MembersRPC implements Function<ActionRequest, List<Member>> {

    private final ShardManager shardManager;

    public MembersRPC(ShardManager shardManager) {
        this.shardManager = shardManager;
    }

    @Override
    public List<Member> apply(ActionRequest request) {
        long guildId = (long) request.getParameters().getOrDefault("guild", 0);
        Guild guild = this.shardManager.getGuildById(guildId);
        if (guild == null) {
            return null;
        }
        return guild.getMembers().parallelStream()
                .map(JDAtoSentinel::map)
                .collect(Collectors.toList());
    }

}