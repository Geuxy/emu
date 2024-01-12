package ac.emu.check.impl.climb;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.user.EmuPlayer;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

@CheckInfo(name = "Climb", description = "Invalid climb height", type = "B")
public class ClimbB extends Check {

    public ClimbB(EmuPlayer data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMovement()) {
            boolean exempt = isExempt(ExemptType.TELEPORTED, ExemptType.SPAWNED, ExemptType.ALLOWED_FLIGHT) || data.getMovementData().isClientGround();

            int ticks = data.getMovementData().getClimbTicks();

            double deltaY = data.getMovementData().getDeltaY();
            double lastDeltaY = data.getMovementData().getLastDeltaY();

            boolean climbing = isExempt(ExemptType.ON_CLIMBABLE) && Math.abs(deltaY - lastDeltaY) < 1E-4;

            boolean invalid = ticks > 2 && (deltaY > 0.1178 || deltaY < -0.1501);
            boolean invalid2 = ticks < 3 && (deltaY > lastDeltaY || deltaY > data.getUtilities().getJumpHeight());

            if(climbing && (invalid || invalid2) && !exempt) {
                this.fail(String.format("tick=%d, deltaY=%.5f, min=-0.1501, max=0.1178", ticks, deltaY));
            } else {
                this.reward();
            }
        }
    }

}