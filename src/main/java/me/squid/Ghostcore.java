package me.squid;

import me.squid.commands.ResetPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class Ghostcore extends JavaPlugin {

    public static String id = "Ghostcore";

    public static GhostsFile ghostsFile;

    @Override
    public void onEnable() {
        // Setting Variables
        try {
            ghostsFile = new GhostsFile(getDataFolder().getAbsolutePath() + "/ghosts.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

    public void makeGhost(Player player) {
    }
}
