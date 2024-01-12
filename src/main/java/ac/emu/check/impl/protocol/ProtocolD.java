package ac.emu.check.impl.protocol;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.user.EmuPlayer;
import ac.emu.packet.Packet;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;

@CheckInfo(name = "Protocol", description = "Invalid held item change", type = "D")
public class ProtocolD extends Check {

    private int currentSlot = -1;

    public ProtocolD(EmuPlayer data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.getType() == PacketType.Play.Client.HELD_ITEM_CHANGE) {
            WrapperPlayClientHeldItemChange wrapper = new WrapperPlayClientHeldItemChange((PacketReceiveEvent) packet.getEvent());

            int slot = wrapper.getSlot();

            if(slot == currentSlot) {
                this.fail("slot=" + currentSlot);
            } else {
                this.reward();
            }

            this.currentSlot = slot;
        }
    }

}
