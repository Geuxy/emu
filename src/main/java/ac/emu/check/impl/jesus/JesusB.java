package ac.emu.check.impl.jesus;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.utils.BlockUtils;
import ac.emu.user.EmuPlayer;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

@CheckInfo(name = "Jesus", description = "Invalid vertical movement in liquid", type = "B")
public class JesusB extends Check {

    private int ticks;

    public JesusB(EmuPlayer data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMovement()) {
            if(isExempt(ExemptType.IN_LIQUID)) {
                this.ticks++;

                double deltaY = data.getMovementData().getDeltaY();
                double lastDeltaY = data.getMovementData().getLastDeltaY();
                double difference = deltaY - lastDeltaY;

                boolean halfSubmerged = BlockUtils.getSurroundingBlocks(data.getPlayer(), 1D).stream().noneMatch(BlockUtils::isLiquid);
                boolean nearSolid = BlockUtils.getSurroundingBlocks(data.getPlayer(), 0, 0.35).stream().anyMatch(b -> b.getType().isSolid());

                boolean exempt = isExempt(
                    ExemptType.VELOCITY,
                    ExemptType.TELEPORTED,
                    ExemptType.ALLOWED_FLIGHT,
                    ExemptType.ON_CLIMBABLE,
                    ExemptType.IN_VEHICLE,
                    ExemptType.BOAT,
                    ExemptType.STEPPING
                ) || (difference < 0.7 && data.getMovementData().isClientGround());

                boolean invalid = ticks > 6 && deltaY > 0.1 && difference > (halfSubmerged && nearSolid ? 0.5 : 0.07);
                boolean invalid2 = ticks < 7 && halfSubmerged && difference > 0.0809 && !data.getMovementData().isMathGround();

                if((invalid || invalid2) && !exempt) {
                    this.fail(String.format("tick=%d, diff=%.5f, deltaY=%.5f, lastDeltaY=%.5f", ticks, difference, deltaY, lastDeltaY));
                } else {
                    this.reward();
                }
            } else {
                this.ticks = 0;
            }
        }
    }

}
