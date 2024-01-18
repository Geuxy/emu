package ac.emu.check.impl.climb;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.profile.EmuPlayer;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

@CheckInfo(name = "Climb", description = "Invalid horizontal movement while climbing", type = "A")
public class ClimbA extends Check {

    public ClimbA(EmuPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(packet.isMovement()) {
            boolean exempt = isExempt(ExemptType.TELEPORTED, ExemptType.SPAWNED, ExemptType.ALLOWED_FLIGHT);

            int ticks = profile.getMovementData().getClimbTicks();

            double speed = profile.getMovementData().getSpeed();
            double lastSpeed = profile.getMovementData().getLastSpeed();

            double deltaY = profile.getMovementData().getDeltaY();
            double lastDeltaY = profile.getMovementData().getLastDeltaY();

            boolean climbing = isExempt(ExemptType.ON_CLIMBABLE) && Math.abs(deltaY - lastDeltaY) <= 1E-4 && !profile.getMovementData().isClientGround();

            boolean invalid = ticks > 2 && speed > 0.18;
            boolean invalid2 = ticks < 3 && speed >= lastSpeed;

            if(climbing && (invalid || invalid2) && !exempt) {
                this.fail(String.format("tick=%d, speed=%.5f, diff=%.5f", ticks, speed, speed - 0.18));
            }
            this.reward();
        }
    }

}
