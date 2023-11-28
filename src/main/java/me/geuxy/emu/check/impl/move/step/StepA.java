package me.geuxy.emu.check.impl.move.step;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.exempt.ExemptType;
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
        if(packet.isFlying()) {
            boolean exempt = isExempt(
                ExemptType.TELEPORTED,
                ExemptType.SPAWNED,
                ExemptType.ON_SLIME,
                ExemptType.IN_VEHICLE,
                ExemptType.EXPLOSION
            );

            double deltaY = data.getPositionProcessor().getDeltaY();

            boolean invalid = deltaY > 0.62 && data.getPositionProcessor().isLastClientGround();

            if(invalid && !exempt) {
                this.fail("delta=" + deltaY);
            }
        }
    }

}
