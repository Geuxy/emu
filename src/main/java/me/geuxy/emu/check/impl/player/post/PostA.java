package me.geuxy.emu.check.impl.player.post;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.api.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;

@CheckInfo(
    name = "Post",
    description = "Sent flying packets late",
    type = "A"
)
public class PostA extends AbstractCheck {

    private long lastFlying;

    public PostA(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMove() || packet.isFlying() || packet.isUseEntity()) {
            long currentTime = System.currentTimeMillis();

            if(packet.isUseEntity()) {
                boolean exempt =
                    lastFlying == 0;

                long difference = currentTime - lastFlying;

                boolean invalid = difference < 5;

                if (invalid && !exempt) {
                    this.fail("diff=" + difference);
                }
            } else {
                this.lastFlying = currentTime;
            }
        }
    }

}
