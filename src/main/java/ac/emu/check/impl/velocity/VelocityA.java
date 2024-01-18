package ac.emu.check.impl.velocity;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.profile.EmuPlayer;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

@CheckInfo(name = "Velocity", description = "Invalid vertical velocity", type = "A")
public class VelocityA extends Check {

    public VelocityA(EmuPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet.isMovement() && profile.getVelocityData().getTicksSinceVelocityPong() <= 2) {
            int ticks = profile.getVelocityData().getTicksSinceVelocityPong();

            double deltaY = profile.getMovementData().getDeltaY();
            double lastDeltaY = profile.getMovementData().getLastDeltaY();
            double velocityY = profile.getVelocityData().getY();

            double predicted = ((isExempt(ExemptType.LAST_JUMPED) ? lastDeltaY : velocityY) - 0.08D) * 0.9800000190734863;
            double difference = Math.abs(deltaY - (ticks > 1 ? predicted : velocityY));

            boolean exempt = isExempt(
                ExemptType.TELEPORTED,
                ExemptType.SPAWNED,
                ExemptType.ON_CLIMBABLE,
                ExemptType.IN_VEHICLE,
                ExemptType.IN_WEB,
                ExemptType.CHUNK,
                ExemptType.ALLOWED_FLIGHT,
                ExemptType.UNDER_BLOCK,
                ExemptType.JUMPED
            ) || velocityY < 3E-2;

            boolean invalid = difference > 1E-10;

            if(invalid && !exempt) {
                this.fail(String.format("tick=%d, diff=%.5f, deltaY=%.5f, predicted=%.5f", ticks, difference, deltaY, predicted));
            } else {
                this.reward();
            }
        }
    }

}
