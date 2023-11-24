package me.geuxy.emu.check.impl.move.step;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.api.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;

@CheckInfo(
    name = "Step",
    description = "Invalid step height",
    type = "A"
)
public class StepA extends AbstractCheck {

    public StepA(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMove()) {
            boolean exempt =
                data.TELEPORTED ||
                data.LIVING ||
                data.SLIME ||
                data.RIDING ||
                data.EXPLOSION;

            double deltaY = data.getPositionProcessor().getDeltaY();

            boolean invalid = deltaY > 0.6000000238418579D && data.getPositionProcessor().isLastClientGround();

            if(invalid && !exempt) {
                this.fail("delta=" + deltaY);
            }
        }
    }

}
