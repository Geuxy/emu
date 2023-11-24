package me.geuxy.emu.check.impl.move.fly;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;

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
            boolean exempt =
                data.TELEPORTED ||
                data.ALLOWED_FLYING ||
                data.RIDING ||
                data.LIVING ||
                data.BLOCK_ABOVE;

            double lastDeltaY = data.getPositionProcessor().getLastDeltaY();
            double deltaY = data.getPositionProcessor().getDeltaY();

            int airTicks = data.getPositionProcessor().getAirTicks();

            boolean invalid = lastDeltaY > 0.1D && deltaY <= 0.03D;

            if(invalid && !exempt) {
                if(increaseBuffer() > 1) {
                    this.fail("delta=" + deltaY, "last=" + lastDeltaY,  "diff=" + Math.abs(deltaY - lastDeltaY),"tick=" + airTicks);
                }
            }
            this.reduceBuffer(0.02);
        }
    }

}
