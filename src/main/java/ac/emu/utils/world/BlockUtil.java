package ac.emu.utils.world;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BlockUtil {

    public static boolean isClimbable(Block block) {
        return isMaterial(block, Material.LADDER) || isMaterial(block, Material.VINE);
    }

    public static boolean isLiquid(Block block) {
        return isMaterial(block, Material.WATER) || isMaterial(block, Material.STATIONARY_WATER)
            || isMaterial(block, Material.LAVA) || isMaterial(block, Material.STATIONARY_LAVA);
    }

    public static boolean isIce(Block block) {
        return isMaterial(block, Material.ICE) || isMaterial(block, Material.PACKED_ICE);
    }

    public static boolean isSlime(Block block) {
        return isMaterial(block, Material.SLIME_BLOCK);
    }

    public static boolean isSoulSand(Block block) {
        return isMaterial(block, Material.SOUL_SAND);
    }

    public static boolean isWeb(Block block) {
        return isMaterial(block, Material.WEB);
    }

    public static Block getBlock(Location location) {
        if (location.getWorld().isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4)) {
            return location.getWorld().getBlockAt(location);
        } else {
            return null;
        }
    }

    public static boolean isMaterial(Block block, Material material) {
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
