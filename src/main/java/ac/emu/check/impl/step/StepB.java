package ac.emu.check.impl.step;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.profile.EmuPlayer;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

@CheckInfo(name = "Step", description = "Invalid fast fall", type = "B")
public class StepB extends Check {

    public StepB(EmuPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(packet.isMovement()) {
            boolean exempt = isExempt(
                ExemptType.TELEPORTED,
                ExemptType.SPAWNED,
                ExemptType.IN_VEHICLE,
                ExemptType.EXPLOSION,
                ExemptType.BOAT
            );

            double deltaY = profile.getMovementData().getDeltaY();

            boolean invalid = deltaY <= -0.07841 && profile.getMovementData().isLastClientGround() && !profile.getMovementData().isClientGround();

            if(invalid && !exempt) {
                this.fail("delta=" + deltaY);
            } else {
                this.reward();
            }
        }
    }

}
