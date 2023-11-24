package me.geuxy.emu.utils;

import lombok.Getter;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/*
  BoundingBox util by Trajkot's Venom repository:
  https://github.com/Trajkot999/Venom/
*/
@Getter
public class BoundingBox {

    private double minX, minY, minZ, maxX, maxY, maxZ;

    public BoundingBox(double minX, double maxX, double minY, double maxY, double minZ, double maxZ) {
        if (minX < maxX) {
            this.minX = minX;
            this.maxX = maxX;
        } else {
            this.minX = maxX;
            this.maxX = minX;
        }

        if (minY < maxY) {
            this.minY = minY;
            this.maxY = maxY;
        } else {
            this.minY = maxY;
            this.maxY = minY;
        }

        if (minZ < maxZ) {
            this.minZ = minZ;
            this.maxZ = maxZ;
        } else {
            this.minZ = maxZ;
            this.maxZ = minZ;
        }
    }

    public BoundingBox(Vector min, Vector max) {
        this.minX = min.getX();
        this.minY = min.getY();
        this.minZ = min.getZ();
        this.maxX = max.getX();
        this.maxY = max.getY();
        this.maxZ = max.getZ();
    }

    public BoundingBox(Player player) {
        this.minX = player.getLocation().getX() - 0.3D;
        this.minY = player.getLocation().getY();
        this.minZ = player.getLocation().getZ() - 0.3D;
        this.maxX = player.getLocation().getX() + 0.3D;
        this.maxY = player.getLocation().getY() + 1.8D;
        this.maxZ = player.getLocation().getZ() + 0.3D;
    }

    public BoundingBox(Vector data) {
        this.minX = data.getX() - 0.4D;
        this.minY = data.getY();
        this.minZ = data.getZ() - 0.4D;
        this.maxX = data.getX() + 0.4D;
        this.maxY = data.getY() + 1.9D;
        this.maxZ = data.getZ() + 0.4D;
    }

    public BoundingBox move(double x, double y, double z) {
        this.minX += x;
        this.minY += y;
        this.minZ += z;
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;

        return this;
    }

    public List<Block> getBlocks(World world) {
        List<Block> blocks = new ArrayList<>();

        double minX = this.minX;
        double minY = this.minY;
        double minZ = this.minZ;
        double maxX = this.maxX;
        double maxY = this.maxY;
        double maxZ = this.maxZ;

        for (double x = minX; x <= maxX; x += (maxX - minX)) {
            for (double y = minY; y <= maxY + 0.01; y += (maxY - minY)) {
                for (double z = minZ; z <= maxZ; z += (maxZ - minZ)) {
                    Block block = world.getBlockAt(new Location(world, x, y, z));
                    blocks.add(block);
                }
            }
        }
        return blocks;
    }

    public BoundingBox expand(double x, double y, double z) {
        this.minX -= x;
        this.minY -= y;
        this.minZ -= z;
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;

        return this;
    }

    public BoundingBox expand(double minX, double maxX, double minY, double maxY, double minZ, double maxZ) {
        this.minX -= minX;
        this.minY -= minY;
        this.minZ -= minZ;
        this.maxX += maxX;
        this.maxY += maxY;
        this.maxZ += maxZ;

        return this;
    }

    public BoundingBox union(BoundingBox other) {
        double minX = Math.min(this.minX, other.minX);
        double minY = Math.min(this.minY, other.minY);
        double minZ = Math.min(this.minZ, other.minZ);
        double maxX = Math.max(this.maxX, other.maxX);
        double maxY = Math.max(this.maxY, other.maxY);
        double maxZ = Math.max(this.maxZ, other.maxZ);

        return new BoundingBox(minX, maxX, minY, maxY, minZ, maxZ);
    }

    public double getSize() {
        Vector min = new Vector(minX, minY, minZ);
        Vector max = new Vector(maxX, maxY, maxZ);

        return min.distance(max);
    }

    public double min(int i) {
        switch (i) {
        case 0:
            return minX;
        case 1:
            return minY;
        case 2:
            return minZ;
        default:
            return 0;
        }
    }

    public double max(int i) {
        switch (i) {
        case 0:
            return maxX;
        case 1:
            return maxY;
        case 2:
            return maxZ;
        default:
            return 0;
        }
    }

}
