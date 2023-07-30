package me.squid;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.Objects;

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
        if(!player.hasPlayedBefore()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "resetplayer " + player.getName());
                }
            }, 1);
        }

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
            // Running on a tick delay to give the server a chance to realize what's happening
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    Ghostcore.setGhost(player, true, true, true);

                    player.sendMessage("You are now a ghost.");
                }
            }, 1);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();

        String UUID = player.getUniqueId().toString();

        ItemStack holding = e.getItem();
        String itemName = "";

        JsonObject playerData;

        try {
            playerData = Ghostcore.ghostsFile.getPlayer(UUID);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        if(holding != null) {
            itemName = Objects.requireNonNull(holding.getItemMeta()).getDisplayName();
        }

        // Right click event
        if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if(holding != null) {
                // Stuff
            }
        }
    }

}
