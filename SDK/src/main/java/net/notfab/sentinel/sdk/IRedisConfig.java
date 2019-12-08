package net.notfab.sentinel.sdk;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class IRedisConfig extends GenericObjectPoolConfig {

    public IRedisConfig() {
        // defaults to make your life with connection pool easier :)
        setTestWhileIdle(true);
        setMinEvictableIdleTimeMillis(60000);
        setTimeBetweenEvictionRunsMillis(30000);
        setNumTestsPerEvictionRun(-1);
        this.setMaxWaitMillis(DEFAULT_MAX_WAIT_MILLIS); // TODO: Check if making it lower than -1 (setting a limit) would be good
        setMaxTotal(1000);
    }

}
