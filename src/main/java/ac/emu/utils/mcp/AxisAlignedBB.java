package ac.emu.utils.mcp;

import lombok.Getter;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@Getter
public class AxisAlignedBB {

    private double minX, minY, minZ;
    private double maxX, maxY, maxZ;

    public AxisAlignedBB(double minX, double maxX, double minY, double maxY, double minZ, double maxZ) {
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

    public AxisAlignedBB(Vector min, Vector max) {
        this.minX = min.getX();
        this.minY = min.getY();
        this.minZ = min.getZ();
        this.maxX = max.getX();
        this.maxY = max.getY();
        this.maxZ = max.getZ();
    }

    public AxisAlignedBB(Player player) {
        this.minX = player.getLocation().getX() - 0.3D;
        this.minY = player.getLocation().getY();
        this.minZ = player.getLocation().getZ() - 0.3D;
        this.maxX = player.getLocation().getX() + 0.3D;
        this.maxY = player.getLocation().getY() + 1.8D;
        this.maxZ = player.getLocation().getZ() + 0.3D;
    }

    public AxisAlignedBB(Vector data) {
        this.minX = data.getX() - 0.4D;
        this.minY = data.getY();
        this.minZ = data.getZ() - 0.4D;
        this.maxX = data.getX() + 0.4D;
        this.maxY = data.getY() + 1.9D;
        this.maxZ = data.getZ() + 0.4D;
    }

    public AxisAlignedBB expand(double x, double y,double z) {
        this.minX -= x;
        this.minY -= y;
        this.minZ -= z;

        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;

        return this;
    }

    public AxisAlignedBB expand(double minX, double maxX, double minY, double maxY, double minZ, double maxZ) {
        this.minX -= minX;
        this.minY -= minY;
        this.minZ -= minZ;

        this.maxX += maxX;
        this.maxY += maxY;
        this.maxZ += maxZ;

        return this;
    }

    public AxisAlignedBB union(AxisAlignedBB other) {
        double minX = Math.min(this.minX, other.minX);
        double minY = Math.min(this.minY, other.minY);
        double minZ = Math.min(this.minZ, other.minZ);
        double maxX = Math.max(this.maxX, other.maxX);
        double maxY = Math.max(this.maxY, other.maxY);
        double maxZ = Math.max(this.maxZ, other.maxZ);

        return new AxisAlignedBB(minX, maxX, minY, maxY, minZ, maxZ);
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

    public MovingObjectPosition calculateIntercept(Vec3 vecA, Vec3 vecB) {
        Vec3 vec3 = vecA.getIntermediateWithXValue(vecB, this.minX);
        Vec3 vec31 = vecA.getIntermediateWithXValue(vecB, this.maxX);
        Vec3 vec32 = vecA.getIntermediateWithYValue(vecB, this.minY);
        Vec3 vec33 = vecA.getIntermediateWithYValue(vecB, this.maxY);
        Vec3 vec34 = vecA.getIntermediateWithZValue(vecB, this.minZ);
        Vec3 vec35 = vecA.getIntermediateWithZValue(vecB, this.maxZ);

        if(!this.isVecInYZ(vec3)) {
            vec3 = null;
        }

        if(!this.isVecInYZ(vec31)) {
            vec31 = null;
        }

        if(!this.isVecInXZ(vec32)) {
            vec32 = null;
        }

        if(!this.isVecInXZ(vec33)) {
            vec33 = null;
        }

        if(!this.isVecInXY(vec34)) {
            vec34 = null;
        }

        if(!this.isVecInXY(vec35)) {
            vec35 = null;
        }

        Vec3 vec36 = null;

        if(vec3 != null) {
            vec36 = vec3;
        }

        if(vec31 != null && (vec36 == null || vecA.squareDistanceTo(vec31) < vecA.squareDistanceTo(vec36))) {
            vec36 = vec31;
        }

        if(vec32 != null && (vec36 == null || vecA.squareDistanceTo(vec32) < vecA.squareDistanceTo(vec36))) {
            vec36 = vec32;
        }

        if(vec33 != null && (vec36 == null || vecA.squareDistanceTo(vec33) < vecA.squareDistanceTo(vec36))) {
            vec36 = vec33;
        }

        if(vec34 != null && (vec36 == null || vecA.squareDistanceTo(vec34) < vecA.squareDistanceTo(vec36))) {
            vec36 = vec34;
        }

        if(vec35 != null && (vec36 == null || vecA.squareDistanceTo(vec35) < vecA.squareDistanceTo(vec36))) {
            vec36 = vec35;
        }

        if(vec36 == null) {
            return null;

        } else {
            EnumFacing enumfacing;

            if(vec36 == vec3) {
                enumfacing = EnumFacing.WEST;

            } else if(vec36 == vec31) {
                enumfacing = EnumFacing.EAST;

            } else if(vec36 == vec32) {
                enumfacing = EnumFacing.DOWN;

            } else if(vec36 == vec33) {
                enumfacing = EnumFacing.UP;

            } else if(vec36 == vec34) {
                enumfacing = EnumFacing.NORTH;

            } else {
                enumfacing = EnumFacing.SOUTH;
            }

            return new MovingObjectPosition(vec36, enumfacing);
        }
    }

    private boolean isVecInYZ(Vec3 vec) {
        return vec != null && vec.yCoord >= this.minY && vec.yCoord <= this.maxY && vec.zCoord >= this.minZ && vec.zCoord <= this.maxZ;
    }

    private boolean isVecInXZ(Vec3 vec) {
        return vec != null && vec.xCoord >= this.minX && vec.xCoord <= this.maxX && vec.zCoord >= this.minZ && vec.zCoord <= this.maxZ;
    }

    private boolean isVecInXY(Vec3 vec) {
        return vec != null && vec.xCoord >= this.minX && vec.xCoord <= this.maxX && vec.yCoord >= this.minY && vec.yCoord <= this.maxY;
    }

}