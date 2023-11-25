package me.geuxy.emu.data;

import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.packetwrappers.play.in.transaction.WrappedPacketInTransaction;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import me.geuxy.emu.Emu;
import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.data.processors.ActionProcessor;
import me.geuxy.emu.data.processors.CombatProcessor;
import me.geuxy.emu.data.processors.PositionProcessor;
import me.geuxy.emu.data.processors.VelocityProcessor;
import me.geuxy.emu.utils.world.BlockUtils;
import me.geuxy.emu.utils.entity.BoundingBox;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.NumberConversions;

import java.util.ArrayList;
import java.util.List;

@Getter @RequiredArgsConstructor
public class PlayerData {

    private final Player player;

    private final PositionProcessor positionProcessor = new PositionProcessor(this);
    private final VelocityProcessor velocityProcessor = new VelocityProcessor(this);
    private final ActionProcessor actionProcessor = new ActionProcessor(this);
    private final CombatProcessor combatProcessor = new CombatProcessor(this);

    // Exempts (fuck naming conventions)
    public boolean TELEPORTED;
    public boolean RIDING;
    public boolean LIVING;
    public boolean ALLOWED_FLYING;
    public boolean BLOCK_ABOVE;
    public boolean CLIMBABLE;
    public boolean LIQUID;
    public boolean ICE;
    public boolean SOULSAND;
    public boolean SLIME;
    public boolean EXPLOSION;
    public boolean VELOCITY;
    public boolean WEB;
    public boolean CHUNK;
    public boolean STEPPING;

    private final List<AbstractCheck> checks = Emu.INSTANCE.getCheckManager().loadChecks(this);

    private final List<Block> blocks = new ArrayList<>();

    @Setter
    private boolean alertsEnabled;

    public void handleFlying(WrappedPacketInFlying packet) {
        positionProcessor.handle(packet);

        this.handleCollision();
        this.handleFlying();
    }

    public void handleCollision() {
        blocks.clear();

        BoundingBox boundingBox = new BoundingBox(player).expand(0, 0, 0.55, 0.6, 0, 0);

        double minX = boundingBox.getMinX();
        double minY = boundingBox.getMinY();
        double minZ = boundingBox.getMinZ();
        double maxX = boundingBox.getMaxX();
        double maxY = boundingBox.getMaxY();
        double maxZ = boundingBox.getMaxZ();

        for (double x = minX; x <= maxX; x += (maxX - minX)) {
            for (double y = minY; y <= maxY + 0.01; y += (maxY - minY) / 5) {
                for (double z = minZ; z <= maxZ; z += (maxZ - minZ)) {
                    blocks.add(BlockUtils.getBlock(new Location(player.getWorld(), x, y, z)));
                }
            }
        }
    }

    public void handleFlying() {
        Location bottomLoc = player.getLocation().subtract(0D, 1D, 0D);
        Location bodyLoc = player.getLocation().add(0D, 1D, 0D);

        this.TELEPORTED = actionProcessor.getTeleportTicks() < 2;
        this.RIDING = positionProcessor.getOutVehicleTicks() < 2;
        this.LIVING = actionProcessor.getLivingTicks() < 2;
        this.ALLOWED_FLYING = player.getAllowFlight();
        this.BLOCK_ABOVE = isSolidAbove();
        this.CLIMBABLE = BlockUtils.isClimbable(player.getLocation()) || BlockUtils.isClimbable(bodyLoc);
        this.LIQUID = BlockUtils.isLiquid(player.getLocation()) || BlockUtils.isLiquid(bodyLoc);
        this.ICE = BlockUtils.isIce(bottomLoc);
        this.SOULSAND = BlockUtils.isSoulSand(bottomLoc);
        this.SLIME = BlockUtils.isSlime(bottomLoc);
        this.WEB = !blocks.isEmpty() && blocks.stream().anyMatch(b -> b.getType().equals(Material.WEB));
        this.VELOCITY = velocityProcessor.getVelocityTicks() < 2;
        this.EXPLOSION = velocityProcessor.getExplosionTicks() < 2;
        this.CHUNK = !player.getWorld().isChunkLoaded(
            NumberConversions.floor(positionProcessor.getX()) >> 4,
            NumberConversions.floor(positionProcessor.getZ()) >> 4
        );
        this.STEPPING = BlockUtils.isHalf(bottomLoc) || BlockUtils.isHalf(positionProcessor.getLastLocation().subtract(0D, 1D, 0D));

        positionProcessor.onTick();
        velocityProcessor.handleFlying();
        actionProcessor.handleFlying();
        combatProcessor.handleFlying();
    }

    public void handleUseEntity(WrappedPacketInUseEntity packet) {
        combatProcessor.handleUseEntity(packet);
    }

    public void handleTeleport() {
        actionProcessor.handleTeleport();
    }

    public void handleVelocity(double x, double y, double z) {
        velocityProcessor.handle(x, y, z);
    }

    public void handleExplosion() {
        velocityProcessor.handleExplosion();
    }

    public void handleRespawn() {
        actionProcessor.handleRespawn();
    }

    public void handleTransaction(WrappedPacketInTransaction packet) {
        velocityProcessor.handleTransaction(packet);
    }

    public double getSpeedMultiplier() {
        PotionEffect effect = this.getEffect(PotionEffectType.SPEED);

        if(effect != null) {
            return effect.getAmplifier() + 1 * 0.2;
        }

        return 1;
    }

    public double getSlowDivider() {
        PotionEffect effect = this.getEffect(PotionEffectType.SLOW);

        if(effect != null) {
            return effect.getAmplifier() + 1 * 0.15;
        }

        return 1;
    }

    public PotionEffect getEffect(PotionEffectType type) {
        return player.getActivePotionEffects().stream().filter(e -> e.getType().equals(type)).findFirst().orElse(null);
    }

    private boolean isSolidAbove() {
        for(double x = -0.3; x < 0.6; x += 0.3) {
            for (double z = -0.3; z < 0.6; z += 0.3) {
                Location playerLoc = player.getLocation();
                Location loc = new Location(player.getWorld(), playerLoc.getX() + x, playerLoc.getY() + 2, playerLoc.getZ() + z);

                Block block = BlockUtils.getBlock(loc);

                if (block != null && block.getType().isSolid()) {
                    return true;
                }
            }
        }
        return false;
    }

}
