package ac.emu.check.impl.timer;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.PlayerData;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;
import ac.emu.utils.LimitedList;
import ac.emu.utils.MathUtil;

import java.util.concurrent.TimeUnit;

@CheckInfo(name = "Timer", description = "Too many position packets", type = "A")
public class TimerA extends Check {

    private final LimitedList<Long> samples = new LimitedList<>(50);

    private long lastTime;

    public TimerA(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMovement()) {
            boolean exempt = isExempt(
                ExemptType.TELEPORTED,
                ExemptType.SPAWNED,
                ExemptType.LAGGING,
                ExemptType.SETBACK
            );

            if(exempt) {
                return;
            }

            long currentTime = System.nanoTime();
            long delta = currentTime - lastTime;

            this.samples.add(delta);

            if(samples.isFull()) {
                double average = MathUtil.getAverage(samples);
                boolean invalid = average < 49000000L - TimeUnit.MILLISECONDS.toNanos(Math.min(3, data.getActionData().getPing() / 100));

                double speed = 50 / (double) TimeUnit.NANOSECONDS.toMillis((long) average);

                if (invalid) {
                    if(thriveBuffer() > 12) {
                        this.fail(String.format("gamespeed=%.2f", speed));
                    }
                } else {
                    this.decayBuffer(0.1);
                }
            }
            this.lastTime = currentTime;

        }
    }

}
