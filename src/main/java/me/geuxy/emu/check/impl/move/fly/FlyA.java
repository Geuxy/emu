package me.geuxy.emu.check.impl.move.fly;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;
import me.geuxy.emu.utils.PlayerUtil;

@CheckInfo(
    name = "Fly",
    description = "Invalid vertical movement",
    type = "A"
)
public class FlyA extends AbstractCheck {

    public FlyA(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMove()) {
            if(!data.getPositionProcessor().isLastClientGround() && !data.getPositionProcessor().isClientGround()) {
                double lastDeltaY = data.getPositionProcessor().getLastDeltaY();
                double deltaY = data.getPositionProcessor().getDeltaY();

                double predicted = (lastDeltaY - 0.08D) * 0.9800000190734863D;
                double falseFix = Math.abs(predicted) < 6E-3D ? 0D : predicted;
                double difference = Math.abs(deltaY - falseFix);

                int airTicks = data.getPositionProcessor().getAirTicks();

                boolean exempt =
                    data.TELEPORTED ||
                    data.RIDING ||
                    data.CLIMBABLE ||
                    data.ALLOWED_FLYING ||
                    data.LIVING ||
                    data.WEB ||
                    data.LIQUID ||
                    data.CHUNK ||
                    PlayerUtil.isNearBoat(data.getPlayer()) ||
                    difference == 0.7840000152587834E-1D ||
                    difference == 0.6349722830977471 ||
                    difference == 0.01524397154484497;

                boolean invalid = difference > 1E-8D;

                if (invalid && !exempt) {
                    if (increaseBuffer() > 1) {
                        this.fail("diff=" + difference, "tick=" + airTicks, "expected=" + predicted, "delta=" + deltaY);
                    }
                }
                this.reduceBuffer(0.01D);
            }
        }
    }

}
