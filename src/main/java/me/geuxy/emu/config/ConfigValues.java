package me.geuxy.emu.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

import me.geuxy.emu.Emu;
import me.geuxy.emu.check.AbstractCheck;

@Getter @AllArgsConstructor
public enum ConfigValues {

    MESSAGE_PREFIX("message.prefix"),
    MESSAGE_FAIL("message.fail");

    private final String prefix;

    public String stringValue() {
        return Emu.INSTANCE.getPlugin().getConfig().getString(prefix);
    }

    public int intValue() {
        return Emu.INSTANCE.getPlugin().getConfig().getInt(prefix);
    }

    public boolean booleanValue() {
        return Emu.INSTANCE.getPlugin().getConfig().getBoolean(prefix);
    }

    public enum Checks {
        ;

        public static int getMaxLevel(AbstractCheck check) {
            String prefix = "checks." + check.getName().replace(" ", "") + "." + check.getType() + "." + "max-level";
            return Emu.INSTANCE.getPlugin().getConfig().getInt(prefix.toLowerCase());
        }

        public static int getMinBuffer(AbstractCheck check) {
            String prefix = "checks." + check.getName().replace(" ", "") + "." + check.getType() + "." + "min-buffer";
            return Emu.INSTANCE.getPlugin().getConfig().getInt(prefix.toLowerCase());
        }

        public static boolean isEnabled(AbstractCheck check) {
            String prefix = "checks." + check.getName().replace(" ", "") + "." + check.getType() + "." + "enabled";
            return Emu.INSTANCE.getPlugin().getConfig().getBoolean(prefix.toLowerCase());
        }

        public static boolean isPunishable(AbstractCheck check) {
            String prefix = "checks." + check.getName().replace(" ", "") + "." + check.getType() + "." + "punishable";
            return Emu.INSTANCE.getPlugin().getConfig().getBoolean(prefix.toLowerCase());
        }
    }

}
