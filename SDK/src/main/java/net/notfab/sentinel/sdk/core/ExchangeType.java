package net.notfab.sentinel.sdk.core;

public enum ExchangeType {

    /**
     * Direct sends a message directly to a consumer. (Worker).
     */
    Direct,

    /**
     * Fanout sends a message to all consumers. (Broadcast).
     */
    Fanout;

}