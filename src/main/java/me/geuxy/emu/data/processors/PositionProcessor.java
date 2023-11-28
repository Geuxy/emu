package me.geuxy.emu.data.processors;

import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.exempt.ExemptType;
import me.geuxy.emu.utils.entity.BoundingBox;
import me.geuxy.emu.utils.math.MathUtil;
import me.geuxy.emu.utils.world.BlockUtils;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

@Getter @RequiredArgsConstructor
public class PositionProcessor {

    private final PlayerData data;

    private float yaw, pitch, lastYaw, lastPitch, deltaYaw, deltaPitch, lastDeltaYaw, lastDeltaPitch;

    private double x, y, z, lastX, lastY, lastZ, deltaX, deltaY, deltaZ, lastDeltaX, lastDeltaY, lastDeltaZ, speed, lastSpeed;
    private boolean clientGround, lastClientGround;

    private int groundTicks, airTicks, climbTicks, lastGroundTicks, lastAirTicks, lastClimbTicks, outVehicleTicks;

    private Location from, to;

    private final List<Block> blocks = new ArrayList<>();

    public void handle(WrappedPacketInFlying wrapper) {
        boolean position = wrapper.isPosition();
        boolean look = wrapper.isLook();

        this.lastYaw = yaw;
        this.lastPitch = pitch;
        this.yaw = look ? wrapper.getYaw() : yaw;
        this.pitch = look ? wrapper.getPitch() : pitch;

        this.lastDeltaYaw = deltaYaw;
        this.lastDeltaPitch = deltaPitch;
        this.deltaYaw = Math.abs(yaw - lastYaw);
        this.deltaPitch = Math.abs(pitch - lastPitch);

        this.lastX = x;
        this.lastY = y;
        this.lastZ = z;

        this.x = position ? wrapper.getX() : x;
        this.y = position ? wrapper.getY() : y;
        this.z = position ? wrapper.getZ() : z;

        this.lastDeltaX = deltaX;
        this.lastDeltaY = deltaY;
        this.lastDeltaZ = deltaZ;

        this.deltaX = Math.abs(x - lastX);
        this.deltaY = y - lastY;
        this.deltaZ = Math.abs(z - lastZ);

        this.lastSpeed = speed;

        this.speed = MathUtil.hypot(deltaX, deltaZ);

        this.handleCollision();

        this.lastClientGround = clientGround;
        this.clientGround = wrapper.isOnGround();

        this.from = new Location(data.getPlayer().getWorld(), lastX, lastY, lastZ, lastYaw, lastPitch);
        this.to = new Location(data.getPlayer().getWorld(), x, y, z, yaw, pitch);

    }

    public void handleCollision() {
        blocks.clear();

        BoundingBox boundingBox = new BoundingBox(data.getPlayer()).expand(0, 0, 0.55, 0.6, 0, 0);

        double minX = boundingBox.getMinX();
        double minY = boundingBox.getMinY();
        double minZ = boundingBox.getMinZ();
        double maxX = boundingBox.getMaxX();
        double maxY = boundingBox.getMaxY();
        double maxZ = boundingBox.getMaxZ();

        for (double x = minX; x <= maxX; x += (maxX - minX)) {
            for (double y = minY; y <= maxY + 0.01; y += (maxY - minY) / 5) {
                for (double z = minZ; z <= maxZ; z += (maxZ - minZ)) {
                    blocks.add(BlockUtils.getBlock(new Location(data.getPlayer().getWorld(), x, y, z)));
                }
            }
        }
    }

    public void handleFlying() {
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

        this.climbTicks = data.getExemptProcessor().isExempt(ExemptType.ON_CLIMBABLE) ? Math.min(10, climbTicks + 1) : 0;
        this.outVehicleTicks = data.getPlayer().isInsideVehicle() ? 0 : Math.min(10, outVehicleTicks + 1);
    }

}
