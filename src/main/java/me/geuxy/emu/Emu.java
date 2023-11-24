package me.geuxy.emu;

import com.comphenix.protocol.ProtocolLibrary;

import lombok.Getter;

import me.geuxy.emu.check.manager.CheckManager;
import me.geuxy.emu.data.manager.DataManager;
import me.geuxy.emu.listeners.NetworkListener;
import me.geuxy.emu.listeners.PlayerListener;
import me.geuxy.emu.processors.PacketProcessor;

import org.bukkit.Bukkit;
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

        ProtocolLibrary.getProtocolManager().addPacketListener(new NetworkListener(this.plugin));
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this.plugin);
    }

    public void save() {

    }

    public void stop() {

    }

}
