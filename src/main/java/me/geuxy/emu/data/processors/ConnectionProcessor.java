package me.geuxy.emu.data.processors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;

@Getter @RequiredArgsConstructor
public class ConnectionProcessor {

    private final PlayerData data;

    private long clientTans, serverTrans, difference;

    public void handleClient(Packet packet) {
        this.clientTans = System.currentTimeMillis();
    }

    public void handleServer(Packet packet) {
        this.serverTrans = System.currentTimeMillis();
    }

}
