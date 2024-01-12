package ac.emu.tick;

import ac.emu.Emu;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

@Getter
public class TickManager implements Runnable {

    private BukkitTask task;

    private int ticks;

    public void onInit() {
        if(task == null) {
            this.task = Bukkit.getScheduler().runTaskTimer(Emu.INSTANCE.getPlugin(), this, 0, 1);
        }
    }

    public void onStop() {
        if(task != null) {
            task.cancel();
            task = null;
        }
    }

    @Override
    public void run() {
        ticks++;
    }

}
