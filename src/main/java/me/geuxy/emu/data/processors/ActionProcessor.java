package me.geuxy.emu.data.processors;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.play.in.blockdig.WrappedPacketInBlockDig;
import io.github.retrooper.packetevents.packetwrappers.play.in.clientcommand.WrappedPacketInClientCommand;

import io.github.retrooper.packetevents.packetwrappers.play.in.helditemslot.WrappedPacketInHeldItemSlot;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.utils.LimitedList;
import me.geuxy.emu.utils.math.MathUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter @RequiredArgsConstructor
public class ActionProcessor {

    private final PlayerData data;

    private final LimitedList<Long> flyingTimes = new LimitedList<>(50);

    private int teleportTicks, livingTicks, placeTicks;

    private boolean insideVehicle, lagging, blocking;

    private long ping, flying, lastFlying, deltaFlying;

    private double deviation;

    public void handleTeleport() {
        this.teleportTicks = 0;
    }

    public void handleSteerVehicle() {
        this.insideVehicle = false;
    }

    public void handleClientCommand(WrappedPacketInClientCommand wrapper) {
        if(wrapper.getClientCommand() == WrappedPacketInClientCommand.ClientCommand.PERFORM_RESPAWN) {
            this.livingTicks = 0;
        }
    }

    public void handleBlockPlace() {
        ItemStack item = data.getPlayer().getItemInHand();

        if(item != null && item.getType().isBlock() && !item.getType().equals(Material.AIR)) {
            this.placeTicks = 0;
        }
    }

    public void handleBlockDig(WrappedPacketInBlockDig wrapper) {
        switch(wrapper.getDigType()) {
            case RELEASE_USE_ITEM:
                this.blocking = true;
                break;
        }
    }

    public void handleFlying() {
        this.teleportTicks = Math.min(10, teleportTicks + 1);
        this.livingTicks = Math.min(15, livingTicks + 1);
        this.ping = PacketEvents.get().getPlayerUtils().getPing(data.getPlayer());
        this.insideVehicle = false;
        this.blocking = false;

        long currentTime = System.currentTimeMillis();

        long delay = currentTime - flying;

        if(delay > 0) {
            flyingTimes.add(delay);
        }

        if(flyingTimes.isFull()) {
            this.deviation = MathUtil.getVarianceSquared(flyingTimes);
            this.lagging = deviation > 200;
        }
        this.lastFlying = flying;
        this.flying = currentTime;
        this.deltaFlying = flying - lastFlying;

    }

}
