package ac.emu.check.impl.killaura;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.packet.Packet;
import ac.emu.user.EmuPlayer;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;

@CheckInfo(name = "Killaura", description = "Attacked multiple entities in one tick", type = "A")
public class KillauraA extends Check {

    private int attacks;

    public KillauraA(EmuPlayer data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
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
