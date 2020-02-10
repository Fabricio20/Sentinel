package net.notfab.sentinel.sdk.discord.command;

import net.notfab.eventti.EventHandler;
import net.notfab.eventti.Listener;
import net.notfab.sentinel.sdk.ExchangeType;
import net.notfab.sentinel.sdk.MessageBroker;
import net.notfab.sentinel.sdk.discord.events.CommandEvent;
import net.notfab.sentinel.sdk.discord.events.CommandRegisterEvent;
import net.notfab.sentinel.sdk.discord.events.CommandUnregisterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

public class CommandFramework implements Listener {

    private static final Logger logger = LoggerFactory.getLogger(CommandFramework.class);
    private Map<String, SentinelCommand> commandMap = new HashMap<>();
    private MessageBroker broker;

    private static final String CMD_PREFIX = "Sentinel:Commands:";
    private static final String REGISTRY = "Sentinel:Registry:Commands";
    private static final String EVENT_HUB = "Sentinel:Hub:Commands";

    public CommandFramework(MessageBroker broker) {
        this.broker = broker;
    }

    /**
     * Registers a command on the network.
     *
     * @param command - Command to add.
     * @apiNote Usually used by workers.
     */
    public void register(SentinelCommand command) {
        this.broker.publish(new CommandRegisterEvent(command.getNames()), ExchangeType.DIRECT, EVENT_HUB);
        try (Jedis jedis = this.broker.getBackend()) {
            command.getNames().forEach(name -> {
                name = name.toLowerCase();
                this.commandMap.put(name, command);
                this.broker.addListener(this, ExchangeType.QUEUE, CMD_PREFIX + name);
                jedis.sadd(REGISTRY, name);
            });
        }
    }

    /**
     * Unregisters/Removes a command from the network.
     *
     * @param command - Command to remove.
     */
    public void unregister(SentinelCommand command) {
        this.broker.publish(new CommandUnregisterEvent(command.getNames()), ExchangeType.DIRECT, EVENT_HUB);
        try (Jedis jedis = this.broker.getBackend()) {
            command.getNames().forEach(name -> {
                name = name.toLowerCase();
                this.commandMap.remove(name);
                // TODO Unregister from the network itself (MessageBroker).
                jedis.srem(REGISTRY, name);
            });
        }
    }

    /**
     * Sends a command to the network.
     *
     * @param event - Command event.
     * @apiNote Usually used by gateways.
     */
    public void fire(CommandEvent event) {
        try (Jedis jedis = this.broker.getBackend()) {
            if (!jedis.sismember(REGISTRY, event.getName().toLowerCase())) {
                logger.debug("Attempted to fire unknown command " + event.getName());
                return;
            }
            this.broker.publish(event, ExchangeType.QUEUE, CMD_PREFIX + event.getName());
        }
    }

    /**
     * Receives a command from the network and calls the proper handler for it.
     *
     * @param event - Command Event.
     */
    @EventHandler
    public void onNetworkCommand(CommandEvent event) {
        try {
            SentinelCommand command = this.commandMap.get(event.getName());
            if (command != null) {
                command.onCommand(event.getMember(), event.getChannel(), event.getArgs());
            }
        } catch (Exception ex) {
            logger.error("Error processing command " + event.getName(), ex);
        }
    }

    /**
     * Shuts down this Command Manager and all underlying systems.
     */
    public void shutdown() {
        this.commandMap.clear();
    }

}