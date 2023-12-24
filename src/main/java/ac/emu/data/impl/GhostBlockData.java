package ac.emu.data.impl;

import ac.emu.config.ConfigValues;
import ac.emu.data.Data;
import ac.emu.data.PlayerData;
import ac.emu.packet.Packet;
import ac.emu.utils.StaffUtil;

import lombok.Getter;

@Getter
public class GhostBlockData extends Data {

    private boolean onGhostBlock;

    private double ghostBlockTicks;

    private int setbacks;

    public GhostBlockData(PlayerData data) {
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
                this.ghostBlockTicks = Math.max(0, this.ghostBlockTicks - 0.01);
            }

            int ping = (int) Math.min(0, data.getActionData().getPing() - 1);

            if(onGhostBlock && data.getMovementData().getLastLocationOnGround() != null && ghostBlockTicks > Math.min(ping, 200) / 100) {
                data.getSetbackData().setback();

                if(System.currentTimeMillis() - data.getSetbackData().getLastSetback() > 1000) {
                    StaffUtil.sendAlert(ConfigValues.MESSAGE_GBP.stringValue() + " x" + setbacks, null, data.getPlayer());
                }
                this.setbacks++;
            }
        }
    }

}
