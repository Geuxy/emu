package me.geuxy.emu.data.processors;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.transaction.WrappedPacketInTransaction;
import io.github.retrooper.packetevents.packetwrappers.play.out.transaction.WrappedPacketOutTransaction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;
import me.geuxy.emu.utils.math.MathUtil;

import me.geuxy.emu.utils.entity.Velocity;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Getter @RequiredArgsConstructor
public class VelocityProcessor {

    private final PlayerData data;

    private double x, y, z, speed, lastX, lastY, lastZ, lastSpeed;
    private int velocityTicks, explosionTicks;
    private short velocityID;

    private final Map<Short, Vector> pending = new HashMap<>();

    private final Velocity transVelocity = new Velocity();

    public void handle(double x, double y, double z) {
        this.lastX = x;
        this.lastY = y;
        this.lastZ = z;

        this.x = x;
        this.y = y;
        this.z = z;

        this.lastSpeed = speed;
        this.speed = MathUtil.hypot(x, z);

        this.velocityID = (short) ThreadLocalRandom.current().nextInt(Short.MAX_VALUE);

        this.velocityTicks = 0;

        WrappedPacketOutTransaction transaction = new WrappedPacketOutTransaction(0, velocityID, false);
        PacketEvents.get().getPlayerUtils().sendPacket(data.getPlayer(), transaction);
        //ProtocolLibrary.getProtocolManager().sendServerPacket(data.getPlayer(), transaction);
        pending.put(velocityID, new Vector(x, y, z));
    }

    public void handleTransaction(WrappedPacketInTransaction packet) {
        pending.computeIfPresent(packet.getActionNumber(), (id, vector) -> {
            transVelocity.setX(vector.getX());
            transVelocity.setY(vector.getY());
            transVelocity.setZ(vector.getZ());

            transVelocity.setIndex(transVelocity.getIndex() + 1);
            pending.remove(packet.getActionNumber());

            return vector;
        });
    }

    public void handleExplosion() {
        this.velocityTicks = 0;
        this.explosionTicks = 0;
    }

    public void handleFlying() {
        if(velocityTicks < 10) {
            this.velocityTicks++;
        }

        if(explosionTicks < 10) {
            this.explosionTicks++;
        }
    }

}
