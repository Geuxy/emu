package ac.emu.exempt;

import ac.emu.data.profile.EmuPlayer;
import ac.emu.utils.world.BlockUtil;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.bukkit.GameMode;
import org.bukkit.material.Stairs;
import org.bukkit.util.NumberConversions;

import java.util.function.Function;

@Getter @RequiredArgsConstructor
public enum ExemptType {

    TELEPORTED(d -> d.getActionData().getTeleportTicks() < 3),
    IN_VEHICLE(d -> d.getMovementData().getSinceInVehicleTicks() < 5 || d.getActionData().isInsideVehicle()),
    SPAWNED(d -> d.getActionData().getLivingTicks() < 40),
    ALLOWED_FLIGHT(d -> d.getPlayer().getAllowFlight()),
    CREATIVE(d -> d.getPlayer().getGameMode() == GameMode.CREATIVE),
    ON_CLIMBABLE(d -> !d.getMovementData().getBlocks().isEmpty() && d.getMovementData().getBlocks().stream().anyMatch(BlockUtil::isClimbable)),
    IN_LIQUID(d -> BlockUtil.getSurroundingBlocks(d.getPlayer(), 0).stream().anyMatch(BlockUtil::isLiquid) || BlockUtil.getSurroundingBlocks(d.getPlayer(), 1).stream().anyMatch(BlockUtil::isLiquid)),
    ON_ICE(d -> BlockUtil.isIce(BlockUtil.getBlock(d.getPlayer().getLocation().subtract(0D, 1D, 0D)))),
    ON_SOULSAND(d -> BlockUtil.isSoulSand(BlockUtil.getBlock(d.getPlayer().getLocation().subtract(0D, 1D, 0D)))),
    ON_SLIME(d -> BlockUtil.isSlime(BlockUtil.getBlock(d.getPlayer().getLocation().subtract(0D, 1D, 0D)))),
    IN_WEB(d -> d.getMovementData().getBlocks().stream().filter(b -> b.getY() >= d.getMovementData().getTo().getY()).anyMatch(BlockUtil::isWeb) || BlockUtil.getSurroundingBlocks(d.getPlayer(), 0).stream().anyMatch(BlockUtil::isWeb)),
    VELOCITY(d -> d.getVelocityData().getTicksSinceVelocityPong() < 3),
    VELOCITY_LONG(d -> d.getVelocityData().getTicksSinceVelocityPong() < 9),
    EXPLOSION(d -> d.getVelocityData().getTicksSinceExplosion() < 8),
    CHUNK(d -> !d.getPlayer().getWorld().isChunkLoaded(NumberConversions.floor(d.getMovementData().getX()) >> 4, NumberConversions.floor(d.getMovementData().getZ()) >> 4)),
    STEPPING(d -> d.getMovementData().isClientGround() && d.getMovementData().isMathGround() && d.getMovementData().isLastMathGround() && d.getMovementData().getDeltaY() <= 0.5 && d.getMovementData().getDeltaY() > 0.01),
    UNDER_BLOCK(d -> d.getMovementData().getSinceUnderBlockTicks() < 2),
    BOAT(EmuPlayer::isNearBoat),
    LAGGING(d -> d.getActionData().getDeltaFlying() > 100 || d.getActionData().isLagging()),
    LAST_LAGGING(d -> d.getActionData().isLastLagging()),
    JUMPED(d -> d.getMovementData().isLastClientGround() && Math.abs(d.getMovementData().getDeltaY() - d.getJumpHeight()) < 1E-13),
    LAST_JUMPED(d -> !d.getMovementData().isLastClientGround() && Math.abs(d.getMovementData().getLastDeltaY() - d.getJumpHeight()) < 1E-13),
    NEAR_WALL(d -> BlockUtil.getSurroundingBlocks(d.getPlayer(), 0, 0.31).stream().anyMatch(b -> b.getType().isSolid()) || BlockUtil.getSurroundingBlocks(d.getPlayer(), 1, 0.31).stream().anyMatch(b -> b.getType().isSolid())),
    SETBACK(d -> d.getSetbackData().getTicksSinceSetback() < 2),
    NEAR_STAIRS(d -> d.getMovementData().getBlocks().stream().anyMatch(b -> b.getType().getData() == Stairs.class)),
    NEAR_SLABS(d -> d.getMovementData().getBlocks().stream().anyMatch(b -> b.getType().getData().getName().toLowerCase().contains("slab"))),
    NOT_MOVING(d -> !d.getMovementData().isPosition() || !d.getMovementData().isLastPosition());

    private final Function<EmuPlayer, Boolean> function;

}
