package net.notfab.sentinel.sdk.discord.command;

import net.notfab.eventti.EventHandler;
import net.notfab.eventti.Listener;
import net.notfab.sentinel.sdk.DiscordChannels;
import net.notfab.sentinel.sdk.ExchangeType;
import net.notfab.sentinel.sdk.MessageBroker;
import net.notfab.sentinel.sdk.discord.events.CommandEvent;
import net.notfab.sentinel.sdk.discord.events.CommandRegisterEvent;
import net.notfab.sentinel.sdk.discord.events.CommandUnregisterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandFramework implements Listener {

    private static final Logger logger = LoggerFactory.getLogger(CommandFramework.class);
    private Map<String, SentinelCommand> commandMap = new HashMap<>();
    private List<String> registered = new ArrayList<>();
    private MessageBroker broker;

    public CommandFramework(MessageBroker broker) {
        this.broker = broker;
        this.broker.addListener(this, ExchangeType.DIRECT, DiscordChannels.COMMAND_REGISTRY);
    }

    /**
     * Adds a command to the framework.
     *
     * @param command - Command to add.
     * @apiNote Usually used by workers.
     */
    public void add(SentinelCommand command) {
        this.broker.publish(new CommandRegisterEvent(command.getNames()),
                ExchangeType.DIRECT, DiscordChannels.COMMAND_REGISTRY);
        command.getNames().forEach(name -> {
            name = name.toLowerCase();
            this.commandMap.put(name, command);
            this.broker.addListener(this, ExchangeType.QUEUE, DiscordChannels.COMMAND_PREFIX + name);
        });
    }

    /**
     * Sends a command to the network.
     *
     * @param event - Command event.
     * @apiNote Usually used by gateways.
     */
    public void fire(CommandEvent event) {
        if (this.registered.contains(event.getName().toLowerCase())) {
            this.broker.publish(event, ExchangeType.QUEUE,
                    DiscordChannels.COMMAND_PREFIX + event.getName());
        } else {
            logger.debug("Attempted to fire unknown command " + event.getName());
        }
    }

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

    @EventHandler
    public void onRegister(CommandRegisterEvent event) {
        this.registered.addAll(event.getNames().stream()
                .map(String::toLowerCase).collect(Collectors.toList()));
        logger.debug("Registered incoming command " + event.getNames().get(0) + " [" + event.getNames().toString() + "]");
    }

    @EventHandler
    public void onUnregister(CommandUnregisterEvent event) {
        this.registered.removeAll(event.getNames().stream()
                .map(String::toLowerCase).collect(Collectors.toList()));
        logger.debug("Unregistered command " + event.getNames().get(0) + " [" + event.getNames().toString() + "]");
    }

    /**
     * Shuts down this Command Manager and all underlying systems.
     */
    public void shutdown() {
        this.commandMap.clear();
    }

}