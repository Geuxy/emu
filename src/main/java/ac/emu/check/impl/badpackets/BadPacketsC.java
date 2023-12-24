package ac.emu.check.impl.badpackets;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.PlayerData;
import ac.emu.packet.Packet;

import java.util.concurrent.TimeUnit;

@CheckInfo(name = "Bad Packets", description = "Switching slots too quickly", type = "C")
public class BadPacketsC extends Check {

    private long lastHeldItem;
    private int changes;

    public BadPacketsC(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isHeldItemChange()) {
            long currentTime = System.nanoTime();
            long delta = TimeUnit.NANOSECONDS.toMillis(currentTime - lastHeldItem);

            this.changes++;

            if(delta < 30) {
                this.fail(String.format("changes=%d, delta=%d", changes, delta));
            }

            this.lastHeldItem = currentTime;

        }

        if(packet.isMovement()) {
            this.changes = 0;
        }
    }

}
