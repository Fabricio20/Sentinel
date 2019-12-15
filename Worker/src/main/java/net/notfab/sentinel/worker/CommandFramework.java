package net.notfab.sentinel.worker;

import net.notfab.eventti.EventHandler;
import net.notfab.eventti.Listener;
import net.notfab.sentinel.sdk.Channels;
import net.notfab.sentinel.sdk.ExchangeType;
import net.notfab.sentinel.sdk.MessageBroker;
import net.notfab.sentinel.sdk.discord.events.CommandEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class CommandFramework implements Listener {

    private static final Logger logger = LoggerFactory.getLogger(CommandFramework.class);
    private Map<String, SentinelCommand> commandMap = new HashMap<>();
    private MessageBroker broker;

    CommandFramework(MessageBroker broker) {
        this.broker = broker;
    }

    /**
     * Adds a command to the framework.
     *
     * @param command - Command to add.
     */
    public void add(SentinelCommand command) {
        command.getNames().forEach(name -> {
            name = name.toLowerCase();
            this.commandMap.put(name, command);
            this.broker.addListener(this, ExchangeType.QUEUE, Channels.COMMAND_PREFIX + name);
        });
    }

    @EventHandler
    public void onCommand(CommandEvent event) {
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
    void shutdown() {
        this.commandMap.clear();
    }

}