package me.geuxy.emu.check.impl.move.jesus;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.exempt.ExemptType;
import me.geuxy.emu.packet.Packet;
import me.geuxy.emu.utils.world.BlockUtils;

@CheckInfo(
    name = "Jesus",
    description = "Invalid vertical movement in liquid",
    type = "B"
)
public class JesusB extends AbstractCheck {

    private int ticks;

    public JesusB(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isFlying()) {
            if(isExempt(ExemptType.IN_LIQUID)) {
                this.ticks++;

                double deltaY = data.getPositionProcessor().getDeltaY();
                double lastDeltaY = data.getPositionProcessor().getLastDeltaY();
                double difference = deltaY - lastDeltaY;

                boolean halfSubmerged = BlockUtils.getSurroundingBlocks(data.getPlayer(), 1D).stream().noneMatch(BlockUtils::isLiquid);

                boolean exempt = isExempt(
                    ExemptType.VELOCITY,
                    ExemptType.TELEPORTED,
                    ExemptType.ALLOWED_FLIGHT,
                    ExemptType.ON_CLIMBABLE,
                    ExemptType.IN_VEHICLE,
                    ExemptType.BOAT,
                    ExemptType.STEPPING
                ) || (difference < 0.7 && data.getPositionProcessor().isClientGround());


                boolean invalid2 = ticks > 6 && deltaY > 0.1 && difference > (halfSubmerged ? 0.43 : 0.07);

                if((invalid2) && !exempt) {
                    this.fail("diff=" + difference, "delta=" + deltaY, "last=" + lastDeltaY);
                }
            } else {
                this.ticks = 0;
            }
        }
    }

}
