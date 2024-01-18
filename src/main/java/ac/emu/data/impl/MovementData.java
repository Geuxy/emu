package ac.emu.data.impl;

import ac.emu.data.Data;
import ac.emu.data.profile.EmuPlayer;
import ac.emu.packet.Packet;
import ac.emu.exempt.ExemptType;

import ac.emu.utils.location.PastLocation;
import ac.emu.utils.math.MathUtil;
import ac.emu.utils.mcp.AxisAlignedBB;
import ac.emu.utils.type.LimitedList;
import ac.emu.utils.world.BlockUtil;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerPositionAndLook;
import lombok.Getter;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.*;

@Getter
public class MovementData extends Data {

    private float yaw, pitch, lastYaw, lastPitch;
    private float deltaYaw, deltaPitch, lastDeltaYaw, lastDeltaPitch;

    private double x, y, z, lastX, lastY, lastZ;
    private double deltaX, deltaY, deltaZ, lastDeltaX, lastDeltaY, lastDeltaZ;
    private double speed, lastSpeed;

    private boolean clientGround, lastClientGround, serverGround, lastServerGround, mathGround, lastMathGround, position, lastPosition;

    private int groundTicks, airTicks, climbTicks, lastGroundTicks, lastAirTicks, lastClimbTicks, sinceInVehicleTicks, sinceUnderBlockTicks, sinceOnIceTicks;

    private Location from, to, lastLocationOnGround;

    private final LimitedList<PastLocation> pastLocations = new LimitedList<>(30);

    private final List<Block> blocks = new ArrayList<>();

    private final Deque<Vector> teleportList = new ArrayDeque<>();

    private AxisAlignedBB boundingBox;

    private long lastFlying, deltaFlying;

    public MovementData(EmuPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(packet.isMovement()) {
            WrapperPlayClientPlayerFlying wrapper = new WrapperPlayClientPlayerFlying((PacketReceiveEvent) packet.getEvent());

            boolean position = wrapper.hasPositionChanged();
            boolean look = wrapper.hasRotationChanged();

            this.lastPosition = position;
            this.position = position;

            this.lastYaw = yaw;
            this.lastPitch = pitch;
            this.yaw = look ? wrapper.getLocation().getYaw() : yaw;
            this.pitch = look ? wrapper.getLocation().getPitch() : pitch;

            this.lastDeltaYaw = deltaYaw;
            this.lastDeltaPitch = deltaPitch;
            this.deltaYaw = Math.abs(yaw - lastYaw);
            this.deltaPitch = Math.abs(pitch - lastPitch);

            this.lastX = x;
            this.lastY = y;
            this.lastZ = z;

            this.x = position ? wrapper.getLocation().getX() : x;
            this.y = position ? wrapper.getLocation().getY() : y;
            this.z = position ? wrapper.getLocation().getZ() : z;

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
            this.lastMathGround = mathGround;
            this.lastServerGround = serverGround;
            this.clientGround = wrapper.isOnGround();
            this.serverGround = blocks.stream().anyMatch(b -> b.getType() != Material.AIR);
            this.mathGround = y % 0.015625 == 0;

            this.from = new Location(data.getPlayer().getWorld(), lastX, lastY, lastZ, lastYaw, lastPitch);
            this.to = new Location(data.getPlayer().getWorld(), x, y, z, yaw, pitch);

            this.pastLocations.add(new PastLocation(lastX, lastY, lastZ, lastYaw, lastPitch));

            if(clientGround && serverGround && mathGround) {
                this.lastLocationOnGround = new Location(data.getPlayer().getWorld(), lastX, Math.round(lastY), lastZ);
            }

            this.deltaFlying = System.currentTimeMillis() - lastFlying;
            this.lastFlying = System.currentTimeMillis();

            this.handleFlying();

            if(position && look) {
                for(Vector teleport : teleportList) {
                    if(teleport.getX() == x && teleport.getY() == y && teleport.getZ() == z) {
                        data.getActionData().handleTeleport();
                        teleportList.remove(teleport);
                    }
                }
            }
        }

        if(packet.getType() == PacketType.Play.Server.PLAYER_POSITION_AND_LOOK) {
            WrapperPlayServerPlayerPositionAndLook wrapper = new WrapperPlayServerPlayerPositionAndLook((PacketSendEvent) packet.getEvent());

            teleportList.add(new Vector(wrapper.getX(), wrapper.getY(), wrapper.getZ()));
        }
    }

    public void handleCollision() {
        blocks.clear();

        this.boundingBox = new AxisAlignedBB(data.getPlayer()).expand(0, 0, 0.55, 0.6, 0, 0);

        double minX = boundingBox.getMinX();
        double minY = boundingBox.getMinY();
        double minZ = boundingBox.getMinZ();
        double maxX = boundingBox.getMaxX();
        double maxY = boundingBox.getMaxY();
        double maxZ = boundingBox.getMaxZ();

        for (double x = minX; x <= maxX; x += (maxX - minX)) {
            for (double y = minY; y <= maxY + 0.01; y += (maxY - minY) / 5) {
                for (double z = minZ; z <= maxZ; z += (maxZ - minZ)) {
                    blocks.add(BlockUtil.getBlock(new Location(data.getPlayer().getWorld(), x, y, z)));
                }
            }
        }
    }

    public void handleFlying() {
        this.lastGroundTicks = groundTicks;
        this.lastAirTicks = airTicks;

        if(clientGround) {
            this.groundTicks++;
            this.airTicks = 0;
        } else {
            this.airTicks++;
            this.groundTicks = 0;
        }

        this.lastClimbTicks = climbTicks;
        this.sinceOnIceTicks = data.getExemptData().isExempt(ExemptType.ON_ICE) ? 0 : ++sinceOnIceTicks;
        this.climbTicks = data.getExemptData().isExempt(ExemptType.ON_CLIMBABLE) ? ++climbTicks : 0;
        this.sinceInVehicleTicks = data.getPlayer().isInsideVehicle() ? 0 : ++sinceInVehicleTicks;
        this.sinceUnderBlockTicks = BlockUtil.getSurroundingBlocks(data.getPlayer(), 2D).stream().anyMatch(b -> b.getType().isSolid()) ? 0 : ++this.sinceUnderBlockTicks;
    }

    public boolean isBridging() {
        return getTo().subtract(0, 2, 0).getBlock().getType() == Material.AIR;
    }

}
