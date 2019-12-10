package net.notfab.sentinel.sdk.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.notfab.eventti.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventWrapper {

    private static Logger logger = LoggerFactory.getLogger(EventWrapper.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    private String className;
    private Event event;

    public String toString() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            logger.error("Error serializing Event", ex);
            return null;
        }
    }

}