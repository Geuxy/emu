package me.geuxy.emu.listeners;

import me.geuxy.emu.Emu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        Emu.INSTANCE.getDataManager().add(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(PlayerQuitEvent event) {
        Emu.INSTANCE.getDataManager().remove(event.getPlayer());
    }
    // The bukkit handlers below will temporarily stay until a better solution is found
    @EventHandler(priority = EventPriority.LOW)
    public void onTeleport(PlayerTeleportEvent event) {
        Emu.INSTANCE.getDataManager().get(event.getPlayer()).handleTeleport();
    }

}
