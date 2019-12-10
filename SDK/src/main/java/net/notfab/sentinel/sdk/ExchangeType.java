package net.notfab.sentinel.sdk;

public enum ExchangeType {

    /**
     * Queue exchange (jobs are submitted to be run by a single node).
     */
    QUEUE,

    /**
     * Direct exchange (messages are read by all nodes listening).
     */
    DIRECT;

}