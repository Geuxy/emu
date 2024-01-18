package ac.emu.data.impl;

import ac.emu.Emu;
import ac.emu.data.Data;
import ac.emu.data.profile.EmuPlayer;
import ac.emu.packet.Packet;

import lombok.Getter;

import org.bukkit.Bukkit;

@Getter
public class SetbackData extends Data {

    private int ticksSinceSetback;

    public SetbackData(EmuPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(packet.isMovement()) {
            this.ticksSinceSetback++;
        }
    }

    public void setback() {
        if(ticksSinceSetback > 5) {
            Bukkit.getScheduler().runTask(Emu.INSTANCE.getPlugin(), () -> data.getPlayer().teleport(data.getMovementData().getLastLocationOnGround()));
            this.ticksSinceSetback = 0;
        }
    }

}
