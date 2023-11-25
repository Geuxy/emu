package me.geuxy.emu.check.impl.move.climb;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
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
            boolean exempt =
                data.TELEPORTED ||
                data.LIVING ||
                data.ALLOWED_FLYING ||
                data.getPositionProcessor().isClientGround();

            int ticks = data.getPositionProcessor().getClimbTicks();

            double deltaY = data.getPositionProcessor().getDeltaY();

            boolean invalid = ticks > 2 && (deltaY > 0.11761D || deltaY < -0.1501D);
            boolean invalid2 = ticks == 2 && deltaY >= 0.3;

            if((invalid || invalid2) && !exempt) {
                if(thriveBuffer() > 1) {
                    this.fail("delta=" + deltaY, "tick=" + ticks, "min=-0.1501", "max=0.11761");
                }
            }
            this.decayBuffer(0.02);
        }
    }

}
