package me.geuxy.emu.check.impl.packet.badpackets;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;

@CheckInfo(
    name = "Bad Packets",
    description = "Placed and dug on the same tick",
    type = "B"
)
public class BadPacketsB extends AbstractCheck {

    private boolean place, dig;

    public BadPacketsB(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isBlockPlace()) {
            this.place = true;
        }

        if(packet.isBlockDig()) {
            this.dig = true;
        }

        if(packet.isFlying()) {
            if(place && dig) {
                this.fail();
            }
            this.place = false;
            this.dig = false;
        }
    }

}
