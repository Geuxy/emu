package ac.emu.config;

import ac.emu.Emu;
import ac.emu.check.Check;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum ConfigValues {

    MESSAGE_PREFIX("message.prefix"),
    MESSAGE_FAIL("message.fail"),
    MESSAGE_GBP("message.gbp");

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

    public static int getMaxLevel(Check check) {
        return Emu.INSTANCE.getPlugin().getConfig().getInt(getPrefix(check) + "." + "max-level");
    }

    public static double getMinBuffer(Check check) {
        return Emu.INSTANCE.getPlugin().getConfig().getInt(getPrefix(check) + "." + "min-buffer");
    }

    public static boolean isEnabled(Check check) {
        return Emu.INSTANCE.getPlugin().getConfig().getBoolean(getPrefix(check) + "." + "enabled");
    }

    public static boolean isPunishable(Check check) {
        return Emu.INSTANCE.getPlugin().getConfig().getBoolean(getPrefix(check) + "." + "punishable");
    }

    private static String getPrefix(Check check) {
        return ("checks." + check.getInfo().name().replace(" ", "") + "." + check.getInfo().type()).toLowerCase();
    }

}
