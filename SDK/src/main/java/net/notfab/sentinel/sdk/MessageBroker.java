package net.notfab.sentinel.sdk;

import lombok.Getter;
import net.notfab.eventti.Event;
import net.notfab.eventti.EventManager;
import net.notfab.eventti.Listener;
import net.notfab.sentinel.sdk.internal.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This is the main sentinel component, it is to be considered a Singleton.
 */
public class MessageBroker {

    private static final Logger logger = LoggerFactory.getLogger(MessageBroker.class);

    @Getter
    private static MessageBroker Instance;

    private final Map<Tuple<ExchangeType, String>, Set<Listener>> listenerMap = new HashMap<>();
    private final ExecutorService service = Executors.newCachedThreadPool();
    private final AtomicBoolean terminating = new AtomicBoolean(false);
    private final EventManager eventManager = new EventManager();
    private final JedisPool jedisPool;

    public MessageBroker() {
        Instance = this;
        if (Environment.has("REDIS_PASSWORD")) {
            this.jedisPool = new JedisPool(new RedisConfig(), Environment.get("REDIS_IP", "localhost"), 6379,
                    2000, Environment.get("REDIS_PASSWORD", ""));
        } else {
            this.jedisPool = new JedisPool(new RedisConfig(), Environment.get("REDIS_IP", "localhost"), 6379);
        }
    }

    public MessageBroker(String redisPassword, String redisIP) {
        Instance = this;
        this.jedisPool = new JedisPool(new RedisConfig(), redisIP, 6379, 2000, redisPassword);
    }

    /**
     * Adds a listener (can listen to multiple events).
     *
     * @param listener     - Listener.
     * @param exchangeType - Exchange type (Queue or Direct).
     * @param channel      - Channel to listen in.
     */
    public void addListener(Listener listener, ExchangeType exchangeType, String channel) {
        Tuple<ExchangeType, String> tuple = new Tuple<>(exchangeType, channel);
        if (this.listenerMap.containsKey(tuple)) {
            logger.debug("Added listener " + listener + " to existing channel " + tuple);
            Set<Listener> listenerList = this.listenerMap.get(tuple);
            listenerList.add(listener);
            this.listenerMap.put(tuple, listenerList);
        } else {
            logger.debug("Added listener " + listener + " to new channel " + tuple);
            Set<Listener> listenerList = new HashSet<>();
            listenerList.add(listener);
            this.listenerMap.put(tuple, listenerList);
            this.service.submit(() -> {
                try (Jedis jedis = this.jedisPool.getResource()) {
                    if (exchangeType == ExchangeType.DIRECT) {
                        jedis.subscribe(new DirectRedisListener(this.eventManager), channel);
                    } else {
                        QueueRedisListener redisListener = new QueueRedisListener(jedis, channel, eventManager);
                        do {
                            redisListener.run();
                        } while (!terminating.get());
                    }
                } catch (Exception ex) {
                    logger.error("Error processing a listener", ex);
                }
            });
        }
        this.eventManager.addListener(listener);
    }

    /**
     * Publishes a message to the network.
     *
     * @param event        - Message to send.
     * @param exchangeType - Exchange type (Queue or Direct).
     * @param channel      - Channel to publish the message in.
     */
    public void publish(Event event, ExchangeType exchangeType, String channel) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            String message = new EventWrapper(event.getClass().getName(), event).toString();
            if (exchangeType == ExchangeType.DIRECT) {
                jedis.publish(channel, message);
            } else {
                jedis.lpush(channel, message);
            }
        } catch (Exception ex) {
            logger.error("Error publishing message to network", ex);
        }
    }

    /**
     * Retrieves a redis connection.
     *
     * @return Jedis.
     */
    public Jedis getBackend() {
        return this.jedisPool.getResource();
    }

    /**
     * Shuts down the message broker and all underlying systems.
     */
    public void shutdown() {
        this.terminating.set(true);
        this.service.shutdownNow();
        this.eventManager.close();
        this.jedisPool.close();
    }

}