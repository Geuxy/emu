package ac.emu.exempt;

import ac.emu.data.PlayerData;
import ac.emu.utils.BlockUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.bukkit.material.Stairs;
import org.bukkit.util.NumberConversions;

import java.util.function.Function;

@Getter @RequiredArgsConstructor
public enum ExemptType {

    TELEPORTED(d -> d.getActionData().getTeleportTicks() < 3),
    IN_VEHICLE(d -> d.getMovementData().getSinceInVehicleTicks() < 5 || d.getActionData().isInsideVehicle()),
    SPAWNED(d -> d.getActionData().getLivingTicks() < 31),
    ALLOWED_FLIGHT(d -> d.getPlayer().getAllowFlight()),
    ON_CLIMBABLE(d -> !d.getMovementData().getBlocks().isEmpty() && d.getMovementData().getBlocks().stream().anyMatch(BlockUtils::isClimbable)),
    IN_LIQUID(d -> BlockUtils.getSurroundingBlocks(d.getPlayer(), 0).stream().anyMatch(BlockUtils::isLiquid) || BlockUtils.getSurroundingBlocks(d.getPlayer(), 1).stream().anyMatch(BlockUtils::isLiquid)),
    ON_ICE(d -> BlockUtils.isIce(d.getPlayer().getLocation().subtract(0D, 1D, 0D))),
    ON_SOULSAND(d -> BlockUtils.isSoulSand(d.getPlayer().getLocation().subtract(0D, 1D, 0D))),
    ON_SLIME(d -> BlockUtils.isSlime(d.getPlayer().getLocation().subtract(0D, 1D, 0D))),
    IN_WEB(d -> !d.getMovementData().getBlocks().isEmpty() && d.getMovementData().getBlocks().stream().anyMatch(BlockUtils::isWeb)),
    VELOCITY(d -> d.getVelocityData().getTicksSinceVelocityPong() < 3),
    VELOCITY_LONG(d -> d.getVelocityData().getTicksSinceVelocityPong() < 9),
    EXPLOSION(d -> d.getVelocityData().getTicksSinceExplosion() < 8),
    CHUNK(d -> !d.getPlayer().getWorld().isChunkLoaded(NumberConversions.floor(d.getMovementData().getX()) >> 4, NumberConversions.floor(d.getMovementData().getZ()) >> 4)),
    STEPPING(d -> d.getMovementData().isClientGround() && d.getMovementData().isMathGround() && d.getMovementData().isLastMathGround() && d.getMovementData().getDeltaY() <= 0.5 && d.getMovementData().getDeltaY() > 0.01),
    UNDER_BLOCK(d -> d.getMovementData().getSinceUnderBlockTicks() < 2),
    BOAT(d -> d.getUtilities().isNearBoat()),
    LAGGING(d -> d.getActionData().getDeltaFlying() > 100 || d.getActionData().getDeltaFlying() < 5 || d.getActionData().isLagging()),
    JUMPED(d -> d.getMovementData().isLastClientGround() && Math.abs(d.getMovementData().getDeltaY() - d.getUtilities().getJumpHeight()) < 1E-10),
    LAST_JUMPED(d -> !d.getMovementData().isLastClientGround() && Math.abs(d.getMovementData().getLastDeltaY() - d.getUtilities().getJumpHeight()) < 1E-10),
    NEAR_WALL(d -> BlockUtils.getSurroundingBlocks(d.getPlayer(), 0, 0.31).stream().anyMatch(b -> b.getType().isSolid()) || BlockUtils.getSurroundingBlocks(d.getPlayer(), 1, 0.31).stream().anyMatch(b -> b.getType().isSolid())),
    SETBACK(d -> d.getSetbackData().getTicksSinceSetback() < 2),
    NEAR_STAIRS(d -> d.getMovementData().getBlocks().stream().anyMatch(b -> b.getType().getData() == Stairs.class));

    private final Function<PlayerData, Boolean> function;

}
