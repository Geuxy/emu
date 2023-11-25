package me.geuxy.emu.check.impl.move.jump;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;

import me.geuxy.emu.utils.entity.PlayerUtil;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@CheckInfo(
    name = "Jump",
    description = "Invalid jump height",
    type = "A"
)
public class JumpA extends AbstractCheck {

    public JumpA(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isFlying()) {
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
                    data.BLOCK_ABOVE ||
                    PlayerUtil.isNearBoat(data.getPlayer()) ||
                    difference == 0.015555072702198913D ||
                    (difference == 0.08000001311302185D && deltaY == 0.5D) ||
                    difference == 0.4983999884128574D ||
                    difference == 0.4983999884128565D ||

                    // Falling when water below
                    difference == 0.49839998841285693D;

                boolean invalid = difference > 1E-4;

                if(invalid && !exempt) {
                    this.fail("diff=" + difference, "delta=" + deltaY, "expected=" + maxJumpHeight);
                }
            }
        }
    }

}
