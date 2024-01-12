package ac.emu.check.impl.jump;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.user.EmuPlayer;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

@CheckInfo(name = "Jump", description = "Invalid jump height", type = "A")
public class JumpA extends Check {

    public JumpA(EmuPlayer data) {
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
            )   // Weird way fix to falses when only jumping and building
                || (deltaY >= 0.40444491418477 && deltaY <= 0.40444491418477924 && data.getActionData().getPlaceTicks() <= 9 && data.getMovementData().getAirTicks() == 1 && data.getMovementData().getSpeed() < 0.03);

            boolean invalid = difference > 1E-13 && deltaY > 0 && !step && data.getMovementData().isLastClientGround() && !data.getMovementData().isClientGround() && data.getMovementData().getSinceUnderBlockTicks() > 4;

            if(invalid && !exempt) {
                this.fail(String.format("diff=%.5f, deltaY=%.5f, max=%.5f", difference, deltaY, maxJumpHeight));
            } else {
                this.reward();
            }
        }
    }

}
