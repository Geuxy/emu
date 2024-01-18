package ac.emu.data;

import lombok.Getter;

import ac.emu.data.profile.EmuPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class DataManager {

    private final HashMap<UUID, EmuPlayer> dataMap = new HashMap<>();

    public void onInit() {
        Bukkit.getOnlinePlayers().forEach(this::add);
    }

    public void add(Player player) {
        dataMap.put(player.getUniqueId(), new EmuPlayer(player));
    }

    public void remove(Player player) {
        dataMap.remove(player.getUniqueId());
    }

    public EmuPlayer get(Player player) {
        if(player != null) {
            return dataMap.get(player.getUniqueId());
        }
        return null;
    }

    public EmuPlayer get(UUID uuid) {
        return dataMap.get(uuid);
    }

}
