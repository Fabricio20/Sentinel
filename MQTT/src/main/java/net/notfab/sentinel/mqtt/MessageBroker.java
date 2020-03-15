package net.notfab.sentinel.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import net.notfab.eventti.EventManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class MessageBroker {

    private static final Logger logger = LoggerFactory.getLogger(MessageBroker.class);
    private static final ExecutorService threadPool = Executors.newCachedThreadPool();
    private static MessageBroker Instance;
    static ObjectMapper objectMapper;

    private Connection connection;
    private final String service;
    private final EventManager eventManager;

    /**
     * Initializes the MessageBroker.
     *
     * @param service      - Name of this service.
     * @param eventManager - EventManager of the backing service.
     */
    public MessageBroker(String service, EventManager eventManager) {
        this(service, eventManager, new ObjectMapper());
    }

    /**
     * Initializes the MessageBroker.
     *
     * @param service      - Name of this service.
     * @param eventManager - EventManager of the backing service.
     * @param objectMapper - Optional customized ObjectMapper.
     */
    public MessageBroker(String service, EventManager eventManager, ObjectMapper objectMapper) {
        this.service = service;
        this.eventManager = eventManager;
        MessageBroker.objectMapper = objectMapper;
        this.addListener(service);
        Instance = this;
    }

    public static MessageBroker getInstance() {
        return Instance;
    }

    private Connection getConnection() {
        if (this.connection != null && connection.isOpen()) {
            return this.connection;
        } else {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(Environment.get("RABBIT_HOST", "localhost"));
            factory.setPort(Integer.parseInt(Environment.get("RABBIT_PORT", "5672")));
            factory.setPassword(Environment.get("RABBIT_PASSWORD", "guest"));
            factory.setUsername(Environment.get("RABBIT_USER", "guest"));
            try {
                this.connection = factory.newConnection();
            } catch (IOException | TimeoutException ex) {
                return null;
            }
        }
        return this.connection;
    }

    private Channel getChannel() throws IOException {
        return Objects.requireNonNull(this.getConnection()).createChannel();
    }

    private void createQueue(String service, Channel channel) throws IOException {
        channel.queueDeclare(service.toUpperCase(), false, false, false, null);
    }

    /* ------------------------------------------------------------------------------------ */

    /**
     * Sends a message to the network.
     *
     * @param service  - Target channel or service.
     * @param endpoint - Endpoint (Message Subtype).
     * @param message  - Actual Message.
     * @return Response or Empty.
     */
    public String send(String service, String endpoint, Object message) {
        try {
            if (message instanceof String) {
                return this.send(service, endpoint, "text/plain", (String) message);
            } else if (message instanceof Long
                    || message instanceof Boolean
                    || message instanceof Integer
                    || message instanceof Double) {
                return this.send(service, endpoint, "text/plain", String.valueOf(message));
            } else if (message instanceof Enum<?>) {
                return this.send(service, endpoint, "text/plain", ((Enum<?>) message).name());
            } else {
                return this.send(service, endpoint, "application/json", objectMapper.writeValueAsString(message));
            }
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * Sends a message to the network, asynchronously, ignoring the response.
     *
     * @param service  - Target channel or service.
     * @param endpoint - Endpoint (Message Subtype).
     * @param message  - Actual Message.
     */
    public void sendAsync(String service, String endpoint, Object message) {
        this.sendAsync(service, endpoint, message, null);
    }

    /**
     * Sends a message to the network, asynchronously.
     *
     * @param service  - Target channel or service.
     * @param endpoint - Endpoint (Message Subtype).
     * @param message  - Actual Message.
     * @param callback - Response callback.
     */
    public void sendAsync(String service, String endpoint, Object message, Consumer<String> callback) {
        threadPool.submit(() -> {
            String response = this.send(service, endpoint, message);
            if (callback != null) {
                callback.accept(response);
            }
        });
    }

    /**
     * Adds a listener to another channel.
     *
     * @param channelName - Name of the channel.
     */
    public void addListener(String channelName) {
        threadPool.submit(() -> this.listen(channelName));
    }

    /* ------------------------------------------------------------------------------------ */

    private String send(String service, String endpoint, String contentType, String message) {
        try (Channel channel = this.getChannel()) {
            this.createQueue(this.service, channel);
            final String corrId = UUID.randomUUID().toString();
            String replyQueueName = channel.queueDeclare().getQueue();
            AMQP.BasicProperties props = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(corrId)
                    .replyTo(replyQueueName)
                    .contentType(contentType)
                    .type(endpoint.toUpperCase())
                    .build();
            channel.basicPublish("", service.toUpperCase(), props, message.getBytes(StandardCharsets.UTF_8));
            final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);
            String tag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
                if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                    response.offer(new String(delivery.getBody(), StandardCharsets.UTF_8));
                }
            }, consumerTag -> {
            });
            String result = response.take();
            channel.basicCancel(tag);
            return result;
        } catch (IOException | TimeoutException | InterruptedException ex) {
            logger.error("Error while sending MQTT message", ex);
            return null;
        }
    }

    /**
     * Listens on the specified channel. This is a BLOCKING operation.
     *
     * @param channelName - Name of the channel.
     */
    private void listen(String channelName) {
        try (Channel channel = this.getChannel()) {
            this.createQueue(channelName, channel);
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(delivery.getProperties().getCorrelationId())
                        .build();
                String response = "";
                try {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    MQTTEvent event = new MQTTEvent(message, delivery.getProperties().getType().toUpperCase());
                    this.eventManager.fire(event);
                    if (event.getResponse() != null) {
                        response = event.getResponse();
                    }
                } catch (RuntimeException ex) {
                    logger.error("Error decoding MQTT message", ex);
                } finally {
                    channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps,
                            response.getBytes(StandardCharsets.UTF_8));
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            };
            channel.basicConsume(channelName.toUpperCase(), false, deliverCallback, consumerTag -> {
                // Ignored
            });
        } catch (TimeoutException | IOException ex) {
            logger.error("Error listening", ex);
        }
    }

}