package me.geuxy.emu.check.impl.move.climb;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;

@CheckInfo(
    name = "Climb",
    description = "Invalid horizontal movement while climbing",
    type = "A"
)
public class ClimbA extends AbstractCheck {

    public ClimbA(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMove()) {
            boolean exempt =
                data.TELEPORTED ||
                data.LIVING;

            int ticks = data.getPositionProcessor().getClimbTicks();

            double lastSpeed = data.getPositionProcessor().getLastSpeed();
            double speed = data.getPositionProcessor().getSpeed();

            boolean invalid = (ticks > 2 && (speed > 0.150000006 || speed < -0.3));
            boolean invalid2 = ticks < 3 && speed >= lastSpeed;

            if(data.CLIMBABLE && (invalid || invalid2) && !exempt) {
                if(increaseBuffer() > 2) {
                    this.fail("tick=" + ticks, "delta=" + speed, "min=-0.3","max=0.150000006");
                }
            }
            this.reduceBuffer(0.05);
        }
    }

}
