package me.squid;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.squid.commands.ResetPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
    public static void invisPlayer(Player player) {
        PotionEffect invis = new PotionEffect(PotionEffectType.INVISIBILITY, PotionEffect.INFINITE_DURATION, 255, true, false);

        player.addPotionEffect(invis);
    }

    public static void joinTeam(Player player, String teamName) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join " + teamName + " " + player.getName());
    }

    public static void leaveTeam(Player player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team leave " + player.getName());
    }

    public static void setGhost(OfflinePlayer player, boolean setGhost, boolean teleport, boolean setConfig) {
        String UUID = player.getUniqueId().toString();

        Plugin plugin = Bukkit.getPluginManager().getPlugin(Ghostcore.id);
        assert plugin != null;

        // Setting value in JSON file
        if(setConfig) {
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
        }

        if(player.isOnline()) {
            Player onlinePlayer = (Player) player;

            // Teleporting player
            if(teleport && setGhost) {
                String warpName = plugin.getConfig().getString("ghost_warp");

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "warp " + warpName + " " + player.getName());
            }

            // Hiding Player
            if(setGhost) {
                Ghostcore.joinTeam(onlinePlayer, plugin.getConfig().getString("ghosts_team"));

                invisPlayer(onlinePlayer);

                onlinePlayer.setAllowFlight(true); // Not working
            } else {
                onlinePlayer.removePotionEffect(PotionEffectType.INVISIBILITY);

                Ghostcore.leaveTeam(onlinePlayer);

                onlinePlayer.setAllowFlight(false); // Might be working
            }
        }
    }
}
