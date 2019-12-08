package net.notfab.sentinel.sdk.core.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import net.notfab.sentinel.sdk.Environment;
import net.notfab.sentinel.sdk.MessageBroker;
import net.notfab.sentinel.sdk.core.ExchangeType;
import net.notfab.sentinel.sdk.core.SentinelListener;
import net.notfab.sentinel.sdk.entities.SentinelMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class RabbitMessageBroker extends MessageBroker {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMessageBroker.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private Connection connection;
    private Channel channel;
    private final ConnectionFactory factory;

    public RabbitMessageBroker() {
        this.factory = new ConnectionFactory();
        factory.setUsername(Environment.get("RABBITMQ_USERNAME", "guest"));
        factory.setPassword(Environment.get("RABBITMQ_PASSWORD", "guest"));
        factory.setVirtualHost(Environment.get("RABBITMQ_VHOST", "/"));
        factory.setHost(Environment.get("RABBITMQ_HOST", "localhost"));
        factory.setPort(Environment.get("RABBITMQ_PORT", 5672));
    }

    @Override
    public void shutdown() {
        try {
            this.connection.close();
        } catch (IOException ex) {
            logger.error("Error while closing RabbitMQ connection", ex);
        }
    }

    @Override
    public void addListener(SentinelListener listener, String... channels) {
        if (!(listener instanceof RabbitSentinelListener)) {
            return;
        }
        for (String channel : channels) {
            this.addExchange(channel, listener.getExchangeType(), Map.of("x-message-ttl", 5000));
        }
    }

    @Override
    public void publish(SentinelMessage message, String... channels) {
        try {
            String encoded = objectMapper.writeValueAsString(message);
            for (String channel : channels) {
                this.sendMessage(channel, encoded);
            }
        } catch (IOException ex) {
            logger.error("Exception while publishing message to " + Arrays.toString(channels), ex);
        }
    }

    /**
     * Adds an entry point for the message system, usually considered a channel.
     *
     * @param name         - Name of the exchange.
     * @param exchangeType - Exchange type.
     * @param arguments    - Arguments for the queue, like the TTL of messages.
     */
    private void addExchange(String name, ExchangeType exchangeType, Map<String, Object> arguments) {
        try {
            this.getChannel().exchangeDeclare(name.toLowerCase(), exchangeType.name().toLowerCase(), true);
            this.getChannel().queueDeclare(name.toLowerCase(), true, false, false, arguments);
            this.getChannel().queueBind(name.toLowerCase(), name.toLowerCase(), name.toLowerCase());
        } catch (IOException ex) {
            logger.error("Error declaring exchange " + name, ex);
        }
    }

    /**
     * Adds an exit point for the message system, usually considered listener.
     *
     * @param queue    - Name of the queue to consume from.
     * @param autoAck  - If messages should be auto-ack-d.
     * @param consumer - Actual consumer.
     */
    private void addConsumer(String queue, boolean autoAck, Consumer consumer) {
        try {
            this.getChannel().basicConsume(queue, autoAck, consumer);
        } catch (IOException ex) {
            logger.error("Error declaring consumer " + queue, ex);
        }
    }

    /**
     * Sends a message with default priority.
     *
     * @param exchange - Name of the exchange to push to.
     * @param message  - Message.
     */
    private void sendMessage(String exchange, String message) {
        this.sendMessage(exchange, message, 0);
    }

    /**
     * Sends a message with high priority.
     *
     * @param exchange - Name of the exchange to push to.
     * @param message  - Message.
     * @param priority - Priority.
     */
    private void sendMessage(String exchange, String message, int priority) {
        try {
            getChannel().basicPublish(exchange.toLowerCase(), exchange.toLowerCase(), new AMQP.BasicProperties.Builder()
                    .contentType("application/json")
                    .priority(priority)
                    .deliveryMode(1).build(), message.getBytes());
        } catch (IOException ex) {
            logger.error("Error publishing message", ex);
        }
    }

    /**
     * Returns a RabbitMQ Channel.
     */
    private Channel getChannel() {
        if (this.channel != null && this.channel.isOpen()) {
            return this.channel;
        } else {
            try {
                this.channel = getConnection().createChannel();
            } catch (IOException ex) {
                logger.error("Error opening RabbitMQ Channel", ex);
            }
            return this.channel;
        }
    }

    /**
     * Returns a RabbitMQ Connection.
     */
    private Connection getConnection() {
        if (this.connection != null && this.connection.isOpen()) {
            return this.connection;
        } else {
            try {
                this.connection = this.factory.newConnection();
            } catch (IOException | TimeoutException ex) {
                logger.error("Error opening RabbitMQ Connection", ex);
            }
            return this.connection;
        }
    }

}