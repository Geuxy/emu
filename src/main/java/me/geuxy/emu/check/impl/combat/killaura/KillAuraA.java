package me.geuxy.emu.check.impl.combat.killaura;

import me.geuxy.emu.api.check.CheckInfo;
import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;

@CheckInfo(
    name = "KillAura",
    description = "Attacked when dead",
    type = "A"
)
public class KillAuraA extends AbstractCheck {

    public KillAuraA(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isUseEntity() && data.getPlayer().isDead()) {
            this.fail("nyghtfull-moment=true");
        }
    }

}
