package ac.emu.check.impl.protocol;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.user.EmuPlayer;
import ac.emu.packet.Packet;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;

@CheckInfo(name = "Protocol", description = "Invalid sprint", type = "E")
public class ProtocolE extends Check {

    private boolean start, stop;

    public ProtocolE(EmuPlayer data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.getType() == PacketType.Play.Client.ENTITY_ACTION) {
            WrapperPlayClientEntityAction wrapper = new WrapperPlayClientEntityAction((PacketReceiveEvent) packet.getEvent());

            switch(wrapper.getAction()) {
            case START_SPRINTING:
                this.start = true;
                break;

            case STOP_SPRINTING:
                this.stop = true;
                break;
            }
        }

        if(packet.isMovement()) {
            if(start && stop) {
                this.fail();
            } else {
                this.reward();
            }

            this.start = false;
            this.stop = false;
        }
    }

}
