package ac.emu.check.impl.badpackets;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.PlayerData;
import ac.emu.packet.Packet;

@CheckInfo(name = "Bad Packets", description = "Impossible Pitch", type = "A")
public class BadPacketsA extends Check {

    public BadPacketsA(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMovement()) {
            float pitch = data.getMovementData().getPitch();

            if(pitch > 90) {
                this.fail("pitch=" + pitch);
            }
        }
    }

}
