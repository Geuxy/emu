package ac.emu.check.impl.step;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.PlayerData;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

@CheckInfo(name = "Step", description = "Invalid step height", type = "A")
public class StepA extends Check {

    public StepA(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMovement()) {
            boolean exempt = isExempt(
                ExemptType.TELEPORTED,
                ExemptType.SPAWNED,
                ExemptType.ON_SLIME,
                ExemptType.IN_VEHICLE,
                ExemptType.EXPLOSION
            );

            double deltaY = data.getMovementData().getDeltaY();

            boolean invalid = deltaY > 0.5 && data.getMovementData().isLastClientGround() && data.getMovementData().isLastMathGround();

            if(invalid && !exempt) {
                this.fail("delta=" + deltaY);
            }
        }
    }

}
