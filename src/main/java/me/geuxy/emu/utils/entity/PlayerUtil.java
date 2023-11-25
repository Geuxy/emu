package me.geuxy.emu.utils.entity;

import me.geuxy.emu.utils.world.BlockUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.*;

import java.util.List;

public class PlayerUtil {


    // PhoenixHaven
    public static boolean isNearRideableEntity(Player user) {
        List<Entity> entities = user.getPlayer().getNearbyEntities(5, 5, 5);

        for (Entity entity : entities) {
            if (entity instanceof Boat || entity instanceof Horse || entity instanceof Minecart) {
                return true;
            }
        }

        return false;
    }

    // PhoenixHaven
    public static boolean isNearBoat(Player user) {
        List<Entity> entities = user.getPlayer().getNearbyEntities(1, 1, 1);

        for (Entity entity : entities) {
            if (entity instanceof Boat) {
                return true;
            }
        }

        return false;
    }

    public static boolean getSurroundingBlocks(Player player, double y) {
        for(double x = -0.4; x < 0.8; x += 0.4) {
            for (double z = -0.4; z < 0.8; z += 0.4) {
                Location playerLoc = player.getLocation();
                Location loc = new Location(player.getWorld(), playerLoc.getX() + x, playerLoc.getY() + y, playerLoc.getZ() + z);

                Block block = BlockUtils.getBlock(loc);

                if (block != null && block.getType().isSolid()) {
                    return true;
                }
            }
        }
        return false;
    }

}
