package ac.emu.check.impl.protocol;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.packet.Packet;
import ac.emu.data.profile.EmuPlayer;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;

import java.util.stream.Stream;

@CheckInfo(name = "Protocol", description = "Invalid block place", type = "G")
public class ProtocolG extends Check {

    public ProtocolG(EmuPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(packet.getType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {
            WrapperPlayClientPlayerBlockPlacement wrapper = new WrapperPlayClientPlayerBlockPlacement((PacketReceiveEvent) packet.getEvent());

            if(profile.isOnOlderVersionThan(ClientVersion.V_1_8)) {
                return;
            }

            Vector3i pos = wrapper.getBlockPosition();

            double yDiff = profile.getMovementData().getY() - pos.y;

            boolean invalid = Stream.of(pos.x, pos.y, pos.z).anyMatch(a -> a > 1 || a < 0) && wrapper.getFace() == BlockFace.DOWN && yDiff >= 1;

            if(invalid) {
                this.fail(String.format("x=%d, y=%d, z=%d, ydiff=%.2f", pos.x, pos.y, pos.z, yDiff));

            } else {
                this.reward();
            }
        }
    }

}
