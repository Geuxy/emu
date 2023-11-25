package me.geuxy.emu.check.impl.move.strafe;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.data.processors.PositionProcessor;
import me.geuxy.emu.packet.Packet;
import me.geuxy.emu.utils.entity.PlayerUtil;

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

            double predDeltaX = lastDeltaX * 0.91; // https://bugs.mojang.com/browse/MC-12832
            double predDeltaZ = lastDeltaZ * 0.91;

            boolean exempt =
                data.ALLOWED_FLYING ||
                data.LIVING ||
                data.VELOCITY ||
                data.EXPLOSION ||
                data.BLOCK_ABOVE ||
                data.RIDING ||
                data.CLIMBABLE ||
                data.LIQUID ||
                airTicks < 3 ||
                PlayerUtil.getSurroundingBlocks(data.getPlayer(), 0) ||
                PlayerUtil.getSurroundingBlocks(data.getPlayer(), 1);

            double differenceX = Math.abs(deltaX - predDeltaX);
            double differenceZ = Math.abs(deltaZ - predDeltaZ);

            boolean invalid = differenceX > 0.031 || differenceZ > 0.03;

            if(invalid && !exempt) {
                if(thriveBuffer() > 2) {
                    this.fail("tick=" + airTicks, "predX=" + predDeltaX, "predZ=" + predDeltaZ, "deltaX=" + deltaX, "deltaZ=" + deltaZ, "diffX=" + differenceX, "diffZ=" + differenceZ);
                }
                decayBuffer(0.025);
            }

        }
    }

}
