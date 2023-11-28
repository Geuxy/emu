package me.geuxy.emu.check.impl.packet.timer;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.exempt.ExemptType;
import me.geuxy.emu.packet.Packet;

@CheckInfo(
    name = "Timer",
    description = "Too many position packets",
    type = "B"
)

public class TimerB extends AbstractCheck {

    private long lastTime;

    private int fiftys, ticks;

    public TimerB(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isFlying()) {
            long currentTime = System.currentTimeMillis();
            long diffTime = currentTime - lastTime;

            boolean exempt = isExempt(
                ExemptType.TELEPORTED,
                ExemptType.SPAWNED,
                ExemptType.LAGGING
            );

            if(exempt && lastTime != 0) {
                return;
            }

            ticks++;

            if(diffTime >= 49L - (int) Math.min(3, data.getActionProcessor().getPing() / 100)) {
                fiftys++;
            }

            if(ticks >= 10) {
                boolean invalid = (ticks - fiftys) > fiftys;

                if(invalid) {
                    this.fail("50s=" + fiftys, "ticks=" + ticks, "diff=" + diffTime);
                }

                this.fiftys = 0;
                this.ticks = 0;
            }
            this.lastTime = currentTime;
        }
    }

}
