package me.geuxy.emu.check.impl.player.ground;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.play.out.blockchange.WrappedPacketOutBlockChange;
import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.exempt.ExemptType;
import me.geuxy.emu.packet.Packet;
import me.geuxy.emu.utils.world.BlockUtils;

@CheckInfo(
    name = "Ground",
    description = "Spoofing on ground",
    type = "A"
)
public class GroundA extends AbstractCheck {

    public GroundA(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isFlying()) {
            boolean exempt = isExempt(
                ExemptType.SPAWNED,
                ExemptType.TELEPORTED,
                ExemptType.IN_VEHICLE,
                ExemptType.BOAT
            );

            if(BlockUtils.getSurroundingBlocks(data.getPlayer(), -1D).stream().noneMatch(b -> b.getType().isSolid())) {
                if(data.getPositionProcessor().isClientGround() && !exempt) {
                    if (thriveBuffer() > 1) {
                        this.fail("ground=true");
                    }

                    WrappedPacketOutBlockChange wrapper = new WrappedPacketOutBlockChange(data.getPositionProcessor().getTo().subtract(0D, 1D, 0D));

                    if(data.getActionProcessor().getPlaceTicks() > 3) {
                        PacketEvents.get().getPlayerUtils().sendPacket(data.getPlayer(), wrapper);
                    }
                }
            } else {
                this.decayBuffer(0.025);
            }
        }
    }

}
