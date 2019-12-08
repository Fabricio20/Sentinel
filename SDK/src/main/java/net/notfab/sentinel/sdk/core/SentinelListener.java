package net.notfab.sentinel.sdk.core;

public interface SentinelListener<T> {

    boolean isAutoAck();

    boolean onMessage(String channel, T message);

}
