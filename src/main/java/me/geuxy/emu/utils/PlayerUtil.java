package me.geuxy.emu.utils;

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
}
