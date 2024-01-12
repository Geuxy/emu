package ac.emu.exempt;

import ac.emu.user.EmuPlayer;
import ac.emu.utils.BlockUtils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.bukkit.GameMode;
import org.bukkit.material.Stairs;
import org.bukkit.util.NumberConversions;

import java.util.function.Function;

@Getter @RequiredArgsConstructor
public enum ExemptType {

    TELEPORTED(d -> d.getActionData().getTeleportTicks() < 2),
    IN_VEHICLE(d -> d.getMovementData().getSinceInVehicleTicks() < 5 || d.getActionData().isInsideVehicle()),
    SPAWNED(d -> d.getActionData().getLivingTicks() < 12),
    ALLOWED_FLIGHT(d -> d.getPlayer().getAllowFlight()),
    CREATIVE(d -> d.getPlayer().getGameMode() == GameMode.CREATIVE),
    ON_CLIMBABLE(d -> !d.getMovementData().getBlocks().isEmpty() && d.getMovementData().getBlocks().stream().anyMatch(BlockUtils::isClimbable)),
    IN_LIQUID(d -> BlockUtils.getSurroundingBlocks(d.getPlayer(), 0).stream().anyMatch(BlockUtils::isLiquid) || BlockUtils.getSurroundingBlocks(d.getPlayer(), 1).stream().anyMatch(BlockUtils::isLiquid)),
    ON_ICE(d -> BlockUtils.isIce(BlockUtils.getBlock(d.getPlayer().getLocation().subtract(0D, 1D, 0D)))),
    ON_SOULSAND(d -> BlockUtils.isSoulSand(BlockUtils.getBlock(d.getPlayer().getLocation().subtract(0D, 1D, 0D)))),
    ON_SLIME(d -> BlockUtils.isSlime(BlockUtils.getBlock(d.getPlayer().getLocation().subtract(0D, 1D, 0D)))),
    IN_WEB(d -> d.getMovementData().getBlocks().stream().filter(b -> b.getY() >= d.getMovementData().getTo().getY()).anyMatch(BlockUtils::isWeb) || BlockUtils.getSurroundingBlocks(d.getPlayer(), 0).stream().anyMatch(BlockUtils::isWeb)),
    VELOCITY(d -> d.getVelocityData().getTicksSinceVelocityPong() < 3),
    VELOCITY_LONG(d -> d.getVelocityData().getTicksSinceVelocityPong() < 9),
    EXPLOSION(d -> d.getVelocityData().getTicksSinceExplosion() < 8),
    CHUNK(d -> !d.getPlayer().getWorld().isChunkLoaded(NumberConversions.floor(d.getMovementData().getX()) >> 4, NumberConversions.floor(d.getMovementData().getZ()) >> 4)),
    STEPPING(d -> d.getMovementData().isClientGround() && d.getMovementData().isMathGround() && d.getMovementData().isLastMathGround() && d.getMovementData().getDeltaY() <= 0.5 && d.getMovementData().getDeltaY() > 0.01),
    UNDER_BLOCK(d -> d.getMovementData().getSinceUnderBlockTicks() < 2),
    BOAT(d -> d.getUtilities().isNearBoat()),
    LAGGING(d -> d.getActionData().getDeltaFlying() > 100 || d.getActionData().isLagging()),
    LAST_LAGGING(d -> d.getActionData().isLastLagging()),
    JUMPED(d -> d.getMovementData().isLastClientGround() && Math.abs(d.getMovementData().getDeltaY() - d.getUtilities().getJumpHeight()) < 1E-13),
    LAST_JUMPED(d -> !d.getMovementData().isLastClientGround() && Math.abs(d.getMovementData().getLastDeltaY() - d.getUtilities().getJumpHeight()) < 1E-13),
    NEAR_WALL(d -> BlockUtils.getSurroundingBlocks(d.getPlayer(), 0, 0.31).stream().anyMatch(b -> b.getType().isSolid()) || BlockUtils.getSurroundingBlocks(d.getPlayer(), 1, 0.31).stream().anyMatch(b -> b.getType().isSolid())),
    SETBACK(d -> d.getSetbackData().getTicksSinceSetback() < 2),
    NEAR_STAIRS(d -> d.getMovementData().getBlocks().stream().anyMatch(b -> b.getType().getData() == Stairs.class)),
    NOT_MOVING(d -> !d.getMovementData().isPosition() || !d.getMovementData().isLastPosition());

    private final Function<EmuPlayer, Boolean> function;

}
