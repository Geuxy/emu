package me.geuxy.emu.check.impl.move.climb;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.exempt.ExemptType;
import me.geuxy.emu.packet.Packet;

@CheckInfo(
    name = "Climb",
    description = "Invalid climb height",
    type = "B"
)
public class ClimbB extends AbstractCheck {

    public ClimbB(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isFlying()) {
            boolean exempt = isExempt(
                ExemptType.TELEPORTED,
                ExemptType.SPAWNED,
                ExemptType.ALLOWED_FLIGHT
            ) || data.getPositionProcessor().isClientGround();

            int ticks = data.getPositionProcessor().getClimbTicks();

            double deltaY = data.getPositionProcessor().getDeltaY();
            double lastDeltaY = data.getPositionProcessor().getLastDeltaY();

            boolean climbing = isExempt(ExemptType.ON_CLIMBABLE) && Math.abs(deltaY - lastDeltaY) < 1E-4;

            boolean invalid = ticks > 2 && (deltaY > 0.11761D || deltaY < -0.1501D);
            boolean invalid2 = ticks < 3 && deltaY > lastDeltaY;

            if(climbing && (invalid || invalid2) && !exempt) {
                if(thriveBuffer() > 1) {
                    this.fail("delta=" + deltaY, "tick=" + ticks, "min=-0.1501", "max=0.11761");
                }
            }
            this.decayBuffer(0.02);
        }
    }

}
