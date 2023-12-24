package ac.emu.packet;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.event.ProtocolPacketEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;

import lombok.Getter;

@Getter
public class Packet {

    protected final PacketEvent event;
    protected final ProtocolPacketEvent packet;

    public Packet(PacketEvent event) {
        this.event = event;
        this.packet = (ProtocolPacketEvent) event;
    }

    public boolean isMovement() {
        return packet.getPacketType() == PacketType.Play.Client.PLAYER_POSITION
            || packet.getPacketType() ==  PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION
            || packet.getPacketType() == PacketType.Play.Client.PLAYER_ROTATION
            || packet.getPacketType() == PacketType.Play.Client.PLAYER_FLYING;
    }

    public boolean isPosition() {
        return packet.getPacketType() == PacketType.Play.Client.PLAYER_POSITION
            || packet.getPacketType() ==  PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION;
    }

    public boolean isRotation() {
        return packet.getPacketType() == PacketType.Play.Client.PLAYER_ROTATION
            || packet.getPacketType() ==  PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION;
    }

    public boolean isEntityVelocity() {
        return packet.getPacketType() == PacketType.Play.Server.ENTITY_VELOCITY;
    }

    public boolean isExplosion() {
        return packet.getPacketType() == PacketType.Play.Server.EXPLOSION;
    }

    public boolean isInteractEntity() {
        return packet.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY;
    }

    public boolean isClientStatus() {
        return packet.getPacketType() == PacketType.Play.Client.CLIENT_STATUS;
    }

    public boolean isSteerVehicle() {
        return packet.getPacketType() == PacketType.Play.Client.STEER_VEHICLE;
    }

    public boolean isPlayerDigging() {
        return packet.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING;
    }

    public boolean isPlayerBlockPlacement() {
        return packet.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT;
    }

    public boolean isHeldItemChange() {
        return packet.getPacketType() == PacketType.Play.Client.HELD_ITEM_CHANGE;
    }

    public boolean isOutTransaction() {
        return packet.getPacketType() == PacketType.Play.Server.WINDOW_CONFIRMATION;
    }

    public boolean isInTransaction() {
        return packet.getPacketType() == PacketType.Play.Client.WINDOW_CONFIRMATION;
    }

}
