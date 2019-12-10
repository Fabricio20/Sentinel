package net.notfab.sentinel.gateway;

import java.util.List;

public interface SentinelGateway {

    List<BotPrefix> getPrefixList(long guildId);

}