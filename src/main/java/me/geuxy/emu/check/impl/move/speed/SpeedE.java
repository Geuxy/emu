package me.geuxy.emu.check.impl.move.speed;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.api.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;

@CheckInfo(
    name = "Speed",
    description = "Invalid horizontal movement in web",
    type = "E"
)
public class SpeedE extends AbstractCheck {

    private int ticks;

    public SpeedE(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMove()) {
            boolean exempt =
                data.TELEPORTED ||
                data.LIVING ||
                data.ALLOWED_FLYING ||
                data.RIDING;

            if(data.WEB) {
                if(ticks < 10) {
                    this.ticks++;
                }
            } else {
                this.ticks = 0;
            }

            double speed = data.getPositionProcessor().getSpeed();

            double maxSpeed = data.getPlayer().isSneaking() ? 0.037 : (data.getPlayer().getFallDistance() > 0.1 ? 0.078 : 0.1137);

            maxSpeed *= data.getSpeedMultiplier();

            boolean invalid = ticks > 1 && speed > maxSpeed;

            if(invalid && !exempt) {
                if(increaseBuffer() > 1) {
                    this.fail("tick=" + ticks, "max=" + maxSpeed, "delta=" + speed);
                }
            }
            this.reduceBuffer(0.05);
        }
    }

}
