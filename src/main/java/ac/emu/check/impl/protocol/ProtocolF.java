package ac.emu.check.impl.protocol;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.packet.Packet;
import ac.emu.user.EmuPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;

@CheckInfo(name = "Protocol", description = "Interacted with themself", type = "F")
public class ProtocolF extends Check {

    public ProtocolF(EmuPlayer data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.getType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity wrapper = new WrapperPlayClientInteractEntity((PacketReceiveEvent) packet.getEvent());

            if(wrapper.getEntityId() == data.getPlayer().getEntityId()) {
                this.fail();
            } else {
                this.reward();
            }
        }
    }

}
