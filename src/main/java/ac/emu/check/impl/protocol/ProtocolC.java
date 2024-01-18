package ac.emu.check.impl.protocol;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.profile.EmuPlayer;
import ac.emu.packet.Packet;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;

@CheckInfo(name = "Protocol", description = "Switching slots too quickly", type = "C")
public class ProtocolC extends Check {

    private long lastHeldItem;
    private int changes;

    public ProtocolC(EmuPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(packet.getType() == PacketType.Play.Client.HELD_ITEM_CHANGE) {
            long currentTime = System.currentTimeMillis();
            long delta = currentTime - lastHeldItem;

            this.changes++;

            if(delta < 30) {
                this.fail(String.format("changes=%d, delta=%d", changes, delta));

            } else {
                this.reward();
            }

            this.lastHeldItem = currentTime;

        }

        if(packet.isMovement()) {
            this.changes = 0;
        }
    }

}
