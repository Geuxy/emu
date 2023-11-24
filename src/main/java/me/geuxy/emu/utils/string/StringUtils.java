package me.geuxy.emu.utils.string;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.config.ConfigValues;

import org.bukkit.entity.Player;

public class StringUtils {

    public static String format(String message) {
        return message.replace("&", "§");
    }

    public static String replace(AbstractCheck check, Player player, String message) {
        return format(message
            .replace("{prefix}", ConfigValues.MESSAGE_PREFIX.stringValue())
            .replace("{player}", player.getName())
            .replace("{check}", check.getName())
            .replace("{level}", String.valueOf(check.getLevel()))
            .replace("{max}", String.valueOf(check.getMaxLevel()))
            .replace("{info}", check.getDescription())
            .replace("{type}", check.getType())
        );
    }

}
