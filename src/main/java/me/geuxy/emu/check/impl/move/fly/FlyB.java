package me.geuxy.emu.check.impl.move.fly;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;
import me.geuxy.emu.utils.entity.PlayerUtil;
import me.geuxy.emu.utils.world.BlockUtils;

@CheckInfo(
    name = "Fly",
    description = "Sudden motion reduction",
    type = "B"
)
public class FlyB extends AbstractCheck {

    public FlyB(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isFlying()) {
            int airTicks = data.getPositionProcessor().getAirTicks();

            double lastDeltaY = data.getPositionProcessor().getLastDeltaY();
            double deltaY = data.getPositionProcessor().getDeltaY();

            boolean exempt =
                data.TELEPORTED ||
                data.ALLOWED_FLYING ||
                data.RIDING ||
                data.LIVING ||
                data.BLOCK_ABOVE ||
                data.STEPPING ||
                PlayerUtil.isNearBoat(data.getPlayer());

            if(data.STEPPING && deltaY <= 0.5) {
                this.resetBuffer();
            }

            boolean invalid = lastDeltaY > 0.1D && deltaY <= 0.07D;

            if(invalid && !exempt) {
                if(thriveBuffer() > 1) {
                    this.fail("delta=" + deltaY, "last=" + lastDeltaY,  "diff=" + Math.abs(deltaY - lastDeltaY), "tick=" + airTicks);
                }
            }
            this.decayBuffer(0.02);
        }
    }

}
