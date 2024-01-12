package ac.emu.utils.mcp;

public class MovingObjectPosition {
    private BlockPos blockPos;
    public MovingObjectPosition.MovingObjectType typeOfHit;
    public EnumFacing sideHit;
    public Vec3 hitVec;

    public MovingObjectPosition(Vec3 hitVecIn, EnumFacing facing, BlockPos blockPosIn)
    {
        this(MovingObjectType.BLOCK, hitVecIn, facing, blockPosIn);
    }

    public MovingObjectPosition(Vec3 p_i45552_1_, EnumFacing facing)
    {
        this(MovingObjectType.BLOCK, p_i45552_1_, facing, BlockPos.ORIGIN);
    }

    public MovingObjectPosition(MovingObjectType typeOfHitIn, Vec3 hitVecIn, EnumFacing sideHitIn, BlockPos blockPosIn)
    {
        this.typeOfHit = typeOfHitIn;
        this.blockPos = blockPosIn;
        this.sideHit = sideHitIn;
        this.hitVec = new Vec3(hitVecIn.xCoord, hitVecIn.yCoord, hitVecIn.zCoord);
    }

    public MovingObjectPosition(Vec3 hitVecIn)
    {
        this.typeOfHit = MovingObjectType.ENTITY;
        this.hitVec = hitVecIn;
    }

    public String toString() {
        return "HitResult{type=" + this.typeOfHit + ", blockpos=" + this.blockPos + ", f=" + this.sideHit + ", pos=" + this.hitVec + '}';
    }

    public static enum MovingObjectType {
        MISS,
        BLOCK,
        ENTITY;
    }
}
