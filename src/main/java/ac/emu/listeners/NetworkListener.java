package ac.emu.listeners;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;

import ac.emu.Emu;
import ac.emu.data.profile.EmuPlayer;
import ac.emu.packet.Packet;

public class NetworkListener extends PacketListenerAbstract {

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        EmuPlayer data = Emu.INSTANCE.getDataManager().get(event.getUser().getUUID());

        if(data != null) {
            data.handle(new Packet(event));
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        EmuPlayer data = Emu.INSTANCE.getDataManager().get(event.getUser().getUUID());

        if(data != null) {
            data.handle(new Packet(event));
        }
    }

}
