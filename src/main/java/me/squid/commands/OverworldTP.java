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

        GhostsFile ghostsFile = Ghostcore.ghostsFile;

        if(ghostsFile.playerIsGhost(player)) {
            // Teleporting the player to the holding area
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mvtp " + player.getName() + " world");
        }

        return true;
    }
}
