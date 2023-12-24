package ac.emu.listeners;

import ac.emu.Emu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class BukkitListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        Emu.INSTANCE.getDataManager().add(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(PlayerQuitEvent event) {
        Emu.INSTANCE.getDataManager().remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onTeleport(PlayerTeleportEvent event) {
        Emu.INSTANCE.getDataManager().get(event.getPlayer()).getActionData().handleTeleport();
    }

}
