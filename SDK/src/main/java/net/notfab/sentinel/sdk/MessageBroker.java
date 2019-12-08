package net.notfab.sentinel.sdk;

import lombok.Getter;
import net.notfab.sentinel.sdk.core.ExchangeType;
import net.notfab.sentinel.sdk.core.SentinelListener;
import net.notfab.sentinel.sdk.entities.SentinelMessage;

public abstract class MessageBroker {

    @Getter
    private static MessageBroker Instance;

    public MessageBroker() {
        Instance = this;
    }

    public abstract void registerChannels(ExchangeType exchangeType, String... channels);

    public abstract void addListener(SentinelListener listener, String... channels);

    public abstract void publish(SentinelMessage message, String... channels);

    public abstract void ack(long deliveryTag, boolean ack);

    public abstract void shutdown();

}