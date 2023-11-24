package me.geuxy.emu.listeners;

import com.comphenix.protocol.PacketType.Play;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import me.geuxy.emu.Emu;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;
import org.bukkit.plugin.java.JavaPlugin;

public class NetworkListener extends PacketAdapter {

    public NetworkListener(JavaPlugin plugin) {
        super(plugin,

            // Client
            Play.Client.POSITION,
            Play.Client.POSITION_LOOK,
            Play.Client.LOOK,
            Play.Client.USE_ENTITY,
            Play.Client.FLYING,

            // Server
            Play.Server.ENTITY_VELOCITY,
            Play.Server.EXPLOSION
        );
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        PlayerData data = Emu.INSTANCE.getDataManager().get(event.getPlayer());
        Packet packet = new Packet(event.getPacket());

        Emu.INSTANCE.getPacketProcessor().handleReceive(data, packet);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PlayerData data = Emu.INSTANCE.getDataManager().get(event.getPlayer());
        Packet packet = new Packet(event.getPacket());

        Emu.INSTANCE.getPacketProcessor().handleSend(data, packet);
    }

}
