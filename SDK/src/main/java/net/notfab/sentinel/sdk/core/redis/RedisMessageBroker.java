package net.notfab.sentinel.sdk.core.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.notfab.sentinel.sdk.MessageBroker;
import net.notfab.sentinel.sdk.core.SentinelListener;
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

public class RedisMessageBroker extends MessageBroker {

    private final static Logger logger = LoggerFactory.getLogger(MessageBroker.class);

    private JedisPool jedisPool;
    private final Map<String, List<JedisPubSub>> listeners = new HashMap<>();
    private final ExecutorService service = Executors.newCachedThreadPool();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RedisMessageBroker() {
        super();
        this.jedisPool = new JedisPool(new IRedisConfig(), System.getenv("IP"), 6379, 2000, System.getenv("PASSWORD"));
    }

    @Override
    public void addListener(SentinelListener listener, String... channels) {
        if (!(listener instanceof RedisSentinelListener)) {
            return;
        }
        RedisSentinelListener redisListener = (RedisSentinelListener) listener;
        channels = Stream.of(channels).map(String::toUpperCase).toArray(String[]::new);
        Stream.of(channels).forEach((channel) -> this.listeners.compute(channel.toUpperCase(), (k, v) -> {
            if (v == null) {
                v = new ArrayList<>();
            }
            v.add(redisListener);
            return v;
        }));
        String[] finalChannels = channels;
        this.service.submit(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.subscribe(redisListener, finalChannels);
            } catch (Exception ex) {
                logger.error("Error binding to channel " + Arrays.toString(finalChannels), ex);
            }
        });
    }

    @Override
    public void publish(SentinelMessage sentinelMessage, String... channels) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            String message = this.objectMapper.writeValueAsString(sentinelMessage);
            for (String channel : channels) {
                jedis.publish(channel.toUpperCase(), message);
            }
        } catch (JsonProcessingException ex) {
            logger.error("Error creating message payload", ex);
        }
    }

    @Override
    public void shutdown() {
        this.listeners.values().forEach(x -> x.forEach(JedisPubSub::unsubscribe));
        this.service.shutdownNow();
        this.listeners.clear();
        this.jedisPool.close();
    }

}