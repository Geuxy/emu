package me.geuxy.emu.utils.math;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class MathUtil {

    public static double hypot(double a, double b) {
        return Math.sqrt((a * a) + (b * b));
    }

    // By PhoenixHaven
    public static double getAngleRotation(Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null) return -1;

        Vector playerRotation = new Vector(loc1.getYaw(), loc1.getPitch(), 0.0f);
        loc1.setY(0);
        loc2.setY(0);

        float[] rot = getRotations(loc1, loc2);
        Vector expectedRotation = new Vector(rot[0], rot[1], 0);
        return ((playerRotation.getX() - expectedRotation.getX()) % 180);
    }

    // By PhoenixHaven
    public static float[] getRotations(Location one, Location two) {
        double diffX = two.getX() - one.getX();
        double diffZ = two.getZ() - one.getZ();
        double diffY = two.getY() + 2.0 - 0.4 - (one.getY() + 2.0);
        double dist = MathUtil.hypot(diffX, diffZ);
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float) (-Math.atan2(diffY, dist) * 180.0 / Math.PI);
        return new float[]{yaw, pitch};
    }

}
