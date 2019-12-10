package net.notfab.sentinel.gateway.jda;

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.notfab.sentinel.gateway.BotPrefix;
import net.notfab.sentinel.gateway.SentinelGateway;
import net.notfab.sentinel.gateway.jda.listeners.CommandListener;
import net.notfab.sentinel.gateway.jda.rpc.MembersRPC;
import net.notfab.sentinel.sdk.Channels;
import net.notfab.sentinel.sdk.ExchangeType;
import net.notfab.sentinel.sdk.MessageBroker;
import net.notfab.sentinel.sdk.rpc.RPCManager;

import java.util.List;
import java.util.function.Function;

public class JDASentinelGateway implements SentinelGateway {

    private final MessageBroker messageBroker;
    private final long num_shards;
    private final Function<Long, JDA> shardFunction;
    private final Function<Long, List<BotPrefix>> prefixFunction;
    private SentinelListener sentinelListener;

    @Getter
    private final RPCManager rpcManager;

    JDASentinelGateway(MessageBroker messageBroker, long num_shards,
                       Function<Long, JDA> shardFunction,
                       Function<Long, List<BotPrefix>> prefixFunction, SentinelListener sentinelListener) {
        this.messageBroker = messageBroker;
        this.num_shards = num_shards;
        this.shardFunction = shardFunction;
        this.prefixFunction = prefixFunction;
        this.sentinelListener = sentinelListener;
        this.rpcManager = new RPCManager(this.messageBroker);
        this.addListeners();
        this.addRPCFunctions();
    }

    public JDA getJDA(long snowflake) {
        return this.shardFunction.apply((snowflake >> 22) % num_shards);
    }

    @Override
    public List<BotPrefix> getPrefixList(long guildId) {
        return this.prefixFunction.apply(guildId);
    }

    private void addListeners() {
        // Sentinel
        this.messageBroker.addListener(new MessengerListener(this), ExchangeType.DIRECT, Channels.MESSENGER);
        // JDA
        this.sentinelListener.addListener(GuildMessageReceivedEvent.class, new CommandListener(this.messageBroker, this));
    }

    private void addRPCFunctions() {
        this.rpcManager.addFunction(new MembersRPC(this));
    }

}