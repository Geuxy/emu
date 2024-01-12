package ac.emu.check.impl.jump;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.user.EmuPlayer;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;

@CheckInfo(name = "Jump", description = "Invalid horizontal movement after jumping", type = "B")
public class JumpB extends Check {

    public JumpB(EmuPlayer data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMovement()) {
            if(data.getMovementData().isLastClientGround() && !data.getMovementData().isClientGround()) {
                double speed = data.getMovementData().getSpeed();

                double maxSpeed = 0.62;

                if(isExempt(ExemptType.NEAR_STAIRS) || isExempt(ExemptType.UNDER_BLOCK)) {
                    maxSpeed += 0.91;
                }

                if (data.getMovementData().getSinceOnIceTicks() < 20 || isExempt(ExemptType.ON_SLIME)) {
                    maxSpeed += 0.57;
                }

                maxSpeed += 0.02759 * data.getUtilities().getAmplifier(PotionEffectType.SPEED);

                double difference = speed - maxSpeed;

                boolean exempt = isExempt(
                    ExemptType.TELEPORTED,
                    ExemptType.SPAWNED,
                    ExemptType.EXPLOSION,
                    ExemptType.IN_LIQUID,
                    ExemptType.IN_WEB,
                    ExemptType.VELOCITY,
                    ExemptType.ON_SLIME,
                    ExemptType.UNDER_BLOCK,
                    ExemptType.ON_CLIMBABLE,
                    ExemptType.BOAT,
                    ExemptType.STEPPING
                );

                boolean step = data.getMovementData().isLastMathGround() && data.getMovementData().isMathGround();

                boolean invalid = difference > 0.0342 && !step;

                if(invalid && !exempt) {
                    this.fail(String.format("diff=%.5f, speed=%.5f, max=%.5f", difference, speed, maxSpeed));
                } else {
                    this.reward();
                }
            }
        }
    }

}
