package me.geuxy.emu.check.impl.player.ground;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;
import me.geuxy.emu.utils.BlockUtils;

import me.geuxy.emu.utils.PlayerUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;

@CheckInfo(
    name = "Ground",
    description = "Faking on ground",
    type = "A"
)
public class GroundA extends AbstractCheck {

    private Location location;

    public GroundA(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMove()) {
            boolean exempt =
                data.LIVING ||
                data.TELEPORTED ||
                data.RIDING ||
                PlayerUtil.isNearBoat(data.getPlayer());

            if(isNearSolid()) {
                location = data.getPositionProcessor().getLastLocation();
                reduceBuffer(0.05);
            } else {
                if(data.getPositionProcessor().isClientGround() && !exempt) {
                    if (increaseBuffer() > 1) {
                        data.getPlayer().teleport(location);
                    }
                }
            }
        }
    }

    private boolean isNearSolid() {
        for(double x = -0.31; x < 0.62; x += 0.31) {
            for (double z = -0.31; z < 0.62; z += 0.31) {
                Location playerLoc = data.getPlayer().getLocation();
                Location loc = new Location(data.getPlayer().getWorld(), playerLoc.getX() + x, playerLoc.getY() - 1D, playerLoc.getZ() + z);

                Block block = BlockUtils.getBlock(loc);

                if (block != null && block.getType().isSolid()) {
                    return true;
                }
            }
        }
        return false;
    }

}
