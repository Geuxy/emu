package me.geuxy.emu.listeners;

import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.event.PacketListenerAbstract;

import me.geuxy.emu.Emu;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;

public class NetworkListener extends PacketListenerAbstract {

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        PlayerData data = Emu.INSTANCE.getDataManager().get(event.getPlayer());
        Packet packet = new Packet(event.getNMSPacket(), event.getPacketId());

        if(data != null) {
            Emu.INSTANCE.getPacketProcessor().handleReceive(data, packet);
        }
    }

    @Override
    public void onPacketPlaySend(PacketPlaySendEvent event) {
        PlayerData data = Emu.INSTANCE.getDataManager().get(event.getPlayer());
        Packet packet = new Packet(event.getNMSPacket(), event.getPacketId());

        if(data != null) {
            Emu.INSTANCE.getPacketProcessor().handleSend(data, packet);
        }
    }


}
