package me.geuxy.emu.data.processors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;

@Getter @RequiredArgsConstructor
public class RotationProcessor {

    private final PlayerData data;

    private float yaw, pitch, lastYaw, lastPitch, deltaYaw, deltaPitch, lastDeltaYaw, lastDeltaPitch;

    public void handle(Packet packet) {
        this.lastYaw = yaw;
        this.lastPitch = pitch;
        this.yaw = packet.getRaw().getFloat().read(0);
        this.pitch = packet.getRaw().getFloat().read(1);

        this.lastDeltaYaw = deltaYaw;
        this.lastDeltaPitch = deltaPitch;
        this.deltaYaw = Math.abs(yaw - lastYaw);
        this.deltaPitch = Math.abs(pitch - lastPitch);
    }

}
