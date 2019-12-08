package net.notfab.sentinel.sdk.core;

public interface SentinelListener<T> {

    ExchangeType getExchangeType();

    void onMessage(String channel, T message);

}
