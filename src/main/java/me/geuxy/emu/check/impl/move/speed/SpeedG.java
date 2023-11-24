package me.geuxy.emu.check.impl.move.speed;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.api.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;

@CheckInfo(
    name = "Speed",
    description = "Invalid horizontal movement in air",
    type = "G"
)
public class SpeedG extends AbstractCheck {

    public SpeedG(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMove()) {

            boolean exempt =
                data.VELOCITY ||
                data.EXPLOSION ||
                data.TELEPORTED ||
                data.LIVING ||
                data.ALLOWED_FLYING ||
                data.RIDING;

            double lastSpeed = data.getPositionProcessor().getLastSpeed();
            double speed = data.getPositionProcessor().getSpeed();

            if(data.getPositionProcessor().isLastClientGround() && !data.getPositionProcessor().isClientGround()) {
                double prediction = lastSpeed * 1.991;
                double difference = speed - prediction;

                boolean invalid = difference > 6E-4 && speed > 0.61;

                if(invalid && !exempt) {
                    this.fail("diff=" + difference, "expected=" + prediction, "delta=" + speed);
                }
            }
        }
    }

}
