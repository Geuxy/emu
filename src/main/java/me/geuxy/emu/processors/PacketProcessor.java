package me.geuxy.emu.processors;

import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.packetwrappers.play.in.transaction.WrappedPacketInTransaction;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityvelocity.WrappedPacketOutEntityVelocity;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;

public class PacketProcessor {

    public void handleReceive(PlayerData data, Packet packet) {
        if(packet.isFlying()) {
            data.handleFlying(new WrappedPacketInFlying(packet.getRaw()));

        } else if(packet.isTransaction()) {
            data.handleTransaction(new WrappedPacketInTransaction(packet.getRaw()));

        } else if(packet.isUseEntity()) {
            data.handleUseEntity(new WrappedPacketInUseEntity(packet.getRaw()));
        }

        data.getChecks().forEach(c -> c.processPacket(packet));
    }

    public void handleSend(PlayerData data, Packet packet) {
        if(packet.isVelocity()) {
            WrappedPacketOutEntityVelocity wrapper = new WrappedPacketOutEntityVelocity(packet.getRaw());

            if(wrapper.getEntity() == data.getPlayer()) {
                data.handleVelocity(wrapper.getVelocityX(), wrapper.getVelocityX(), wrapper.getVelocityZ());
            }

        } else if(packet.isExplosion()) {
            data.handleExplosion();
        }

        data.getChecks().forEach(c -> c.processPacket(packet));
    }
}
