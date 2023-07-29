package me.squid;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;

import java.io.IOException;

public class Events implements Listener {

    Plugin plugin;

    public Events() {
        this.plugin = Bukkit.getPluginManager().getPlugin(Ghostcore.id);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        // Variables
        Player player = e.getPlayer();
        String UUID = player.getUniqueId().toString();

        // Resetting player on join
        /*if(!player.hasPlayedBefore()) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "resetplayer " + e.getPlayer());
        }*/

        // Getting JSON data
        GhostsFile ghostsFile = Ghostcore.ghostsFile;
        JsonObject ghostsFileData = ghostsFile.getJson();
        JsonObject playerObject = null;

        try {
            playerObject = ghostsFile.getPlayer(UUID);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        assert playerObject != null;

        if(!playerObject.has("isGhost")) {
            playerObject.addProperty("isGhost", false);

            plugin.getLogger().info("Adding isGhost property to player in " + Ghostcore.ghostsFile.getName());

            try {
                ghostsFileData.add(UUID, playerObject);

                Ghostcore.ghostsFile.overwriteFile(ghostsFileData.toString());

                plugin.getLogger().info(ghostsFileData.toString());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            JsonElement isGhost = playerObject.get("isGhost");

            if(isGhost.getAsBoolean()) {
                player.sendMessage("You are a ghost");
            } else {
                player.sendMessage("You are not a ghost");
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();

        if(player.getGameMode() != GameMode.CREATIVE) {
            Ghostcore.setGhost(player, true, true, true);
        }
    }

}
