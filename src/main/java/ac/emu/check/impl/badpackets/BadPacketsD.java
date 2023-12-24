package ac.emu.check.impl.badpackets;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.PlayerData;
import ac.emu.packet.Packet;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;

@CheckInfo(name = "Bad Packets", description = "Invalid held item change", type = "C")
public class BadPacketsD extends Check {

    private int currentSlot = -1;

    public BadPacketsD(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isHeldItemChange()) {
            WrapperPlayClientHeldItemChange wrapper = new WrapperPlayClientHeldItemChange((PacketReceiveEvent) packet.getEvent());

            int slot = wrapper.getSlot();

            if(slot == currentSlot) {
                this.fail("slot=" + currentSlot);
            }

            this.currentSlot = slot;
        }
    }

}
