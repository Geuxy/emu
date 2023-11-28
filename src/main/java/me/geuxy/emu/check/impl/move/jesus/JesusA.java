package me.geuxy.emu.check.impl.move.jesus;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.exempt.ExemptType;
import me.geuxy.emu.packet.Packet;

@CheckInfo(
    name = "Jesus",
    description = "Invalid horizontal movement in liquid",
    type = "A"
)
public class JesusA extends AbstractCheck {

    private double ticks;

    public JesusA(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isFlying()) {
            boolean exempt = isExempt(
                ExemptType.TELEPORTED,
                ExemptType.SPAWNED,
                ExemptType.ON_CLIMBABLE,
                ExemptType.ALLOWED_FLIGHT
            ) || data.getPositionProcessor().isClientGround();

            if(isExempt(ExemptType.IN_LIQUID)) {
                if(ticks < 21) {
                    this.ticks++;
                }
            } else {

                // Patches my own matrix jesus bypass
                if(ticks > 10) {
                    this.ticks = 0;
                } else {
                    this.ticks = Math.max(0, ticks - 0.25);
                }
            }

            if(isExempt(ExemptType.VELOCITY) && ticks > 10) {
                this.ticks = 0;
            }

            double speed = data.getPositionProcessor().getSpeed();
            double lastSpeed = data.getPositionProcessor().getLastSpeed();

            double maxSpeed = ticks > 20 ? 0.15 : (lastSpeed * 0.909997) + 2.54711021E-2;

            maxSpeed += data.getSpeedMultiplier();

            boolean invalid = isExempt(ExemptType.IN_LIQUID) && speed > maxSpeed && ticks > 16;

            if(invalid && !exempt) {
                if(thriveBuffer() > 2) {
                    this.fail("tick=" + ticks, "max=" + maxSpeed, "delta=" + speed);
                }
            }
            this.decayBuffer(0.05);
        }
    }

}
