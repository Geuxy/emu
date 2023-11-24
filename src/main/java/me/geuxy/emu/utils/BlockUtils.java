package me.geuxy.emu.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class BlockUtils {

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

}
