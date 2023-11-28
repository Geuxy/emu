package me.geuxy.emu.utils.world;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockUtils {

    private static List<Material> halfBlocks = Arrays.asList(
            Material.ACACIA_STAIRS, Material.BIRCH_WOOD_STAIRS, Material.BRICK_STAIRS, Material.COBBLESTONE_STAIRS, Material.DARK_OAK_STAIRS, Material.JUNGLE_WOOD_STAIRS, Material.NETHER_BRICK_STAIRS, Material.QUARTZ_STAIRS, Material.RED_SANDSTONE_STAIRS, Material.SANDSTONE_STAIRS, Material.SMOOTH_STAIRS, Material.SPRUCE_WOOD_STAIRS, Material.WOOD_STAIRS,
            Material.STONE_SLAB2, Material.DOUBLE_STONE_SLAB2, Material.STEP, Material.WOOD_STEP,
            Material.SNOW, Material.TRAP_DOOR, Material.IRON_TRAPDOOR, Material.BED);

    public static boolean isHalf(final Location location) {
        Block block = getBlock(location);
        return block != null && isHalf(block);
    }

    public static boolean isHalf(final Block block) {
        return halfBlocks.contains(block.getType());
    }

    public static boolean isClimbable(final Location location) {
        return isClimbable(getBlock(location));
    }

    public static boolean isClimbable(final Block block) {
        return isMaterial(block, Material.LADDER) || isMaterial(block, Material.VINE);
    }

    public static boolean isLiquid(final Location location) {
        return isLiquid(getBlock(location));
    }

    public static boolean isLiquid(final Block block) {
        return isMaterial(block, Material.WATER) || isMaterial(block, Material.STATIONARY_WATER)
            || isMaterial(block, Material.LAVA) || isMaterial(block, Material.STATIONARY_LAVA);
    }

    public static boolean isIce(final Location location) {
        return isIce(getBlock(location));
    }

    public static boolean isIce(final Block block) {
        return isMaterial(block, Material.ICE) || isMaterial(block, Material.PACKED_ICE);
    }

    public static boolean isSlime(final Location location) {
        return isSlime(getBlock(location));
    }

    public static boolean isSlime(final Block block) {
        return isMaterial(block, Material.SLIME_BLOCK);
    }

    public static boolean isSoulSand(final Location location) {
        return isSoulSand(getBlock(location));
    }

    public static boolean isSoulSand(final Block block) {
        return isMaterial(block, Material.SOUL_SAND);
    }

    public static boolean isWeb(final Location location) {
        return isWeb(getBlock(location));
    }

    public static boolean isWeb(final Block block) {
        return isMaterial(block, Material.WEB);
    }

    public static Block getBlock(final Location location) {
        if (location.getWorld().isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4)) {
            return location.getWorld().getBlockAt(location);
        } else {
            return null;
        }
    }

    public static boolean isMaterial(final Block block, final Material material) {
        if(block == null) {
            return false;
        }
        return block.getType().equals(material);
    }

    public static List<Block> getSurroundingBlocks(Player player, double y) {
        List<Block> blockList = new ArrayList<>();

        for(double x = -0.3; x < 0.6; x += 0.3) {
            for (double z = -0.3; z < 0.6; z += 0.3) {
                Location playerLoc = player.getLocation();
                Location loc = new Location(player.getWorld(), playerLoc.getX() + x, playerLoc.getY() + y, playerLoc.getZ() + z);

                Block block = getBlock(loc);

                if (block != null) {
                    blockList.add(block);
                }
            }
        }
        return blockList;
    }

    public static List<Block> getSurroundingBlocks(Player player, double y, double radius) {
        List<Block> blockList = new ArrayList<>();

        for(double x = -radius; x < radius * 2; x += radius) {
            for (double z = -radius; z < radius * 2; z += radius) {
                Location playerLoc = player.getLocation();
                Location loc = new Location(player.getWorld(), playerLoc.getX() + x, playerLoc.getY() + y, playerLoc.getZ() + z);

                Block block = getBlock(loc);

                if (block != null) {
                    blockList.add(block);
                }
            }
        }
        return blockList;
    }

}
