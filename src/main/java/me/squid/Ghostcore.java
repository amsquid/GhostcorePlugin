package me.squid;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.squid.commands.ResetPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
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

    // Utility Methods
    public static void totallyHidePlayer(Player player) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(Ghostcore.id);
        assert plugin != null;

        for(Player otherPlayer : Bukkit.getOnlinePlayers()) {
            otherPlayer.hidePlayer(plugin, player);
        }
    }

    public static void totallyShowPlayer(Player player) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(Ghostcore.id);
        assert plugin != null;

        for(Player otherPlayer : Bukkit.getOnlinePlayers()) {
            otherPlayer.showPlayer(plugin, player);
        }
    }

    public static void setGhost(OfflinePlayer player, boolean setGhost, boolean teleport, boolean setConfig) {
        String UUID = player.getUniqueId().toString();

        Plugin plugin = Bukkit.getPluginManager().getPlugin(Ghostcore.id);
        assert plugin != null;

        // Setting value in JSON file
        JsonObject ghostsFileData = ghostsFile.getJson();
        JsonObject playerObject = null;

        try {
            playerObject = ghostsFile.getPlayer(UUID);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        playerObject.addProperty("isGhost", setGhost);
        ghostsFileData.add(UUID, playerObject);

        try {
            ghostsFile.overwriteFile(ghostsFileData.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(player.isOnline()) {
            Player onlinePlayer = (Player) player;

            // Teleporting player
            if(teleport && setGhost) {
                String warpName = plugin.getConfig().getString("ghost_warp");

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "warp " + warpName + " " + player.getName());
            }

            // Showing/Hiding Player
            if(setGhost) {
                totallyHidePlayer(onlinePlayer);
            } else {
                totallyShowPlayer(onlinePlayer);
            }
        }
    }
}
