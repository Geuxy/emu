package me.geuxy.emu.check.impl.packet.timer;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;

import java.util.concurrent.TimeUnit;

@CheckInfo(
    name = "Timer",
    description = "Too many position packets",
    type = "A"
)

// Balance check from FunkeMunky's fork of DemonDxv Anticheat-Open-Source repository:
// https://github.com/funkemunky/Anticheat-Open-Source/blob/main/src/main/java/me/rhys/anticheat/checks/misc/timer/TimerA.java
public class TimerA extends AbstractCheck {

    private double balance = -100L;
    private long lastTime;

    public TimerA(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isFlying()) {
            boolean exempt =
                data.TELEPORTED ||
                data.LIVING;

            long currentTime = System.nanoTime();
            long lastTime = this.lastTime != 0 ? this.lastTime : currentTime - 50L;
            long balanceRate = currentTime - lastTime;

            if(exempt) {
                return;
            }

            this.balance += TimeUnit.MILLISECONDS.toNanos(50L) - balanceRate;

            boolean invalid = balance > TimeUnit.MILLISECONDS.toNanos(46L);

            if(invalid) {
                if(increaseBuffer() > 2) {
                    this.fail("balance=" + balance, "rate=" + balanceRate);
                }
                this.balance = 0;
            } else {
                reduceBuffer(0.01D);
            }
            this.lastTime = currentTime;
        }
    }

}
