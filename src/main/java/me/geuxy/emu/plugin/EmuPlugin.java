package me.geuxy.emu.plugin;

import me.geuxy.emu.Emu;
import org.bukkit.plugin.java.JavaPlugin;

public class EmuPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Emu.INSTANCE.init(this);
        this.saveDefaultConfig();
    }

    /*@Override
    public void saveDefaultConfig() {
        super.saveDefaultConfig();
    }*/

    @Override
    public void onDisable() {
        Emu.INSTANCE.stop();
    }

}
