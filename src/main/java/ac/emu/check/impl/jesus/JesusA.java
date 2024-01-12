package ac.emu.check.impl.jesus;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.user.EmuPlayer;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

@CheckInfo(name = "Jesus", description = "Invalid horizontal movement in liquid", type = "A")
public class JesusA extends Check {

    private double ticks;

    public JesusA(EmuPlayer data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMovement()) {
            boolean exempt = isExempt(
                ExemptType.TELEPORTED,
                ExemptType.SPAWNED,
                ExemptType.ON_CLIMBABLE,
                ExemptType.ALLOWED_FLIGHT
            ) || data.getMovementData().isClientGround();

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

            double speed = data.getMovementData().getSpeed();
            double lastSpeed = data.getMovementData().getLastSpeed();

            double maxSpeed = ticks > 20 ? 0.15 : (lastSpeed * 0.909997) + 2.54711021E-2;

            maxSpeed += data.getUtilities().getSpeedMultiplier();

            boolean invalid = isExempt(ExemptType.IN_LIQUID) && speed > maxSpeed && ticks > 16;

            if(invalid && !exempt) {
                this.fail(String.format("tick=%.0f, max=%.5f, speed=%.5f", ticks, maxSpeed, speed));
            } else {
                this.reward();
            }
        }
    }

}
