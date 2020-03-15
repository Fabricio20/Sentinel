package net.notfab.sentinel.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import net.notfab.eventti.Event;

import java.io.IOException;

public class MQTTEvent extends Event {

    private static final ObjectMapper objectMapper = MessageBroker.objectMapper;

    @Setter
    private Object response;

    @Getter
    private final String message;

    @Getter
    private final String endpoint;

    MQTTEvent(String message, String type) {
        this.message = message;
        this.endpoint = type;
    }

    /**
     * Attempts to parse and convert the payload/message.
     *
     * @param clazz - Class of Desired T.
     * @param <T>   - Desired T.
     * @return Converted Payload or Null.
     */
    @SuppressWarnings("unchecked")
    public <T> T getAs(Class<T> clazz) {
        try {
            if (clazz == String.class) {
                return (T) this.message;
            } else if (clazz == Boolean.class) {
                return (T) Boolean.valueOf(this.message);
            } else if (clazz == Long.class) {
                return (T) Long.valueOf(this.message);
            } else if (clazz == Integer.class) {
                return (T) Integer.valueOf(this.message);
            } else if (clazz == Double.class) {
                return (T) Double.valueOf(this.message);
            } else {
                return objectMapper.readValue(this.getMessage(), clazz);
            }
        } catch (IOException ignored) {
            // Ignored
        }
        return null;
    }

    /**
     * @return The response of this event, encoded for sending via MQTT.
     */
    String getResponse() {
        if (this.response == null) {
            return null;
        }
        try {
            if (this.response instanceof String) {
                return (String) this.response;
            } else if (this.response instanceof Boolean
                    || this.response instanceof Long
                    || this.response instanceof Integer
                    || this.response instanceof Double) {
                return String.valueOf(response);
            } else {
                return objectMapper.writeValueAsString(this.response);
            }
        } catch (JsonProcessingException ex) {
            return null;
        }
    }

}