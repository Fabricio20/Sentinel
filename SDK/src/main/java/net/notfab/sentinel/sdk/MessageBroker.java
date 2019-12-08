package net.notfab.sentinel.sdk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import net.notfab.sentinel.sdk.entities.SentinelMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class MessageBroker {

    private final static Logger logger = LoggerFactory.getLogger(MessageBroker.class);

    @Getter
    private static MessageBroker Instance;

    private JedisPool jedisPool;
    private final Map<String, List<JedisPubSub>> listeners = new HashMap<>();
    private final ExecutorService service = Executors.newCachedThreadPool();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MessageBroker() {
        Instance = this;
        this.jedisPool = new JedisPool(new IRedisConfig(), System.getenv("IP"), 6379, 2000, System.getenv("PASSWORD"));
    }

    public void addListener(JedisPubSub listener, String... channels) {
        channels = Stream.of(channels).map(String::toUpperCase).toArray(String[]::new);
        Stream.of(channels).forEach((channel) -> this.listeners.compute(channel.toUpperCase(), (k, v) -> {
            if (v == null) {
                v = new ArrayList<>();
            }
            v.add(listener);
            return v;
        }));
        String[] finalChannels = channels;
        this.service.submit(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.subscribe(listener, finalChannels);
            } catch (Exception ex) {
                logger.error("Error binding to channel " + Arrays.toString(finalChannels), ex);
            }
        });
    }

    public void sendMessage(SentinelMessage sentinelMessage, String... channels) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            String message = this.objectMapper.writeValueAsString(sentinelMessage);
            for (String channel : channels) {
                jedis.publish(channel.toUpperCase(), message);
            }
        } catch (JsonProcessingException ex) {
            logger.error("Error creating message payload", ex);
        }
    }

    public void shutdown() {
        this.listeners.values().forEach(x -> x.forEach(JedisPubSub::unsubscribe));
        this.service.shutdownNow();
        this.listeners.clear();
        this.jedisPool.close();
    }

}