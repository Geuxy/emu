package me.geuxy.emu.data.processors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;
import me.geuxy.emu.utils.MathUtil;
import org.bukkit.Location;

@Getter @RequiredArgsConstructor
public class PositionProcessor {

    private final PlayerData data;

    private double x, y, z, lastX, lastY, lastZ, deltaX, deltaY, deltaZ, lastDeltaX, lastDeltaY, lastDeltaZ, speed, lastSpeed;
    private boolean clientGround, lastClientGround;

    private int groundTicks, airTicks, climbTicks, lastGroundTicks, lastAirTicks, lastClimbTicks, outVehicleTicks;

    private Location lastLocation;

    public void handle(Packet packet) {
        this.lastX = x;
        this.lastY = y;
        this.lastZ = z;

        this.x = packet.getRaw().getDoubles().read(0);
        this.y = packet.getRaw().getDoubles().read(1);
        this.z = packet.getRaw().getDoubles().read(2);

        this.lastDeltaX = deltaX;
        this.lastDeltaY = deltaY;
        this.lastDeltaZ = deltaZ;

        this.deltaX = Math.abs(x - lastX);
        this.deltaY = y - lastY;
        this.deltaZ = Math.abs(z - lastZ);

        this.lastSpeed = speed;

        this.speed = MathUtil.hypot(deltaX, deltaZ);

        this.handleFlying(packet);

        this.lastLocation = new Location(data.getPlayer().getWorld(), lastX, lastY, lastZ, data.getRotationProcessor().getLastYaw(), data.getRotationProcessor().getLastPitch());
    }

    public void handleFlying(Packet packet) {
        this.lastClientGround = clientGround;
        this.clientGround = packet.getRaw().getBooleans().read(0);
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
