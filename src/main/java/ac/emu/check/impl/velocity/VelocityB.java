package ac.emu.check.impl.velocity;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.user.EmuPlayer;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

@CheckInfo(name = "Velocity", description = "Invalid horizontal velocity", type = "B")
public class VelocityB extends Check {

    public VelocityB(EmuPlayer data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if (packet.isMovement() && data.getVelocityData().getTicksSinceVelocityPong() <= 2) {
            int ticks = data.getVelocityData().getTicksSinceVelocityPong();

            double speed = data.getMovementData().getSpeed();
            double lastSpeed = data.getMovementData().getLastSpeed();
            double velocitySpeed = data.getVelocityData().getSpeed();
            double predicted = (isExempt(ExemptType.LAST_JUMPED) ? lastSpeed : velocitySpeed) * 0.9100000262260448 + 0.026;

            double difference = (ticks > 1 ? predicted : velocitySpeed) - speed;

            boolean exempt = isExempt(
                ExemptType.TELEPORTED,
                ExemptType.SPAWNED,
                ExemptType.ON_CLIMBABLE,
                ExemptType.IN_VEHICLE,
                ExemptType.IN_WEB,
                ExemptType.CHUNK,
                ExemptType.ALLOWED_FLIGHT,
                ExemptType.UNDER_BLOCK,
                ExemptType.NEAR_WALL
            ) || velocitySpeed <= 0.04575;

            boolean invalid = difference >= 0.267 || difference <= -0.45;
            boolean invalid2 = data.getVelocityData().getX() < -0.1 && data.getMovementData().getX() - data.getMovementData().getLastX() > 0
                    || data.getVelocityData().getX() > 0.1 && data.getMovementData().getX() - data.getMovementData().getLastX() < 0
                    || data.getVelocityData().getZ() < -0.1 && data.getMovementData().getZ() - data.getMovementData().getLastZ() > 0
                    || data.getVelocityData().getZ() > 0.1 && data.getMovementData().getZ() - data.getMovementData().getLastZ() < 0;

            if((invalid || invalid2) && !exempt) {
                if(fail(String.format("tick=%d, diff=%.5f, speed=%.5f, predicted=%.5f", ticks, difference, speed, (ticks > 1 ? predicted : velocitySpeed)))) {
                    this.resetBuffer();
                }
            } else {
                this.reward();
            }
        }
    }

}
