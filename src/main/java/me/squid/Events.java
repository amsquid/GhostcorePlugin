package me.squid;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Events implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if(!player.hasPlayedBefore()) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "resetplayer " + e.getPlayer());
        }
    }

}
