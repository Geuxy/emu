package me.geuxy.emu.check.impl.move.fly;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.api.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@CheckInfo(
    name = "Fly",
    description = "Invalid jump height",
    type = "D"
)
public class FlyD extends AbstractCheck {

    public FlyD(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMove()) {
            if(data.getPositionProcessor().isLastClientGround() && !data.getPositionProcessor().isClientGround()) {
                double maxJumpHeight = 0.41999998688697815D;

                PotionEffect effect = data.getEffect(PotionEffectType.JUMP);

                if(effect != null) {
                    int amplifier = effect.getAmplifier() + 1;

                    maxJumpHeight += (1.4901161415892261E-9D * amplifier) + (amplifier * 0.1D);
                }

                double deltaY = data.getPositionProcessor().getDeltaY();

                double difference = Math.abs(deltaY - maxJumpHeight);

                boolean exempt =
                    data.TELEPORTED ||
                    data.LIVING ||
                    data.EXPLOSION ||
                    data.LIQUID ||
                    data.WEB ||
                    data.VELOCITY ||
                    data.SLIME ||
                    difference == 0.015555072702198913D ||
                    difference == 0.08000001311302185 && deltaY == 0.5D;

                //boolean invalid = deltaY > maxJumpHeight;
                //boolean invalid2 = deltaY < maxJumpHeight && deltaY > -1E-10D;

                boolean invalid = difference > 1E-4 && deltaY > -1E-10D;

                if(invalid && !exempt) {
                    this.fail("diff=" + difference, "delta=" + deltaY, "expected=" + maxJumpHeight);
                }
            }
        }
    }

}
