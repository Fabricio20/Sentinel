package net.notfab.sentinel.sdk.internal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.notfab.eventti.Event;
import net.notfab.eventti.EventManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.List;

public class QueueRedisListener implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(QueueRedisListener.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final Jedis jedis;
    private final String channel;
    private final EventManager eventManager;

    public QueueRedisListener(Jedis jedis, String channel, EventManager eventManager) {
        this.jedis = jedis;
        this.channel = channel;
        this.eventManager = eventManager;
    }

    @Override
    public void run() {
        try {
            List<String> item = jedis.brpop(0, channel);
            if (item == null || item.size() < 2) {
                return;
            }
            String message = item.get(1);
            JsonNode node = objectMapper.readTree(message);
            Class<?> clazz = Class.forName(node.get("className").asText());
            if (clazz != null) {
                JsonNode eventNode = node.get("event");
                Event event = (Event) objectMapper.treeToValue(eventNode, clazz);
                this.eventManager.fire(event);
            } else {
                logger.warn("Unknown event class " + node.get("className").asText());
            }
        } catch (ClassNotFoundException ignored) {
            logger.trace("Event not fired due to class not found.");
        } catch (Exception ex) {
            logger.error("Error while deserializing Event", ex);
        }
    }

}
