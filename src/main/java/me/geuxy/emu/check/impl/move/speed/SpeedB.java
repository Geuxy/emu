package me.geuxy.emu.check.impl.move.speed;

import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.check.CheckInfo;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;
import me.geuxy.emu.utils.world.BlockUtils;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@CheckInfo(
    name = "Speed",
    description = "Invalid horizontal movement on ground",
    type = "B"
)
public class SpeedB extends AbstractCheck {

    private int offIceTicks;

    public SpeedB(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isFlying() && data.getPositionProcessor().isClientGround()) {
            int groundTicks = data.getPositionProcessor().getGroundTicks();

            double lastSpeed = data.getPositionProcessor().getLastSpeed();
            double speed = data.getPositionProcessor().getSpeed();

            boolean exempt =
                data.VELOCITY ||
                data.TELEPORTED ||
                data.LIVING ||
                data.ALLOWED_FLYING ||
                data.RIDING;

            double maxSpeed = lastSpeed;

            switch(groundTicks) {
            case 1:
                maxSpeed += 0.0029509452549265625;
                break;
            case 2:
                maxSpeed += 0.09923446024880855;
                break;
            case 3:
                maxSpeed -= 0.05973099371747159;
                break;
            case 4:
                maxSpeed -= 0.032613123573092206;
                break;
            case 5:
                maxSpeed -= 0.017806766406296526;
                break;
            case 6:
                maxSpeed -= 0.009722495179868784;
                break;
            case 7:
                maxSpeed -= 0.00530848284953106;
                break;
            case 8:
                maxSpeed -= 0.002898431929611589;
                break;
            case 9:
                maxSpeed -= 0.0015825440041481453;
                break;
            default:
                if(BlockUtils.isIce(data.getPlayer().getLocation().subtract(0D, 1D, 0D))) {
                    if(speed > 0.27021) {
                        maxSpeed -= 0.001;
                    } else {
                        maxSpeed = 0.27021;
                    }
                    this.offIceTicks = 0;
                } else {
                    this.offIceTicks = Math.min(15, offIceTicks + 1);

                    maxSpeed = 0.287;

                    if(offIceTicks < 12) {
                        maxSpeed += 0.218;
                    }
                }

                maxSpeed += data.getSpeedMultiplier();
                //maxSpeed /= data.getSlowDivider();

                PotionEffect effect = data.getEffect(PotionEffectType.SPEED);

                int amplifier = 0;

                if(effect != null) {
                    amplifier = effect.getAmplifier() + 1;
                }

                maxSpeed -= 0.006 * (1 + amplifier);
                break;
            }

            double difference = speed - maxSpeed;
            double maxDifference = groundTicks > 1 ? (groundTicks == 3 ? 0.069 : (groundTicks == 4 ? 0.415 : 0.0207)) : -1E-3;

            boolean invalid = difference > maxDifference && speed >= 0.2732;

            if(invalid && !exempt) {
                this.fail("diff=" + difference, "tick=" + groundTicks, "max=" + maxSpeed, "delta=" + speed);
            }
        }
    }

}
