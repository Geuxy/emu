package me.geuxy.emu.check.impl.move.speed;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.api.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;

@CheckInfo(
    name = "Speed",
    description = "Invalid horizontal movement in air",
    type = "A"
)
public class SpeedA extends AbstractCheck {

    public SpeedA(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMove()) {
            if(!data.getPositionProcessor().isLastClientGround() && !data.getPositionProcessor().isClientGround()) {
                boolean exempt =
                    data.LIVING ||
                    data.ALLOWED_FLYING ||
                    data.TELEPORTED ||
                    data.LIQUID ||
                    data.RIDING ||
                    data.WEB ||
                    data.VELOCITY ||
                    data.EXPLOSION;

                double speed = data.getPositionProcessor().getSpeed();
                double lastSpeed = data.getPositionProcessor().getLastSpeed();

                double predicted = lastSpeed * 0.9100000262260448D + 0.026D;
                double difference = speed - predicted;

                int airTicks = data.getPositionProcessor().getAirTicks();

                boolean invalid = difference > 1E-4;

                if (invalid && !exempt) {
                    if (increaseBuffer() > 1) {
                        this.fail("diff=" + difference, "tick=" + airTicks, "expected=" + predicted, "delta=" + speed);
                    }
                }
                this.reduceBuffer(0.02);
            }
        }
    }

}
