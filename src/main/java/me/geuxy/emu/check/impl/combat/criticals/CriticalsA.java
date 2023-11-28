package me.geuxy.emu.check.impl.combat.criticals;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.exempt.ExemptType;
import me.geuxy.emu.packet.Packet;

@CheckInfo(
    name = "Criticals",
    description = "Mini jumped to get a critical hit",
    type = "A"
)
public class CriticalsA extends AbstractCheck {

    private boolean jumped, ground;
    private double deltaY;

    public CriticalsA(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isFlying() && data.getCombatProcessor().getHitTicks() < 5) {
            int ticks = data.getCombatProcessor().getHitTicks();

            if(ticks < 2) {
                double deltaY = data.getPositionProcessor().getDeltaY();
                this.jumped = deltaY > 0;
                this.deltaY = deltaY;
            } else {
                this.ground = data.getPositionProcessor().isClientGround();
            }

            boolean exempt = isExempt(
                ExemptType.SPAWNED,
                ExemptType.TELEPORTED,
                ExemptType.ON_SLIME,
                ExemptType.EXPLOSION,
                ExemptType.ALLOWED_FLIGHT,
                ExemptType.IN_LIQUID,
                ExemptType.IN_VEHICLE,
                ExemptType.ON_CLIMBABLE
            );

            if(ticks == 4) {
                boolean invalid = jumped && ground;

                if(invalid && !exempt) {
                    this.fail("deltaY=" + deltaY);
                }
                this.deltaY = 0;
                this.jumped = false;
                this.ground = false;
            }
        }
    }

}
