package ac.emu.check.impl.killaura;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.profile.EmuPlayer;
import ac.emu.packet.Packet;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;

@CheckInfo(name = "Killaura", description = "Invalid attack", type = "B")
public class KillauraB extends Check {

    private boolean swingAnim;

    public KillauraB(EmuPlayer profile) {
        super(profile);
    }

    @Override
    public void handle(Packet packet) {
        if(packet.getType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity wrapper = new WrapperPlayClientInteractEntity((PacketReceiveEvent) packet.getEvent());

            boolean invalid = wrapper.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK && !swingAnim;

            if(invalid) {
                this.fail();

            } else {
                this.reward();
            }
        }

        if(packet.isMovement()) {
            this.swingAnim = false;
        }

        if(packet.getType() == PacketType.Play.Client.ANIMATION) {
            this.swingAnim = true;
        }
    }

}
