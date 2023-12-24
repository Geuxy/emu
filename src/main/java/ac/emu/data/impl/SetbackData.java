package ac.emu.data.impl;

import ac.emu.Emu;
import ac.emu.data.Data;
import ac.emu.data.PlayerData;
import ac.emu.packet.Packet;

import lombok.Getter;

import org.bukkit.Bukkit;

@Getter
public class SetbackData extends Data {

    private int ticksSinceSetback;

    private long lastSetback;

    public SetbackData(PlayerData data) {
        super(data);
        this.lastSetback = System.currentTimeMillis();
    }

    @Override
    public void handle(Packet packet) {
        if(packet.isMovement()) {
            this.ticksSinceSetback++;
        }
    }

    public void setback() {
        if(ticksSinceSetback > 5) {
            Bukkit.getScheduler().runTask(Emu.INSTANCE.getPlugin(), () -> data.getPlayer().teleport(data.getMovementData().getLastLocationOnGround().clone()));
            this.ticksSinceSetback = 0;
            this.lastSetback = System.currentTimeMillis();
        }
    }

}
