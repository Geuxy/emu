package me.geuxy.emu.check.impl.packet.badpackets;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;

@CheckInfo(
    name = "Bad Packets",
    description = "Switching slots too quickly",
    type = "C"
)
public class BadPacketsC extends AbstractCheck {

    private long heldItemSlot, lastHeldItemSlot;

    public BadPacketsC(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isHeldItemSlot()) {
            this.lastHeldItemSlot = heldItemSlot;
            this.heldItemSlot = System.currentTimeMillis();

            if(lastHeldItemSlot != 0) {
                long deltaHeldItemSlot = heldItemSlot - lastHeldItemSlot;

                boolean invalid = deltaHeldItemSlot < 15;

                if(invalid) {
                    this.fail("delta=" + deltaHeldItemSlot);
                }
            }
        }
    }

}
