package ac.emu.check.impl.protocol;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.packet.Packet;
import ac.emu.user.EmuPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;

@CheckInfo(name = "Protocol", description = "No Swing", type = "H")
public class ProtocolH extends Check {

    private int noSwings;

    private boolean reward;

    public ProtocolH(EmuPlayer data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.getType() == PacketType.Play.Client.ANIMATION) {
            this.noSwings = 0;
            this.reward = true;

        } else if(packet.getType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity wrapper = new WrapperPlayClientInteractEntity((PacketReceiveEvent) packet.getEvent());

            if(wrapper.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                if(++noSwings > 2) {
                    this.fail();
                } else {
                    if(reward) {
                        this.reward();
                    }
                }

                this.reward = false;
            }
        }
    }

}
