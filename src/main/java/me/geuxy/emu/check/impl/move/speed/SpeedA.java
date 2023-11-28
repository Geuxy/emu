package me.geuxy.emu.check.impl.move.speed;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.exempt.ExemptType;
import me.geuxy.emu.packet.Packet;

@CheckInfo(
    name = "Speed",
    description = "Invalid horizontal movement in air",
    type = "A"
)
public class SpeedA extends AbstractCheck {

    public SpeedA(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isFlying()) {
            if(!data.getPositionProcessor().isLastClientGround() && !data.getPositionProcessor().isClientGround()) {
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

                double speed = data.getPositionProcessor().getSpeed();
                double lastSpeed = data.getPositionProcessor().getLastSpeed();

                double predicted = lastSpeed * 0.9100000262260448D + 0.026D;
                double difference = speed - predicted;

                int airTicks = data.getPositionProcessor().getAirTicks();

                boolean invalid = difference > 1E-4 && speed > 0.1;

                if (invalid && !exempt) {
                    if (thriveBuffer() > 1) {
                        this.fail("diff=" + difference, "tick=" + airTicks, "expected=" + predicted, "delta=" + speed);
                    }
                }
                this.decayBuffer(0.02);
            }
        }
    }

}
