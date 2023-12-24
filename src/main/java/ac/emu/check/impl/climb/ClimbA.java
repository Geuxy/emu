package ac.emu.check.impl.climb;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.PlayerData;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

@CheckInfo(name = "Climb", description = "Invalid horizontal movement while climbing", type = "A")
public class ClimbA extends Check {

    public ClimbA(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMovement()) {
            boolean exempt = isExempt(
                ExemptType.TELEPORTED,
                ExemptType.SPAWNED,
                ExemptType.ALLOWED_FLIGHT
            );

            int ticks = data.getMovementData().getClimbTicks();

            double speed = data.getMovementData().getSpeed();
            double lastSpeed = data.getMovementData().getLastSpeed();

            double deltaY = data.getMovementData().getDeltaY();
            double lastDeltaY = data.getMovementData().getLastDeltaY();

            boolean climbing = isExempt(ExemptType.ON_CLIMBABLE) && Math.abs(deltaY - lastDeltaY) <= 1E-4 && !data.getMovementData().isClientGround();

            boolean invalid = ticks > 2 && speed > 0.18;
            boolean invalid2 = ticks < 3 && speed >= lastSpeed;

            if(climbing && (invalid || invalid2) && !exempt) {
                if(thriveBuffer() > 2) {
                    this.fail(String.format("tick=%d, speed=%.5f, diff=%.5f", ticks, speed, speed - 0.18));
                }
            }
            this.decayBuffer(0.025);
        }
    }

}
