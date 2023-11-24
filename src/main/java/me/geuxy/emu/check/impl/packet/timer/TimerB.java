package me.geuxy.emu.check.impl.packet.timer;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.api.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;
import me.geuxy.emu.utils.MathUtil;
import org.bukkit.Location;
import org.bukkit.util.Vector;

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
        if(packet.isMove()) {
            long currentTime = System.currentTimeMillis();
            long diffTime = currentTime - lastTime;

            boolean exempt =
                data.TELEPORTED ||
                data.LIVING;

            if(exempt && lastTime != 0) {
                return;
            }

            ticks++;

            if(diffTime > 47L) {
                fiftys++;
            }

            if(ticks >= 20) {
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
