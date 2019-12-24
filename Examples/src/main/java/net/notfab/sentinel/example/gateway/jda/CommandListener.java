package net.notfab.sentinel.example.gateway.jda;

import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.notfab.sentinel.example.gateway.jda.mapper.JDAtoSentinel;
import net.notfab.sentinel.sdk.discord.command.CommandFramework;
import net.notfab.sentinel.sdk.discord.events.CommandEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a basic JDA listener that listens for commands.
 * <p>
 * It's important to note that we should not be firing Command Events for Unknown commands, as they are
 * handled in a QUEUE manner.
 */
public class CommandListener extends ListenerAdapter {

    private final Pattern argPattern = Pattern.compile("(?:([^\\s\"]+)|\"((?:\\w+|\\\\\"|[^\"])+)\")");
    private final CommandFramework commandFramework;

    public CommandListener(CommandFramework commandFramework) {
        this.commandFramework = commandFramework;
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!"213044545825406976".equals(event.getGuild().getId())) {
            return;
        }
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
        } else if (split.equalsIgnoreCase("!")) {
            prefix = "!";
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
        this.commandFramework.fire(commandEvent);
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