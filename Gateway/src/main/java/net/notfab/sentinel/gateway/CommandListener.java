package net.notfab.sentinel.gateway;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.notfab.sentinel.gateway.mapper.JDAMapper;
import net.notfab.sentinel.sdk.Channels;
import net.notfab.sentinel.sdk.MessageBroker;
import net.notfab.sentinel.sdk.entities.events.CommandEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandListener extends ListenerAdapter {

    private final Pattern argPattern = Pattern.compile("(?:([^\\s\"]+)|\"((?:\\w+|\\\\\"|[^\"])+)\")");
    private final MessageBroker broker;

    public CommandListener(MessageBroker broker) {
        this.broker = broker;
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String rawMessage = event.getMessage().getContentRaw();
        if (rawMessage.split("\\s+").length == 0) {
            return;
        }
        String prefix = null;
        // -- Find Prefix
        if (rawMessage.split("\\s+")[0].equals("<@147409622603399168>")) {
            prefix = "@LewdBeta";
        } else if (rawMessage.split("\\s+")[0].equals("<@!147409622603399168>")) {
            prefix = "@" + event.getGuild().getSelfMember().getNickname();
        } else if (rawMessage.split("\\s+")[0].toLowerCase().startsWith("l!")) {
            prefix = rawMessage.split("!")[0] + "!"; // l!
        }
        // -- End Prefix Finder
        if (prefix == null) {
            return;
        }
        // -- Argument Finder
        List<String> arguments = this.getArguments(event.getMessage().getContentDisplay()
                .replaceFirst(Pattern.quote(prefix), ""));
        String commandName = arguments.get(0);
        if (arguments.size() == 1) {
            arguments.clear();
        } else {
            arguments.remove(0);
        }
        // -------------------------------------------------
        CommandEvent commandEvent = new CommandEvent();
        commandEvent.setArgs(arguments.toArray(new String[0]));
        commandEvent.setName(commandName);
        commandEvent.setMember(JDAMapper.map(Objects.requireNonNull(event.getMember())));
        commandEvent.setChannel(JDAMapper.map(event.getChannel()));
        broker.sendMessage(commandEvent, Channels.COMMAND_PREFIX + commandName.toUpperCase());
    }

    private List<String> getPrefixes(Guild guild) {
        return Arrays.asList("L!", "!", "<@147409622603399168>", "<@!147409622603399168>");
    }

    private List<String> getArguments(String rawArgs) {
        List<String> args = new ArrayList<>();
        Matcher m = argPattern.matcher(rawArgs);
        while (m.find()) {
            if (m.group(1) == null) {
                args.add(m.group(2));
            } else {
                args.add(m.group(1));
            }
        }
        return args;
    }

}