package net.notfab.sentinel.gateway.jda.listeners;

import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.notfab.sentinel.gateway.BotPrefix;
import net.notfab.sentinel.gateway.SentinelGateway;
import net.notfab.sentinel.gateway.jda.mapper.JDAtoSentinel;
import net.notfab.sentinel.sdk.Channels;
import net.notfab.sentinel.sdk.ExchangeType;
import net.notfab.sentinel.sdk.MessageBroker;
import net.notfab.sentinel.sdk.entities.events.CommandEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandListener extends ListenerAdapter {

    private final Pattern argPattern = Pattern.compile("(?:([^\\s\"]+)|\"((?:\\w+|\\\\\"|[^\"])+)\")");
    private final MessageBroker broker;
    private final SentinelGateway gateway;

    public CommandListener(MessageBroker broker, SentinelGateway gateway) {
        this.broker = broker;
        this.gateway = gateway;
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
        SelfUser selfUser = event.getJDA().getSelfUser();
        String split = rawMessage.split("\\s+")[0];
        if (split.equals("<@" + selfUser.getId() + ">")) {
            prefix = "@" + selfUser.getName();
        } else if (split.equals("<@!" + selfUser.getId() + ">")) {
            prefix = "@" + event.getGuild().getSelfMember().getNickname();
        } else {
            prefix = this.gateway.getPrefixList(event.getGuild().getIdLong()).parallelStream()
                    .filter(x -> x.getRawContent().equalsIgnoreCase(split))
                    .map(BotPrefix::getDisplayContent)
                    .findAny()
                    .orElse(null);
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
        commandEvent.setMember(JDAtoSentinel.map(Objects.requireNonNull(event.getMember())));
        commandEvent.setChannel(JDAtoSentinel.map(event.getChannel()));
        broker.publish(commandEvent, ExchangeType.QUEUE, Channels.COMMAND_PREFIX + commandName.toLowerCase());
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