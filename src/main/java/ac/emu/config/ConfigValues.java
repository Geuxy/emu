package ac.emu.config;

import ac.emu.Emu;
import ac.emu.check.Check;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum ConfigValues {

    MESSAGE_PREFIX("message.prefix"),
    MESSAGE_FAIL("message.fail"),
    MESSAGE_GBP("message.gbp"),
    PUNISHMENTS_KICK("punishments.kick"),
    PUNISHMENTS_BAN("punishments.ban");

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

    public static double getFlagBuffer(Check check) {
        return Emu.INSTANCE.getPlugin().getConfig().getInt(getPrefix(check) + ".buffer.flag");
    }

    public static double getBufferDecay(Check check) {
        return Emu.INSTANCE.getPlugin().getConfig().getInt(getPrefix(check) + ".buffer.decay");
    }

    public static String getDisplay(Check check) {
        return Emu.INSTANCE.getPlugin().getConfig().getString(getPrefix(check) + ".display");
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
