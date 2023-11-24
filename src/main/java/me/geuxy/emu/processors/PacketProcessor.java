package me.geuxy.emu.processors;

import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;

public class PacketProcessor {

    public void handleReceive(PlayerData data, Packet packet) {
        if(packet.isPosition()) {
            data.handlePosition(packet);

        } else if(packet.isFlying()) {
                data.getPositionProcessor().handleFlying(packet);

        } else if(packet.isPositionLook()) {
            data.handlePositionLook(packet);

        } else if(packet.isTransaction()) {
            data.handleTransaction(packet);
        }

        data.getChecks().forEach(c -> c.processPacket(packet));
    }

    public void handleSend(PlayerData data, Packet packet) {
        if(packet.isVelocity()) {
            int id = packet.getRaw().getIntegers().read(0);

            if(id == data.getPlayer().getEntityId()) {
                double velX = (double) packet.getRaw().getIntegers().read(1) / 8000;
                double velY = (double) packet.getRaw().getIntegers().read(2) / 8000;
                double velZ = (double) packet.getRaw().getIntegers().read(3) / 8000;

                data.handleVelocity(velX, velY, velZ);
            }
        } else if(packet.isExplosion()) {
            data.handleExplosion();
        }

        data.getChecks().forEach(c -> c.processPacket(packet));
    }
}
