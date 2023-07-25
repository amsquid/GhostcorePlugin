package me.squid;

import me.squid.commands.ResetPlayer;
import org.bukkit.plugin.java.JavaPlugin;

public final class Ghostcore extends JavaPlugin {

    public static String id = "Ghostcore";

    @Override
    public void onEnable() {
        // Registering Commands
        getCommand("resetplayer").setExecutor(new ResetPlayer());

        // Registering Events
        getServer().getPluginManager().registerEvents(new Events(), this);

        // Config
        this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
