package me.geuxy.emu.data.processors;

import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;
import me.geuxy.emu.utils.entity.BoundingBox;
import me.geuxy.emu.utils.math.MathUtil;
import me.geuxy.emu.utils.world.BlockUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.NumberConversions;

@Getter @RequiredArgsConstructor
public class PositionProcessor {

    private final PlayerData data;

    private float yaw, pitch, lastYaw, lastPitch, deltaYaw, deltaPitch, lastDeltaYaw, lastDeltaPitch;

    private double x, y, z, lastX, lastY, lastZ, deltaX, deltaY, deltaZ, lastDeltaX, lastDeltaY, lastDeltaZ, speed, lastSpeed;
    private boolean clientGround, lastClientGround;

    private int groundTicks, airTicks, climbTicks, lastGroundTicks, lastAirTicks, lastClimbTicks, outVehicleTicks;

    private Location lastLocation;

    public void handle(WrappedPacketInFlying packet) {
        boolean position = packet.isPosition();
        boolean look = packet.isLook();

        this.lastYaw = yaw;
        this.lastPitch = pitch;
        this.yaw = look ? packet.getYaw() : yaw;
        this.pitch = look ? packet.getPitch() : pitch;

        this.lastDeltaYaw = deltaYaw;
        this.lastDeltaPitch = deltaPitch;
        this.deltaYaw = Math.abs(yaw - lastYaw);
        this.deltaPitch = Math.abs(pitch - lastPitch);

        this.lastX = x;
        this.lastY = y;
        this.lastZ = z;

        this.x = position ? packet.getX() : x;
        this.y = position ? packet.getY() : y;
        this.z = position ? packet.getZ() : z;

        this.lastDeltaX = deltaX;
        this.lastDeltaY = deltaY;
        this.lastDeltaZ = deltaZ;

        this.deltaX = Math.abs(x - lastX);
        this.deltaY = y - lastY;
        this.deltaZ = Math.abs(z - lastZ);

        this.lastSpeed = speed;

        this.speed = MathUtil.hypot(deltaX, deltaZ);

        this.lastClientGround = clientGround;
        this.clientGround = packet.isOnGround();

        this.lastLocation = new Location(data.getPlayer().getWorld(), lastX, lastY, lastZ, lastYaw, lastPitch);
    }

    public void onTick() {
        this.lastGroundTicks = groundTicks;
        this.lastAirTicks = airTicks;

        if(clientGround) {
            this.groundTicks = Math.min(100, groundTicks + 1);
            this.airTicks = 0;
        } else {
            this.airTicks = Math.min(100, airTicks + 1);
            this.groundTicks = 0;
        }

        this.lastClimbTicks = climbTicks;

        this.climbTicks = data.CLIMBABLE ? Math.min(10, climbTicks + 1) : 0;
        this.outVehicleTicks = data.getPlayer().isInsideVehicle() ? 0 : Math.min(10, outVehicleTicks + 1);
    }

}
