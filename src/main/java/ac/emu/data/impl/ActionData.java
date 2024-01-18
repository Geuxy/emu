package ac.emu.data.impl;

import ac.emu.data.Data;
import ac.emu.data.profile.EmuPlayer;
import ac.emu.packet.Packet;
import ac.emu.utils.math.MathUtil;
import ac.emu.utils.type.LimitedList;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.*;

import lombok.Getter;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public class ActionData extends Data {

    private final LimitedList<Long> flyingTimes = new LimitedList<>(50);

    private int teleportTicks, livingTicks, placeTicks;

    private boolean insideVehicle, lagging, lastLagging, blocking;

    private long ping, flying, lastFlying, deltaFlying;

    private double deviation;

    public ActionData(EmuPlayer data) {
        super(data);
    }

    public void handleTeleport() {
        this.teleportTicks = 0;
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
                this.lastLagging = lagging;
                this.lagging = deviation > 200;
            }

            this.lastFlying = flying;
            this.flying = currentTime;
            this.deltaFlying = flying - lastFlying;
        }

        if(packet.getType() == PacketType.Play.Client.CLIENT_STATUS) {
            WrapperPlayClientClientStatus wrapper = new WrapperPlayClientClientStatus((PacketReceiveEvent) packet.getEvent());

            if(wrapper.getAction() == WrapperPlayClientClientStatus.Action.PERFORM_RESPAWN) {
                this.livingTicks = 0;
            }
        }

        if(packet.getType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {
            ItemStack item = data.getPlayer().getItemInHand();

            if(item != null && item.getType().isBlock() && !item.getType().equals(Material.AIR)) {
                this.placeTicks = 0;
            }
        }

        if(packet.getType() == PacketType.Play.Client.PLAYER_DIGGING) {
            WrapperPlayClientPlayerDigging wrapper = new WrapperPlayClientPlayerDigging((PacketReceiveEvent) packet.getEvent());

            switch(wrapper.getAction()) {
            case RELEASE_USE_ITEM:
                this.blocking = true;
                break;
            }
        }

        if(packet.getType() == PacketType.Play.Client.STEER_VEHICLE) {
            this.insideVehicle = true;
        }
    }

}
