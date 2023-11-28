package me.geuxy.emu.check.impl.combat.velocity;

import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.exempt.ExemptType;
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

            boolean exempt = isExempt(
                ExemptType.TELEPORTED,
                ExemptType.SPAWNED,
                ExemptType.ON_CLIMBABLE,
                ExemptType.IN_VEHICLE
            );

            boolean invalid = difference > 1E-6 && velocityY > 1E-2;

            if(invalid && !exempt) {
                if(thriveBuffer() > 1) {
                    this.fail("diff=" + difference, "delta=" + deltaY, "expected=" + velocityY);
                }
            }
            this.decayBuffer(0.25);

        }
    }

}
