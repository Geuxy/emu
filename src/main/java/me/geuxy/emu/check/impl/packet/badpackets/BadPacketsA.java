package me.geuxy.emu.check.impl.packet.badpackets;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;

@CheckInfo(
    name = "Bad Packets",
    description = "Impossible Pitch",
    type = "A"
)
public class BadPacketsA extends AbstractCheck {

    public BadPacketsA(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMove()) {
            float pitch = data.getRotationProcessor().getPitch();

            if(pitch > 90) {
                this.fail("max=90", "pitch=" + pitch);
            }
        }
    }

}
