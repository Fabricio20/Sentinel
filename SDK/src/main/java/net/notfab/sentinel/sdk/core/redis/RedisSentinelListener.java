package net.notfab.sentinel.sdk.core.redis;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.notfab.sentinel.sdk.core.SentinelListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

import java.io.IOException;

public class RedisSentinelListener<T> extends JedisPubSub {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(RedisSentinelListener.class);

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private final SentinelListener<T> listener;

    RedisSentinelListener(SentinelListener<T> listener) {
        this.listener = listener;
    }

    @Override
    public void onMessage(String channel, String message) {
        try {
            T object = objectMapper.readValue(message, this.listener.getClazz());
            this.listener.onMessage(channel, object);
        } catch (IOException ex) {
            logger.error("Error during deserialization", ex);
        }
    }

}
