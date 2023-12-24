package ac.emu.check.impl.timer;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.PlayerData;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;
import ac.emu.utils.LimitedList;

import java.util.concurrent.TimeUnit;

@CheckInfo(name = "Timer", description = "Too many position packets", type = "B")
public class TimerB extends Check {

    private long lastTime;

    private int fiftys, ticks;

    public TimerB(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isPosition()) {
            long currentTime = System.nanoTime();
            long diffTime = TimeUnit.NANOSECONDS.toMillis(currentTime - lastTime);

            boolean exempt = isExempt(
                ExemptType.TELEPORTED,
                ExemptType.SPAWNED,
                ExemptType.LAGGING
            );

            if(exempt && lastTime != 0) {
                return;
            }

            ticks++;

            if(diffTime >= 49 - (int) Math.min(3, data.getActionData().getPing() / 100)) {
                fiftys++;
            }

            if(ticks >= 10) {
                boolean invalid = (ticks - fiftys) >= fiftys;

                if(invalid) {
                    if(thriveBuffer() > 1) {
                        this.fail(String.format("legits=%d/10, diff=%d", fiftys, diffTime));
                    }
                } else {
                    this.decayBuffer(0.1);
                }

                this.fiftys = 0;
                this.ticks = 0;
            }

            this.lastTime = currentTime;
        }
    }

}
