package ac.emu.check.impl.noslow;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.user.EmuPlayer;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

@CheckInfo(name = "NoSlow", description = "Invalid horizontal movement in web", type = "A")
public class NoSlowA extends Check {

    private int ticks;

    public NoSlowA(EmuPlayer data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMovement()) {
            boolean exempt = isExempt(ExemptType.TELEPORTED, ExemptType.SPAWNED, ExemptType.ALLOWED_FLIGHT, ExemptType.IN_VEHICLE);

            if(isExempt(ExemptType.IN_WEB)) {
                if(ticks < 10) {
                    this.ticks++;
                }
            } else {
                this.ticks = 0;
            }

            double speed = data.getMovementData().getSpeed();

            double maxSpeed = 0.158;

            maxSpeed += data.getUtilities().getSpeedMultiplier();

            boolean invalid = ticks > 1 && speed > maxSpeed;

            if(invalid && !exempt) {
                this.fail(String.format("tick=%d, speed=%.5f, max=%.5f", ticks, speed, maxSpeed));
            } else {
                this.reward();
            }
        }
    }

}
