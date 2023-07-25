package me.squid.commands;

import me.squid.Ghostcore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ResetPlayer implements CommandExecutor {

    Plugin plugin;

    public ResetPlayer() {
        plugin = Bukkit.getPluginManager().getPlugin(Ghostcore.id);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if(args.length < 1) {
            return false;
        }

        Player playerToReset = Bukkit.getPlayer(args[0]);

        if(playerToReset == null) {
            return false;
        }

        // Clearing inventory
        playerToReset.getInventory().clear();

        // Teleporting the player to the holding area
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mvtp " + playerToReset.getName() + " world");

        Location location = new Location(player.getWorld(), 0.5, -63, 0.5);
        playerToReset.teleport(location);

        // Teleporting the player randomly
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "rtp player_sudo " + playerToReset.getName());

        return true;
    }
}
