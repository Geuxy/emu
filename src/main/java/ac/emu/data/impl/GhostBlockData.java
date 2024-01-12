package ac.emu.data.impl;

import ac.emu.config.ConfigValues;
import ac.emu.data.Data;
import ac.emu.user.EmuPlayer;
import ac.emu.packet.Packet;
import ac.emu.utils.StaffUtil;

import lombok.Getter;

import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientQueryBlockNBT;

import org.bukkit.Location;

import java.util.concurrent.ThreadLocalRandom;

@Getter
public class GhostBlockData extends Data {

    private boolean onGhostBlock;

    private double ghostBlockTicks;

    public GhostBlockData(EmuPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(packet.isMovement()) {
            boolean clientGround = data.getMovementData().isClientGround();
            boolean serverGround = data.getMovementData().isServerGround();

            this.onGhostBlock = clientGround && !serverGround && data.getMovementData().isMathGround();

            if(onGhostBlock) {
                this.ghostBlockTicks++;
            } else {
                this.ghostBlockTicks = Math.max(0, this.ghostBlockTicks - 0.05);
            }

            int ping = (int) Math.min(0, data.getActionData().getPing() - 1);

            if(onGhostBlock && data.getMovementData().getLastLocationOnGround() != null && ghostBlockTicks > 1 + Math.min(200, ping) / 100) {
                Location blockLoc = data.getMovementData().getTo().subtract(0, 1, 0);
                WrapperPlayClientQueryBlockNBT blockNBT = new WrapperPlayClientQueryBlockNBT(ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE), new Vector3i(blockLoc.getBlockX(), blockLoc.getBlockY(), blockLoc.getBlockZ()));
                data.getUtilities().sendPacket(blockNBT);
                data.getSetbackData().setback();

                StaffUtil.sendAlert(ConfigValues.MESSAGE_GBP.stringValue(), null, data.getPlayer());
            }
        }
    }

}
