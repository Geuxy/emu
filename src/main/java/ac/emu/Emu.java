package ac.emu;

import ac.emu.check.manager.CheckManager;
import ac.emu.command.manager.CommandManager;
import ac.emu.data.manager.DataManager;
import ac.emu.listeners.BukkitListener;
import ac.emu.listeners.NetworkListener;
import com.github.retrooper.packetevents.PacketEvents;

import lombok.Getter;

import lombok.Setter;
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

    @Setter
    private boolean debug;

    private final NetworkListener networkListener;

    Emu() {
        this.logger = Bukkit.getLogger();
        this.checkManager = new CheckManager();
        this.dataManager = new DataManager();
        this.networkListener = new NetworkListener();
        this.debug = false;
    }

    public void init(JavaPlugin plugin) {
        if(this.plugin != null) {
            logger.info("Error encounter when starting Emu: Already initialized!");
            return;
        }

        PacketEvents.getAPI().init();

        this.plugin = plugin;

        checkManager.onInit();
        dataManager.onInit();

        plugin.getCommand("emu").setExecutor(new CommandManager());

        PacketEvents.getAPI().getEventManager().registerListener(networkListener);
        Bukkit.getPluginManager().registerEvents(new BukkitListener(), this.plugin);
    }

    public void stop() {
        PacketEvents.getAPI().getEventManager().unregisterListener(networkListener);
        HandlerList.unregisterAll(this.plugin);
        this.dataManager.getDataMap().clear();
    }

}
