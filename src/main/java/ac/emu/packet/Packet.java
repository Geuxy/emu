package ac.emu.packet;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.event.ProtocolPacketEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Client;

import lombok.Getter;

@Getter
public class Packet {

    private final PacketEvent event;
    private final PacketTypeCommon type;

    public Packet(PacketEvent event) {
        this.event = event;
        this.type = ((ProtocolPacketEvent<?>) event).getPacketType();
    }

    public boolean isMovement() {
        return type == Client.PLAYER_POSITION || type == Client.PLAYER_POSITION_AND_ROTATION || type == Client.PLAYER_ROTATION || type == Client.PLAYER_FLYING;
    }

    public boolean isPosition() {
        return type == Client.PLAYER_POSITION || type == Client.PLAYER_POSITION_AND_ROTATION;
    }

    public boolean isRotation() {
        return type == Client.PLAYER_ROTATION || type == Client.PLAYER_POSITION_AND_ROTATION;
    }

}
