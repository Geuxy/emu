package me.geuxy.emu.check.impl.move.speed;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.api.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;

@CheckInfo(
    name = "Speed",
    description = "Invalid horizontal movement in liquid",
    type = "C"
)
public class SpeedC extends AbstractCheck {

    private double ticks;

    public SpeedC(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMove()) {
            boolean exempt =
                data.TELEPORTED ||
                data.LIVING ||
                data.CLIMBABLE ||
                data.getPositionProcessor().isClientGround() ||
                data.ALLOWED_FLYING;

            if(data.LIQUID) {
                if(ticks < 10) {
                    this.ticks++;
                }
            } else {

                // Patches my own matrix jesus bypass
                if(ticks > 10) {
                    this.ticks = 0;
                } else {
                    this.ticks -= 0.25;
                }
            }

            double lastSpeed = data.getPositionProcessor().getLastSpeed();
            double speed = data.getPositionProcessor().getSpeed();

            double predicted = (lastSpeed * 0.909997) + 2.54711021E-2;
            double maxSpeed = 0.15;

            maxSpeed *= data.getSpeedMultiplier();

            boolean invalid = data.LIQUID && speed > (ticks > 25 ? maxSpeed : Math.max(predicted, maxSpeed));

            if(invalid && !exempt) {
                if(increaseBuffer() > 2) {
                    this.fail("tick=" + ticks, "max=" + (ticks > 25 ? maxSpeed : Math.max(predicted, maxSpeed)), "delta=" + speed);
                }
            }
            this.reduceBuffer(0.05);
        }
    }

}
