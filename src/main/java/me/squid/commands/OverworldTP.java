package me.squid.commands;

import com.google.gson.JsonObject;
import me.squid.Ghostcore;
import me.squid.GhostsFile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class OverworldTP implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        String UUID = String.valueOf(player.getUniqueId());

        GhostsFile ghostsFile = Ghostcore.ghostsFile;
        JsonObject playerData;

        try {
            playerData = ghostsFile.getPlayer(UUID);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(playerData.get("isGhost").getAsBoolean()) {
            // Teleporting the player to the holding area
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mvtp " + player.getName() + " world");
        }

        return true;
    }
}
