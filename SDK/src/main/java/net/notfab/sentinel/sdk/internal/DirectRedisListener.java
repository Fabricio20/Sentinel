package net.notfab.sentinel.sdk.internal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.notfab.eventti.Event;
import net.notfab.eventti.EventManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

public class DirectRedisListener extends JedisPubSub {

    private static final Logger logger = LoggerFactory.getLogger(DirectRedisListener.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final EventManager eventManager;

    public DirectRedisListener(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @Override
    public void onMessage(String channel, String message) {
        try {
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
