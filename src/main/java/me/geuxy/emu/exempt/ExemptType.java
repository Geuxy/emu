package me.geuxy.emu.exempt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.utils.entity.PlayerUtil;
import me.geuxy.emu.utils.world.BlockUtils;

import org.bukkit.util.NumberConversions;

import java.util.function.Function;

@Getter @RequiredArgsConstructor
public enum ExemptType {

    TELEPORTED(d -> d.getActionProcessor().getTeleportTicks() < 2),
    IN_VEHICLE(d -> d.getPositionProcessor().getOutVehicleTicks() < 5 || d.getActionProcessor().isInsideVehicle()),
    SPAWNED(d -> d.getActionProcessor().getLivingTicks() < 12),
    ALLOWED_FLIGHT(d -> d.getPlayer().getAllowFlight()),
    ON_CLIMBABLE(d -> !d.getPositionProcessor().getBlocks().isEmpty() && d.getPositionProcessor().getBlocks().stream().anyMatch(BlockUtils::isClimbable)),
    IN_LIQUID(d -> BlockUtils.getSurroundingBlocks(d.getPlayer(), 0).stream().anyMatch(BlockUtils::isLiquid) || BlockUtils.getSurroundingBlocks(d.getPlayer(), 1).stream().anyMatch(BlockUtils::isLiquid)),
    ON_ICE(d -> BlockUtils.isIce(d.getPlayer().getLocation().subtract(0D, 1D, 0D))),
    ON_SOULSAND(d -> BlockUtils.isSoulSand(d.getPlayer().getLocation().subtract(0D, 1D, 0D))),
    ON_SLIME(d -> BlockUtils.isSlime(d.getPlayer().getLocation().subtract(0D, 1D, 0D))),
    IN_WEB(d -> !d.getPositionProcessor().getBlocks().isEmpty() && d.getPositionProcessor().getBlocks().stream().anyMatch(BlockUtils::isWeb)),
    VELOCITY(d -> d.getVelocityProcessor().getVelocityTicks() < 3),
    VELOCITY_LONG(d -> d.getVelocityProcessor().getVelocityTicks() < 10),
    EXPLOSION(d -> d.getVelocityProcessor().getExplosionTicks() < 3),
    CHUNK(d -> !d.getPlayer().getWorld().isChunkLoaded(NumberConversions.floor(d.getPositionProcessor().getX()) >> 4, NumberConversions.floor(d.getPositionProcessor().getZ()) >> 4)),
    STEPPING(d -> BlockUtils.isHalf(d.getPositionProcessor().getTo().subtract(0D, 1D, 0D)) || BlockUtils.isHalf(d.getPositionProcessor().getFrom().subtract(0D, 1D, 0D))),
    UNDER_BLOCK(d -> BlockUtils.getSurroundingBlocks(d.getPlayer(), 2D).stream().anyMatch(b -> b.getType().isSolid())),
    BOAT(d -> PlayerUtil.isNearBoat(d.getPlayer())),
    LAGGING(d -> d.getActionProcessor().getDeltaFlying() > 100 || d.getActionProcessor().getDeltaFlying() < 5 || d.getActionProcessor().isLagging());

    private final Function<PlayerData, Boolean> function;

}
