package ac.emu.check.impl.protocol;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.packet.Packet;
import ac.emu.user.EmuPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;

@CheckInfo(name = "Protocol", description = "Bad sprint", type = "G")
public class ProtocolG extends Check {

    private boolean sprinting;

    public ProtocolG(EmuPlayer data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.getType() == PacketType.Play.Client.ENTITY_ACTION) {
            WrapperPlayClientEntityAction wrapper = new WrapperPlayClientEntityAction((PacketReceiveEvent) packet.getEvent());

            if(wrapper.getAction() == WrapperPlayClientEntityAction.Action.START_SPRINTING) {
                if(sprinting) {
                    this.fail("sprinting=true");
                } else {
                    this.reward();
                }
                this.sprinting = false;

            } else if(wrapper.getAction() == WrapperPlayClientEntityAction.Action.STOP_SPRINTING) {
                if(!sprinting) {
                    this.fail("sprinting=false");
                } else {
                    this.reward();
                }
                this.sprinting = false;
            }

        }
    }

}
