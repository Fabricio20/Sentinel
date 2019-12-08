package net.notfab.sentinel.sdk.entities;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

import java.io.IOException;

public abstract class SentinelListener<T> extends JedisPubSub {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(SentinelListener.class);

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private final Class<T> tClass;

    public SentinelListener(Class<T> tClass) {
        this.tClass = tClass;
    }

    public abstract void onMessage(String channel, T message);

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
