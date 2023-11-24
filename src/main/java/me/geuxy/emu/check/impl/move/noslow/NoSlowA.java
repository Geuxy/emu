package me.geuxy.emu.check.impl.move.noslow;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;

@CheckInfo(
    name = "NoSlow",
    description = "Invalid horizontal movement in web",
    type = "A"
)
public class NoSlowA extends AbstractCheck {

    private int ticks;

    public NoSlowA(PlayerData data) {
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
