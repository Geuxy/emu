package me.geuxy.emu.check.impl.move.step;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;

@CheckInfo(
    name = "Step",
    description = "Invalid fast fall",
    type = "B"
)
public class StepB extends AbstractCheck {

    public StepB(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isFlying()) {
            boolean exempt =
                data.TELEPORTED ||
                data.LIVING ||
                data.RIDING ||
                data.EXPLOSION;

            double deltaY = data.getPositionProcessor().getDeltaY();

            boolean invalid = deltaY <= -0.07841D && data.getPositionProcessor().isLastClientGround();

            if(invalid && !exempt) {
                this.fail("delta=" + deltaY);
            }
        }
    }

}
