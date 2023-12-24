package ac.emu.data.impl;

import ac.emu.data.Data;
import ac.emu.data.PlayerData;
import ac.emu.packet.Packet;
import ac.emu.utils.MathUtil;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClientStatus;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;

import lombok.Getter;

import ac.emu.utils.LimitedList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public class ActionData extends Data {

    private final LimitedList<Long> flyingTimes = new LimitedList<>(50);

    private int teleportTicks, livingTicks, placeTicks;

    private boolean insideVehicle, lagging, blocking;

    private long ping, flying, lastFlying, deltaFlying;

    private double deviation;

    public ActionData(PlayerData data) {
        super(data);
    }

    public void handleTeleport() {
        this.teleportTicks = 0;
    }

    public void handleSteerVehicle() {
        this.insideVehicle = true;
    }

    @Override
    public void handle(Packet packet) {
        if(packet.isMovement()) {
            this.teleportTicks++;
            this.livingTicks++;
            this.placeTicks++;
            this.ping = PacketEvents.getAPI().getPlayerManager().getPing(data.getPlayer());
            this.insideVehicle = false;
            this.blocking = false;

            long currentTime = System.currentTimeMillis();

            long delay = currentTime - flying;

            if(delay > 0) {
                flyingTimes.add(delay);
            }

            if(flyingTimes.isFull()) {
                this.deviation = MathUtil.getVarianceSq(flyingTimes);
                this.lagging = deviation > 200;
            }

            this.lastFlying = flying;
            this.flying = currentTime;
            this.deltaFlying = flying - lastFlying;
        }

        if(packet.isClientStatus()) {
            WrapperPlayClientClientStatus wrapper = new WrapperPlayClientClientStatus((PacketReceiveEvent) packet.getEvent());

            if(wrapper.getAction() == WrapperPlayClientClientStatus.Action.PERFORM_RESPAWN) {
                this.livingTicks = 0;
            }
        }

        if(packet.isPlayerBlockPlacement()) {
            ItemStack item = data.getPlayer().getItemInHand();

            if(item != null && item.getType().isBlock() && !item.getType().equals(Material.AIR)) {
                this.placeTicks = 0;
            }
        }

        if(packet.isPlayerDigging()) {
            WrapperPlayClientPlayerDigging wrapper = new WrapperPlayClientPlayerDigging((PacketReceiveEvent) packet.getEvent());

            switch(wrapper.getAction()) {
                case RELEASE_USE_ITEM:
                this.blocking = true;
                break;
            }
        }
    }

}
