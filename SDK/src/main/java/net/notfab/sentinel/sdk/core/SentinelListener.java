package net.notfab.sentinel.sdk.core;

public interface SentinelListener<T> {

    Class<T> getClazz();

    boolean isAutoAck();

    boolean onMessage(String channel, T message);

}
