package ac.emu.plugin;

import com.github.retrooper.packetevents.PacketEvents;

import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;

import ac.emu.Emu;

import org.bukkit.plugin.java.JavaPlugin;

public class EmuPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Emu.INSTANCE.init(this);
        this.saveDefaultConfig();
    }

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onDisable() {
        Emu.INSTANCE.stop();
        PacketEvents.getAPI().terminate();
    }

}
