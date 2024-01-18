package ac.emu.utils.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BukkitUtil {

    public static Player getPlayer(int id) {
        return Bukkit.getOnlinePlayers().stream().filter(e -> e.getEntityId() == id).findFirst().orElse(null);
    }

}
