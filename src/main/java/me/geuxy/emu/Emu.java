package me.geuxy.emu;

import io.github.retrooper.packetevents.PacketEvents;
import lombok.Getter;

import me.geuxy.emu.check.manager.CheckManager;
import me.geuxy.emu.data.manager.DataManager;
import me.geuxy.emu.listeners.NetworkListener;
import me.geuxy.emu.listeners.PlayerListener;
import me.geuxy.emu.processors.PacketProcessor;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

@Getter
public enum Emu {

    INSTANCE;

    private JavaPlugin plugin;

    private final Logger logger;
    private final DataManager dataManager;
    private final CheckManager checkManager;
    private final PacketProcessor packetProcessor;

    Emu() {
        this.logger = Bukkit.getLogger();
        this.checkManager = new CheckManager();
        this.dataManager = new DataManager();
        this.packetProcessor = new PacketProcessor();
    }

    public void init(JavaPlugin plugin) {
        if(this.plugin != null) {
            logger.info("Error encounter when starting Emu: Already initialized!");
            return;
        }

        this.plugin = plugin;

        checkManager.onInit();
        dataManager.onInit();

        PacketEvents.get().registerListener(new NetworkListener());
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this.plugin);
    }

    public void stop() {
        PacketEvents.get().unregisterAllListeners();
        HandlerList.unregisterAll(this.plugin);
        this.dataManager.getDataMap().clear();
    }

}
