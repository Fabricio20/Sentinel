package net.notfab.sentinel.sdk.core.rabbit;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import net.notfab.sentinel.sdk.MessageBroker;
import net.notfab.sentinel.sdk.core.SentinelListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class RabbitSentinelListener<T> implements SentinelListener<T>, Consumer {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(RabbitSentinelListener.class);

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private final Class<T> tClass;

    public RabbitSentinelListener(Class<T> tClass) {
        this.tClass = tClass;
    }

    @Override
    public abstract boolean onMessage(String channel, T message);

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
        try {
            T object = objectMapper.readValue(body, tClass);
            if (this.isAutoAck()) {
                this.onMessage(envelope.getExchange(), object);
            } else {
                MessageBroker.getInstance()
                        .ack(envelope.getDeliveryTag(), this.onMessage(envelope.getExchange(), object));
            }
        } catch (IOException ex) {
            logger.error("Error during deserialization", ex);
        }
    }

    @Override
    public void handleConsumeOk(String consumerTag) {
        // Nothing
    }

    @Override
    public void handleCancelOk(String consumerTag) {
        // Nothing
    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {
        // Nothing
    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
        // Nothing
    }

    @Override
    public void handleRecoverOk(String consumerTag) {
        // Nothing
    }

}