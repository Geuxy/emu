package ac.emu.check.impl.protocol;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.packet.Packet;
import ac.emu.user.EmuPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;

@CheckInfo(name = "Protocol", description = "Invalid digging", type = "I")
public class ProtocolI extends Check {

    public ProtocolI(EmuPlayer data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.getType() == PacketType.Play.Client.PLAYER_DIGGING) {
            WrapperPlayClientPlayerDigging wrapper = new WrapperPlayClientPlayerDigging((PacketReceiveEvent) packet.getEvent());

            if(data.getUtilities().isOnOlderVersionThan(ClientVersion.V_1_8)) {
                return;
            }

            if(wrapper.getAction() == DiggingAction.RELEASE_USE_ITEM) {
                Vector3i pos = wrapper.getBlockPosition();

                boolean invalid = wrapper.getBlockFace() == BlockFace.DOWN || pos.getX() != 0 || pos.getY() != 0 || pos.getZ() != 0;

                if(invalid) {
                    this.fail();
                } else {
                    this.reward();
                }
            }
        }
    }

}
