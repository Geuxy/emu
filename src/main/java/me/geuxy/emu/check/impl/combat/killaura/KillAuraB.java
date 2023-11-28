package me.geuxy.emu.check.impl.combat.killaura;

import io.github.retrooper.packetevents.packetwrappers.play.in.blockdig.WrappedPacketInBlockDig;
import io.github.retrooper.packetevents.packetwrappers.play.in.blockplace.WrappedPacketInBlockPlace;
import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;

@CheckInfo(
    name = "KillAura",
    description = "Attacked while blocking",
    type = "B"
)
public class KillAuraB extends AbstractCheck {

    private boolean block, use;

    public KillAuraB(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isBlockDig()) {
            WrappedPacketInBlockDig wrapper = new WrappedPacketInBlockDig(packet.getRaw());

            if(wrapper.getDigType().equals(WrappedPacketInBlockDig.PlayerDigType.RELEASE_USE_ITEM)) {
                this.block = true;
            }
        }

        if(data.getCombatProcessor().getHitTicks() <= 1) {
            this.use = true;
        }

        if(packet.isFlying()) {
            boolean invalid = block && use;

            if(invalid) {
                this.fail();
            }
            this.block = false;
            this.use = false;
        }
    }

}
