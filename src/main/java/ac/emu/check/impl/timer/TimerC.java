package ac.emu.check.impl.timer;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.PlayerData;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;
import ac.emu.utils.LimitedList;
import ac.emu.utils.MathUtil;

@CheckInfo(name = "Timer", description = "Too many position packets", type = "C")
public class TimerC extends Check {

    public TimerC(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMovement()) {
            LimitedList<Long> flyingTimes = data.getActionData().getFlyingTimes();

            if(flyingTimes.isFull()) {
                double speed = 50 / MathUtil.getAverage(flyingTimes);

                boolean exempt = isExempt(
                    ExemptType.SPAWNED,
                    ExemptType.LAGGING,
                    ExemptType.TELEPORTED
                );

                boolean invalid = speed >= 1.06;

                if(data.getMovementData().getSinceInVehicleTicks() < 20 || data.getSetbackData().getTicksSinceSetback() < 50) {
                    this.resetBuffer();
                }

                if(invalid && !exempt) {
                    if(thriveBuffer() > 6) {
                        this.fail("tps=" + speed * 20);
                    }
                } else {
                    this.decayBuffer(0.02);
                }
            }
        }
    }

}
