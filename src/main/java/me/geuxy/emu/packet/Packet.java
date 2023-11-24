package me.geuxy.emu.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.PacketType.Play;
import com.comphenix.protocol.events.PacketContainer;

import lombok.Getter;

@Getter
public class Packet {

    protected final PacketContainer raw;
    protected final PacketType type;

    public Packet(PacketContainer raw) {
        this.raw = raw;
        this.type = raw.getType();
    }

    public boolean isMove() {
        return type == Play.Client.POSITION ||
                type == Play.Client.POSITION_LOOK;
    }

    public boolean isPosition() {
        return type == Play.Client.POSITION;
    }

    public boolean isPositionLook() {
        return type == Play.Client.POSITION_LOOK;
    }

    public boolean isVelocity() {
        return type == Play.Server.ENTITY_VELOCITY;
    }

    public boolean isExplosion() {
        return type == Play.Server.EXPLOSION;
    }

    public boolean isUseEntity() {
        return type == Play.Client.USE_ENTITY;
    }

    public boolean isTransaction() {
        return type == Play.Client.TRANSACTION;
    }

    public boolean isFlying() {
        return type == Play.Client.FLYING;
    }

}
