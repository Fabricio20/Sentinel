package net.notfab.sentinel.gateway.mapper;

import net.notfab.sentinel.sdk.entities.discord.Guild;
import net.notfab.sentinel.sdk.entities.discord.Member;
import net.notfab.sentinel.sdk.entities.discord.TextChannel;
import net.notfab.sentinel.sdk.entities.discord.User;

public class JDAMapper {

    public static Member map(net.dv8tion.jda.api.entities.Member entity) {
        Member member = new Member();
        member.setNickname(entity.getEffectiveName());
        member.setGuild(map(entity.getGuild()));
        member.setUser(map(entity.getUser()));
        return member;
    }

    public static Guild map(net.dv8tion.jda.api.entities.Guild entity) {
        Guild guild = new Guild();
        guild.setId(entity.getIdLong());
        guild.setName(entity.getName());
        return guild;
    }

    public static User map(net.dv8tion.jda.api.entities.User entity) {
        User user = new User();
        user.setId(entity.getIdLong());
        user.setName(entity.getName());
        return user;
    }

    public static TextChannel map(net.dv8tion.jda.api.entities.TextChannel entity) {
        TextChannel channel = new TextChannel();
        channel.setId(entity.getIdLong());
        channel.setName(entity.getName());
        channel.setGuild(map(entity.getGuild()));
        return channel;
    }

}