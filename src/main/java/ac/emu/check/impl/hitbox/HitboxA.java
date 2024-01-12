package ac.emu.check.impl.hitbox;

import ac.emu.Emu;
import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;
import ac.emu.user.EmuPlayer;
import ac.emu.utils.location.PastLocation;
import ac.emu.utils.mcp.AxisAlignedBB;
import ac.emu.utils.mcp.MathHelper;
import ac.emu.utils.mcp.MovingObjectPosition;
import ac.emu.utils.mcp.Vec3;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

// This code is based of AHMs HitboxA check
// https://github.com/Tecnio/AntiHaxerman/blob/master/src/main/java/me/tecnio/ahm/check/impl/hitbox/HitboxA.java
@CheckInfo(name = "Hitbox", description = "Missed players hitbox", type = "A")
public class HitboxA extends Check {

    private final boolean[] BOOLEANS = {true, false};

    private EmuPlayer target;

    public HitboxA(EmuPlayer data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMovement() && target != null) {
            boolean intersection = false;

            double x = target.getMovementData().getX();
            double y = target.getMovementData().getY();
            double z = target.getMovementData().getZ();

            AxisAlignedBB box = new AxisAlignedBB(x - 0.4, y - 0.1, z - 0.4, x + 0.4, y + 1.9, z + 0.4);

            if (isExempt(ExemptType.NOT_MOVING)) {
                box = box.expand(0.03, 0.03, 0.03);
            }

            for (boolean rotation : BOOLEANS) {
                for (boolean sneak : BOOLEANS) {
                    float yaw = rotation ? data.getMovementData().getYaw() : data.getMovementData().getLastYaw();
                    float pitch = rotation ? data.getMovementData().getPitch() : data.getMovementData().getLastPitch();

                    MovingObjectPosition result = this.rayCast(yaw, pitch, sneak, box);

                    intersection |= result != null && result.hitVec != null;
                }
            }

            boolean exempt = isExempt(ExemptType.CREATIVE, ExemptType.IN_VEHICLE);

            if(intersection && !exempt) {
                this.fail("missed=true");
            } else {
                this.reward();
            }

            this.target = null;
        }

        if(packet.getType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity wrapper = new WrapperPlayClientInteractEntity((PacketReceiveEvent) packet.getEvent());

            if(wrapper.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                Bukkit.getOnlinePlayers().stream().filter(p -> p.getEntityId() == wrapper.getEntityId()).findFirst().ifPresent(player -> this.target = Emu.INSTANCE.getDataManager().get(player));
            }
        }
    }

    private MovingObjectPosition rayCast(float yaw, float pitch, boolean sneak, AxisAlignedBB box) {
        Location position = data.getMovementData().getFrom();

        double lastX = position.getX();
        double lastY = position.getY();
        double lastZ = position.getZ();

        Vec3 vec3 = new Vec3(lastX, lastY + data.getUtilities().getEyeHeight(sneak), lastZ);
        Vec3 rotation = this.getVectorForRotation(pitch, yaw);
        Vec3 vec32 = vec3.add(new Vec3(rotation.xCoord * 3.0D, rotation.yCoord * 3.0D, rotation.zCoord * 3.0D));

        return box.calculateIntercept(vec3, vec32);
    }

    private Vec3 getVectorForRotation(float pitch, float yaw) {
        float f = 0.017453292F;

        float sinYaw = MathHelper.sin(-yaw * f - (float) Math.PI);
        float cosYaw = MathHelper.cos(-yaw * f - (float) Math.PI);
        float sinPitch = MathHelper.sin(-pitch * f);
        float cosPitch = -MathHelper.cos(-pitch * f);

        double x = sinYaw * cosPitch;
        double z = cosYaw * cosPitch;

        return new Vec3(x, sinPitch, z);
    }

    public static boolean isInsideVec(AxisAlignedBB box, Vec3 vec) {
        return vec.xCoord > box.getMinX() && vec.xCoord < box.getMaxX() && (vec.yCoord > box.getMinY() && vec.yCoord < box.getMaxY() && vec.zCoord > box.getMinZ() && vec.zCoord < box.getMaxZ());
    }

}
