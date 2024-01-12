package ac.emu.utils;

import ac.emu.check.Check;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class StaffUtil {

    public static void sendAlert(String message, Check check, Player player) {
        Bukkit.getOnlinePlayers()/*.stream().filter(p -> p.hasPermission(new Permission("emu.alert")) || p.isOp())*/.forEach(p -> p.sendMessage(StringUtil.replace(check, player, message)));
    }

}
