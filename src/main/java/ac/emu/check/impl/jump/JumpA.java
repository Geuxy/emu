package ac.emu.check.impl.jump;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.PlayerData;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

import ac.emu.utils.BlockUtils;

@CheckInfo(name = "Jump", description = "Invalid jump height", type = "A")
public class JumpA extends Check {

    public JumpA(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMovement()) {
            double maxJumpHeight = data.getUtilities().getJumpHeight();
            double deltaY = data.getMovementData().getDeltaY();

            boolean step = data.getMovementData().isMathGround() && data.getMovementData().isLastMathGround();
            double difference = Math.abs(deltaY - maxJumpHeight);

            boolean exempt = isExempt(
                ExemptType.TELEPORTED,
                ExemptType.SPAWNED,
                ExemptType.EXPLOSION,
                ExemptType.IN_LIQUID,
                ExemptType.IN_WEB,
                ExemptType.VELOCITY,
                ExemptType.ON_SLIME,
                ExemptType.UNDER_BLOCK,
                ExemptType.ON_CLIMBABLE,
                ExemptType.BOAT
            ) || difference == 0.015555072702198913;

            boolean invalid = difference > 1E-13 && deltaY > 0 && !step && data.getMovementData().isLastClientGround() && !data.getMovementData().isClientGround();

            if(invalid && !exempt) {
                this.fail(String.format("diff=%.5f, deltaY=%.5f, max=%.5f", difference, deltaY, maxJumpHeight));
            }
        }
    }

}
