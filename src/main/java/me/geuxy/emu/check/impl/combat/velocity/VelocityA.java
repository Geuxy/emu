package me.geuxy.emu.check.impl.combat.velocity;

import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;

@CheckInfo(
    name = "Velocity",
    description = "Invalid vertical velocity",
    type = "A"
)
public class VelocityA extends AbstractCheck {

    public VelocityA(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if (packet.isFlying() && data.getVelocityProcessor().getVelocityTicks() == 1) {
            double deltaY = data.getPositionProcessor().getDeltaY();
            double velocityY = data.getVelocityProcessor().getY();

            double difference = Math.abs(deltaY - velocityY);

            boolean invalid = difference > 0.1;

            boolean exempt =
                data.TELEPORTED ||
                data.LIVING ||
                data.CLIMBABLE ||
                data.RIDING;

            if (invalid && !exempt) {
                this.fail("diff=" + difference, "delta=" + deltaY, "expected=" + velocityY);
            }

        }
    }

}
