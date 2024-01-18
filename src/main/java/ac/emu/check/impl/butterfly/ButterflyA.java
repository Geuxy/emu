package ac.emu.check.impl.butterfly;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.profile.EmuPlayer;
import ac.emu.packet.Packet;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;

@CheckInfo(name = "Butterfly", description = "Double clicked", type = "A")
public class ButterflyA extends Check {

    private int attacks;

    public ButterflyA(EmuPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(packet.getType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity wrapper = new WrapperPlayClientInteractEntity((PacketReceiveEvent) packet.getEvent());

            if(wrapper.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                this.attacks++;
            }
        }

        if(packet.isMovement()) {
            if(attacks > 1) {
                this.fail(String.format("attacks=%d", attacks));

            } else {
                this.reward();
            }
            this.attacks = 0;
        }
    }

}
