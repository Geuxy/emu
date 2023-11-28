package me.geuxy.emu.processors;

import io.github.retrooper.packetevents.packetwrappers.play.in.blockdig.WrappedPacketInBlockDig;
import io.github.retrooper.packetevents.packetwrappers.play.in.clientcommand.WrappedPacketInClientCommand;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.packetwrappers.play.in.transaction.WrappedPacketInTransaction;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityvelocity.WrappedPacketOutEntityVelocity;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;

public class PacketProcessor {

    public void handleReceive(PlayerData data, Packet packet) {
        if(packet.isFlying()) {
            data.getPositionProcessor().handle(new WrappedPacketInFlying(packet.getRaw()));
            data.getPositionProcessor().handleFlying();
            data.getVelocityProcessor().handleFlying();
            data.getActionProcessor().handleFlying();
            data.getCombatProcessor().handleFlying();

        } else if(packet.isTransaction()) {
            data.getVelocityProcessor().handleTransaction(new WrappedPacketInTransaction(packet.getRaw()));

        } else if(packet.isUseEntity()) {
            data.getCombatProcessor().handleUseEntity(new WrappedPacketInUseEntity(packet.getRaw()));

        } else if(packet.isClientCommand()) {
            data.getActionProcessor().handleClientCommand(new WrappedPacketInClientCommand(packet.getRaw()));

        } else if(packet.isSteerVehicle()) {
            data.getActionProcessor().handleSteerVehicle();

        } else if(packet.isBlockPlace()) {
            data.getActionProcessor().handleBlockPlace();

        } else if(packet.isBlockDig()) {
            data.getActionProcessor().handleBlockDig(new WrappedPacketInBlockDig(packet.getRaw()));
        }

        data.getChecks().forEach(c -> c.processPacket(packet));
    }

    public void handleSend(PlayerData data, Packet packet) {
        if(packet.isVelocity()) {
            WrappedPacketOutEntityVelocity wrapper = new WrappedPacketOutEntityVelocity(packet.getRaw());

            if(wrapper.getEntity() == data.getPlayer()) {
                data.getVelocityProcessor().handle(wrapper.getVelocityX(), wrapper.getVelocityY(), wrapper.getVelocityZ());
            }

        } else if(packet.isExplosion()) {
            data.getVelocityProcessor().handleExplosion();
        }

        data.getChecks().forEach(c -> c.processPacket(packet));
    }

}
