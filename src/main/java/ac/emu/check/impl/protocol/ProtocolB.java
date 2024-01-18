package ac.emu.check.impl.protocol;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.profile.EmuPlayer;
import ac.emu.packet.Packet;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;

@CheckInfo(name = "Protocol", description = "Placed and dug on the same tick", type = "B")
public class ProtocolB extends Check {

    private boolean place, dig;

    public ProtocolB(EmuPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(packet.getType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {
            this.place = true;
        }

        if(packet.getType() == PacketType.Play.Client.PLAYER_DIGGING) {
            this.dig = true;
        }

        if(packet.isMovement()) {
            if(place && dig) {
                this.fail();

            } else {
                this.reward();
            }

            this.place = false;
            this.dig = false;
        }
    }

}
