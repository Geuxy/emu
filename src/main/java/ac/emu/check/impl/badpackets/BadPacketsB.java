package ac.emu.check.impl.badpackets;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.PlayerData;
import ac.emu.packet.Packet;

@CheckInfo(name = "Bad Packets", description = "Placed and dug on the same tick", type = "B")
public class BadPacketsB extends Check {

    private boolean place, dig;

    public BadPacketsB(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isPlayerBlockPlacement()) {
            this.place = true;
        }

        if(packet.isPlayerDigging()) {
            this.dig = true;
        }

        if(packet.isMovement()) {
            if(place && dig) {
                if(thriveBuffer() > 2) { // funny
                    this.fail();
                }
            } else {
                this.decayBuffer(0.25);
            }

            this.place = false;
            this.dig = false;
        }
    }

}
