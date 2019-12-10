package net.notfab.sentinel.sdk.internal;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class RedisConfig extends GenericObjectPoolConfig {

    public RedisConfig() {
        // defaults to make your life with connection pool easier :)
        this.setTestWhileIdle(true);
        this.setMinEvictableIdleTimeMillis(60000);
        this.setTimeBetweenEvictionRunsMillis(30000);
        this.setNumTestsPerEvictionRun(-1);
        this.setMaxWaitMillis(DEFAULT_MAX_WAIT_MILLIS); // TODO: Check if making it lower than -1 (setting a limit) would be good
        this.setMaxTotal(1000);
    }

}
