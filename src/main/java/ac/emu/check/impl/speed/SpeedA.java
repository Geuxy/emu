package ac.emu.check.impl.speed;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.profile.EmuPlayer;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

@CheckInfo(name = "Speed", description = "Invalid horizontal movement in air", type = "A")
public class SpeedA extends Check {

    public SpeedA(EmuPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(packet.isMovement()) {
            if(!profile.getMovementData().isLastClientGround() && !profile.getMovementData().isClientGround()) {
                boolean exempt = isExempt(
                    ExemptType.SPAWNED,
                    ExemptType.ALLOWED_FLIGHT,
                    ExemptType.TELEPORTED,
                    ExemptType.IN_LIQUID,
                    ExemptType.IN_VEHICLE,
                    ExemptType.IN_WEB,
                    ExemptType.VELOCITY,
                    ExemptType.EXPLOSION
                );

                double speed = profile.getMovementData().getSpeed();
                double lastSpeed = profile.getMovementData().getLastSpeed();

                double predicted = lastSpeed * 0.9100000262260448D + 0.026D;
                double difference = speed - predicted;

                int airTicks = profile.getMovementData().getAirTicks();

                boolean invalid = difference > 1E-4 && speed > 0.1;

                if (invalid && !exempt) {
                    this.fail(String.format("tick=%d, diff=%.5f, predicted=%.5f, speed=%.5f", airTicks, difference, predicted, speed));
                }
                this.reward();
            }
        }
    }

}
