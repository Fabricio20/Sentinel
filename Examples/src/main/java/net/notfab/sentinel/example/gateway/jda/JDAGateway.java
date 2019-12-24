package net.notfab.sentinel.example.gateway.jda;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.notfab.sentinel.example.gateway.jda.rpc.MembersRPC;
import net.notfab.sentinel.sdk.DiscordChannels;
import net.notfab.sentinel.sdk.Environment;
import net.notfab.sentinel.sdk.ExchangeType;
import net.notfab.sentinel.sdk.MessageBroker;
import net.notfab.sentinel.sdk.discord.command.CommandFramework;
import net.notfab.sentinel.sdk.rpc.RPCManager;

import javax.annotation.Nonnull;

public class JDAGateway extends ListenerAdapter {

    private final MessageBroker messageBroker;
    private final RPCManager rpcManager;
    private final CommandFramework commandFramework;

    private ShardManager shardManager;

    public JDAGateway() {
        this.messageBroker = new MessageBroker();
        this.rpcManager = new RPCManager(this.messageBroker);
        this.commandFramework = new CommandFramework(this.messageBroker);
        try {
            this.shardManager = new DefaultShardManagerBuilder()
                    .setToken(Environment.get("TOKEN", null))
                    .addEventListeners(this)
                    .setActivity(Activity.playing("Sentinel"))
                    .build();
            this.addSentinelHooks();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Adds all sentinel event listeners.
     */
    private void addSentinelHooks() {
        // Sentinel
        this.messageBroker.addListener(new MessengerListener(this.shardManager),
                ExchangeType.DIRECT, DiscordChannels.MESSENGER);
        // Sentinel RPC
        this.rpcManager.addFunction(new MembersRPC(this.shardManager));
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        event.getJDA().addEventListener(new CommandListener(this.commandFramework));
    }

}