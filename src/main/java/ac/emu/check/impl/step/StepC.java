package ac.emu.check.impl.step;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.PlayerData;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

@CheckInfo(name = "Step", description = "Invalid step", type = "C")
public class StepC extends Check {

    private int packets;

    public StepC(PlayerData data) {
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

            boolean mathGround = data.getMovementData().isMathGround();
            boolean lastMathGround = data.getMovementData().isLastMathGround();

            this.packets = lastMathGround ? 0 : ++this.packets;

            boolean invalid = deltaY <= 0.5 && deltaY > 0 && packets > 1 && packets < 5 && mathGround;

            if(invalid && !exempt) {
                this.fail();
            }
        }
    }

}
