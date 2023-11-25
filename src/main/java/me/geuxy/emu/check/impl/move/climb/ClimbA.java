package me.geuxy.emu.check.impl.move.climb;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;

@CheckInfo(
    name = "Climb",
    description = "Invalid horizontal movement while climbing",
    type = "A"
)
public class ClimbA extends AbstractCheck {

    public ClimbA(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isFlying()) {
            boolean exempt =
                data.TELEPORTED ||
                data.LIVING ||
                data.ALLOWED_FLYING;

            int ticks = data.getPositionProcessor().getClimbTicks();

            double lastSpeed = data.getPositionProcessor().getLastSpeed();
            double speed = data.getPositionProcessor().getSpeed();

            double deltaY = data.getPositionProcessor().getDeltaY();
            double lastDeltaY = data.getPositionProcessor().getLastDeltaY();

            boolean climbing = Math.abs(deltaY - lastDeltaY) < 1E-4 && !data.getPositionProcessor().isClientGround() && data.CLIMBABLE;

            boolean invalid = ticks > 2 && speed > 0.16;
            boolean invalid2 = ticks < 3 && speed >= lastSpeed;

            if(climbing && (invalid || invalid2) && !exempt) {
                if(thriveBuffer() > 2) {
                    this.fail("tick=" + ticks, "delta=" + speed, "min=-0.3","max=0.150000006");
                }
            }
            this.decayBuffer(0.05);
        }
    }

}
