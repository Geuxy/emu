package me.geuxy.emu.data.processors;

import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import me.geuxy.emu.data.PlayerData;

import org.bukkit.entity.Entity;

@Getter @RequiredArgsConstructor
public class CombatProcessor {

    private final PlayerData data;

    private Entity hitEntity, lastHitEntity;

    private int hitTicks;

    public void handleUseEntity(WrappedPacketInUseEntity wrapper) {
        if(wrapper.getAction() != WrappedPacketInUseEntity.EntityUseAction.ATTACK || wrapper.getEntity() == null) {
            return;
        }

        this.lastHitEntity = hitEntity;
        this.hitEntity = wrapper.getEntity();
        this.hitTicks = 0;
    }

    public void handleFlying() {
        hitTicks = Math.min(10, hitTicks + 1);
    }

}
