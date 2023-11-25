package me.geuxy.emu.data.processors;

import io.github.retrooper.packetevents.PacketEvents;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import me.geuxy.emu.data.PlayerData;

@Getter @RequiredArgsConstructor
public class ActionProcessor {

    private final PlayerData data;

    private int teleportTicks, livingTicks;

    private long ping;

    public void handleRespawn() {
        this.livingTicks = 0;
    }

    public void handleTeleport() {
        this.teleportTicks = 0;
    }

    public void handleFlying() {
        if(teleportTicks < 10) {
            this.teleportTicks++;
        }

        if(livingTicks < 10) {
            this.livingTicks++;
        }

        this.ping = PacketEvents.get().getPlayerUtils().getPing(data.getPlayer());
    }

}
