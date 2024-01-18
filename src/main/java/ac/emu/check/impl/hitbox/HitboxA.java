package ac.emu.check.impl.hitbox;

import ac.emu.Emu;
import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;
import ac.emu.data.profile.EmuPlayer;
import ac.emu.utils.bukkit.BukkitUtil;
import ac.emu.utils.mcp.AxisAlignedBB;
import ac.emu.utils.mcp.MathHelper;
import ac.emu.utils.mcp.MovingObjectPosition;
import ac.emu.utils.mcp.Vec3;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;

import org.bukkit.Location;

@CheckInfo(name = "Hitbox", description = "Missed players hitbox", type = "A")
public class HitboxA extends Check {

    public HitboxA(EmuPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(packet.getType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity wrapper = new WrapperPlayClientInteractEntity((PacketReceiveEvent) packet.getEvent());

            if(wrapper.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                EmuPlayer target = Emu.INSTANCE.getDataManager().get(BukkitUtil.getPlayer(wrapper.getEntityId()));

                if(target == null) {
                    return;
                }

                boolean intersection = profile.getPastLocations().stream().anyMatch(l -> {
                    for (boolean sneak : new boolean[] {true, false}) {
                        AxisAlignedBB box = target.getMovementData().getBoundingBox().expand(0.1, 0, 0.1);

                        if (isExempt(ExemptType.NOT_MOVING)) {
                            box = box.expand(0.03, 0.03, 0.03);
                        }

                        MovingObjectPosition result = this.rayCast(l.getYaw(), l.getPitch(), sneak, box);

                        return (result != null && result.hitVec != null) || isInsideVec(box, l.toVec3());
                    }

                    return false;
                });

                boolean exempt = isExempt(ExemptType.CREATIVE, ExemptType.IN_VEHICLE);

                if(!intersection && !exempt) {
                    this.fail();

                } else {
                    this.reward();
                }
            }
        }
    }

    private MovingObjectPosition rayCast(float yaw, float pitch, boolean sneak, AxisAlignedBB box) {
        Location position = profile.getMovementData().getFrom();

        double lastX = position.getX();
        double lastY = position.getY();
        double lastZ = position.getZ();

        Vec3 vec3 = new Vec3(lastX, lastY + profile.getEyeHeight(sneak), lastZ);
        Vec3 rotation = this.getVectorForRotation(pitch, yaw);
        Vec3 vec32 = vec3.add(new Vec3(rotation.xCoord * 3, rotation.yCoord * 3, rotation.zCoord * 3));

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
