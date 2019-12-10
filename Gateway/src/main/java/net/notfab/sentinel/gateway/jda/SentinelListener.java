package net.notfab.sentinel.gateway.jda;

import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SentinelListener extends ListenerAdapter {

    private Map<String, List<ListenerAdapter>> eventListMap = new HashMap<>();

    void addListener(Class<? extends Event> clazz, ListenerAdapter adapter) {
        this.eventListMap.compute(clazz.getSimpleName(), (k, v) -> {
            if (v == null) {
                v = new ArrayList<>();
            }
            v.add(adapter);
            return v;
        });
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        eventListMap.getOrDefault(GuildMessageReceivedEvent.class.getSimpleName(), new ArrayList<>())
                .forEach(x -> x.onGuildMessageReceived(event));
    }

}