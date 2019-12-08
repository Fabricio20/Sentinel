package net.notfab.sentinel.sdk.core.redis;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.notfab.sentinel.sdk.core.ExchangeType;
import net.notfab.sentinel.sdk.core.SentinelListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

import java.io.IOException;

public abstract class RedisSentinelListener<T> extends JedisPubSub implements SentinelListener<T> {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(RedisSentinelListener.class);

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private final Class<T> tClass;

    public RedisSentinelListener(Class<T> tClass) {
        this.tClass = tClass;
    }

    @Override
    public abstract void onMessage(String channel, T message);

    @Override
    public ExchangeType getExchangeType() {
        return ExchangeType.Fanout;
    }

    @Override
    public void onMessage(String channel, String message) {
        try {
            T object = objectMapper.readValue(message, tClass);
            this.onMessage(channel, object);
        } catch (IOException ex) {
            logger.error("Error during deserialization", ex);
        }
    }

}
