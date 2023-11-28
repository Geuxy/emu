package me.geuxy.emu.check.impl.combat.reach;

import me.geuxy.emu.Emu;
import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.exempt.ExemptType;
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
        if(packet.isFlying() && data.getCombatProcessor().getHitTicks() < 2) {
            Entity entity = data.getCombatProcessor().getHitEntity();

            if(!(entity instanceof Player)) {
                return;
            }

            PlayerData playerData = Emu.INSTANCE.getDataManager().get((Player) entity);

            if(playerData == null) {
                return;
            }

            double distance = data.getPositionProcessor().getTo().distance(playerData.getPositionProcessor().getTo()) - 0.5D;
            double lastDistance = data.getPositionProcessor().getFrom().distance(playerData.getPositionProcessor().getFrom()) - 0.5D;

            boolean exempt = isExempt(
                ExemptType.TELEPORTED,
                ExemptType.SPAWNED,
                ExemptType.CHUNK
            );

            boolean invalid = distance > 3.1 && distance < lastDistance;

            if(invalid && !exempt) {
                this.fail("dist=" + distance);
            }

        }
    }

}
