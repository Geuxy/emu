package me.geuxy.emu.check.impl.combat.reach;

import me.geuxy.emu.Emu;
import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;


@CheckInfo(
    name = "Reach",
    description = "Interacted with entity too far away",
    type = "A"
)
public class ReachA extends AbstractCheck {

    public ReachA(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isFlying() && data.getCombatProcessor().getHitTicks() < 1) {
            Entity entity = data.getCombatProcessor().getHitEntity();

            if(!(entity instanceof Player)) {
                return;
            }

            PlayerData playerData = Emu.INSTANCE.getDataManager().get((Player) entity);

            if(playerData == null) {
                return;
            }

            double distance = data.getPlayer().getLocation().distance(playerData.getPlayer().getLocation());

            boolean exempt =
                data.TELEPORTED ||
                data.LIVING ||
                data.CHUNK;

            boolean invalid = distance > 3.08;

            if(invalid && !exempt) {
                this.fail("dist=" + distance);
            }

        }
    }

}
