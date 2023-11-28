package me.geuxy.emu.check.impl.move.strafe;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.data.processors.PositionProcessor;
import me.geuxy.emu.exempt.ExemptType;
import me.geuxy.emu.packet.Packet;
import me.geuxy.emu.utils.entity.PlayerUtil;
import me.geuxy.emu.utils.world.BlockUtils;

@CheckInfo(
    name = "Strafe",
    description = "Moved wrong in air",
    type = "A"
)
public class StrafeA extends AbstractCheck {

    public StrafeA(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isFlying()) {
            PositionProcessor processor = data.getPositionProcessor();

            int airTicks = data.getPositionProcessor().getAirTicks();

            double deltaX = processor.getDeltaX();
            double deltaZ = processor.getDeltaZ();
            double lastDeltaX = processor.getLastDeltaX();
            double lastDeltaZ = processor.getLastDeltaZ();

            double predDeltaX = lastDeltaX * 0.91; // https://bugs.msudo pojang.com/browse/MC-12832
            double predDeltaZ = lastDeltaZ * 0.91;

            boolean exempt = isExempt(
                ExemptType.ALLOWED_FLIGHT,
                ExemptType.SPAWNED,
                ExemptType.VELOCITY,
                ExemptType.EXPLOSION,
                ExemptType.UNDER_BLOCK,
                ExemptType.IN_VEHICLE,
                ExemptType.ON_CLIMBABLE,
                ExemptType.IN_LIQUID
            ) || airTicks < 3 ||
                BlockUtils.getSurroundingBlocks(data.getPlayer(), 0D, 0.35D).stream().anyMatch(b -> b.getType().isSolid()) ||
                BlockUtils.getSurroundingBlocks(data.getPlayer(), 1D, 0.35D).stream().anyMatch(b -> b.getType().isSolid());

            double differenceX = Math.abs(deltaX - predDeltaX);
            double differenceZ = Math.abs(deltaZ - predDeltaZ);

            boolean invalid = (differenceX > 0.031 || differenceZ > 0.031) && data.getPositionProcessor().getSpeed() > 0.3;

            if(invalid && !exempt) {
                if(thriveBuffer() > 2) {
                    this.fail("tick=" + airTicks, "predX=" + predDeltaX, "predZ=" + predDeltaZ, "deltaX=" + deltaX, "deltaZ=" + deltaZ, "diffX=" + differenceX, "diffZ=" + differenceZ);
                }
                decayBuffer(0.025);
            }

        }
    }

}
