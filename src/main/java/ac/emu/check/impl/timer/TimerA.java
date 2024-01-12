package ac.emu.check.impl.timer;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.user.EmuPlayer;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;
import ac.emu.utils.LimitedList;
import ac.emu.utils.MathUtil;

import java.util.concurrent.TimeUnit;

@CheckInfo(name = "Timer", description = "Too many position packets", type = "A")
public class TimerA extends Check {

    private final LimitedList<Long> samples = new LimitedList<>(20);

    private long lastTime;

    public TimerA(EmuPlayer data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMovement()) {
            boolean exempt = isExempt(
                ExemptType.TELEPORTED,
                ExemptType.SPAWNED,
                ExemptType.LAGGING,
                ExemptType.SETBACK,
                ExemptType.NOT_MOVING
            );

            if(exempt) {
                return;
            }

            long currentTime = System.nanoTime();
            long delta = currentTime - lastTime;

            this.samples.add(delta);

            if(samples.isFull()) {
                double average = MathUtil.getAverage(samples);

                // TODO: make more strict
                boolean invalid = average < 49000000L - TimeUnit.MILLISECONDS.toNanos(Math.min(3, data.getActionData().getPing() / 100));
                boolean invalid2 = !isExempt(ExemptType.LAGGING, ExemptType.LAST_LAGGING) && delta < 3;

                double speed = 50 / (double) TimeUnit.NANOSECONDS.toMillis((long) average);

                if ((invalid || invalid2) || delta < 5) {
                    if(fail(String.format("gamespeed=%.2f", speed))) {
                        this.resetBuffer();
                    }
                } else {
                    this.reward();
                }
            }
            this.lastTime = currentTime;
        }
    }

}
