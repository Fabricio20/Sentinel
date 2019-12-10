package net.notfab.sentinel.gateway.jda;

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.notfab.sentinel.gateway.BotPrefix;
import net.notfab.sentinel.gateway.SentinelGateway;
import net.notfab.sentinel.sdk.MessageBroker;

import java.util.List;
import java.util.function.Function;

@Getter
public class JDASentinelGatewayBuilder {

    private MessageBroker messageBroker;
    private long num_shards;
    private Function<Long, JDA> shardFunction;
    private Function<Long, List<BotPrefix>> prefixFunction;
    private SentinelListener sentinelListener;

    public SentinelGateway build() {
        return new JDASentinelGateway(messageBroker, num_shards, shardFunction, prefixFunction, sentinelListener);
    }

    /**
     * The message broker instance.
     *
     * @param messageBroker - Message broker.
     * @return chain.
     */
    public JDASentinelGatewayBuilder setMessageBroker(MessageBroker messageBroker) {
        this.messageBroker = messageBroker;
        return this;
    }

    /**
     * The total shard count of the entire bot. (Used for shard calculation).
     *
     * @param shardCount - Total shard count.
     * @return chain.
     */
    public JDASentinelGatewayBuilder setShardCount(long shardCount) {
        this.num_shards = shardCount;
        return this;
    }

    /**
     * Function that maps a shard ID to a JDA instance.
     *
     * @param shardFunction - Shard ID to JDA Function.
     * @return chain.
     */
    public JDASentinelGatewayBuilder setShardFunction(Function<Long, JDA> shardFunction) {
        this.shardFunction = shardFunction;
        return this;
    }

    /**
     * Function that retrieves a list of prefixes for a given guild.
     *
     * @param prefixFunction - Function for prefixes.
     * @return chain.
     */
    public JDASentinelGatewayBuilder setPrefixFunction(Function<Long, List<BotPrefix>> prefixFunction) {
        this.prefixFunction = prefixFunction;
        return this;
    }

    /**
     * Listener instance for your gateway. Only one per gateway instance.
     *
     * @param sentinelListener - Shared listener instance.
     * @return chain.
     */
    public JDASentinelGatewayBuilder setSentinelListener(SentinelListener sentinelListener) {
        this.sentinelListener = sentinelListener;
        return this;
    }

}