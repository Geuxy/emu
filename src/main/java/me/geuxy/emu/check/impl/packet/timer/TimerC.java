package me.geuxy.emu.check.impl.packet.timer;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.exempt.ExemptType;
import me.geuxy.emu.packet.Packet;
import me.geuxy.emu.utils.LimitedList;
import me.geuxy.emu.utils.math.MathUtil;

@CheckInfo(
    name = "Timer",
    description = "Too many position packets",
    type = "C"
)
public class TimerC extends AbstractCheck {

    public TimerC(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isFlying()) {
            LimitedList<Long> flyingTimes = data.getActionProcessor().getFlyingTimes();

            if(flyingTimes.isFull()) {
                double speed = 50 / MathUtil.getAverage(flyingTimes);

                boolean exempt = isExempt(
                    ExemptType.SPAWNED,
                    ExemptType.LAGGING,
                    ExemptType.TELEPORTED
                );

                boolean invalid = speed >= 1.06;

                if(data.getPositionProcessor().getOutVehicleTicks() < 20) {
                    this.resetBuffer();
                }

                if(invalid && !exempt) {
                    if(thriveBuffer() > 6) {
                        this.fail("speed=" + speed);
                    }
                }
                this.decayBuffer(0.02);
            }
        }
    }

}
