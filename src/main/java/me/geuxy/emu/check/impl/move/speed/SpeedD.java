package me.geuxy.emu.check.impl.move.speed;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;

@CheckInfo(
    name = "Speed",
    description = "Invalid horizontal movement",
    type = "D"
)
public class SpeedD extends AbstractCheck {

    public SpeedD(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isFlying()) {
            boolean exempt =
                data.TELEPORTED ||
                data.LIVING;

            double maxSpeed = 2D;
            double speed = data.getPositionProcessor().getSpeed();

            boolean invalid = speed > maxSpeed;

            if(invalid && !exempt) {
                if(thriveBuffer() > 1) {
                    this.fail("max=" + maxSpeed, "delta=" + speed);
                }
            }
            this.decayBuffer(0.02);
        }
    }

}
