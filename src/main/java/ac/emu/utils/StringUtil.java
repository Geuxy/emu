package ac.emu.utils;

import ac.emu.check.Check;
import ac.emu.config.ConfigValues;

import org.bukkit.entity.Player;

public class StringUtil {

    public static String format(String message) {
        return message.replace("&", "§");
    }

    public static String replace(Check check, Player player, String message) {
        message = message
            .replace("{prefix}", ConfigValues.MESSAGE_PREFIX.stringValue())
            .replace("{player}", player.getName());

        if(check != null) {
            message = message
            .replace("{check}", check.getInfo().name())
            .replace("{level}", String.valueOf(check.getLevel()))
            .replace("{max}", String.valueOf(check.getMaximumLevel()))
            .replace("{info}", check.getInfo().description())
            .replace("{type}", check.getInfo().type());
        }

        return format(message);
    }

}
