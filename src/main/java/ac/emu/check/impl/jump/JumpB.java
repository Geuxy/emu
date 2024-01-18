package ac.emu.check.impl.jump;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.profile.EmuPlayer;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;
import org.bukkit.potion.PotionEffectType;

@CheckInfo(name = "Jump", description = "Invalid horizontal movement after jumping", type = "B")
public class JumpB extends Check {

    public JumpB(EmuPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(packet.isMovement()) {
            if(profile.getMovementData().isLastClientGround() && !profile.getMovementData().isClientGround()) {
                double speed = profile.getMovementData().getSpeed();

                double maxSpeed = 0.62;

                if(isExempt(ExemptType.NEAR_STAIRS) || isExempt(ExemptType.UNDER_BLOCK)) {
                    maxSpeed += 0.91;
                }

                if (profile.getMovementData().getSinceOnIceTicks() < 20 || isExempt(ExemptType.ON_SLIME)) {
                    maxSpeed += 0.57;
                }

                maxSpeed += 0.02759 * profile.getAmplifier(PotionEffectType.SPEED);

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

                boolean step = profile.getMovementData().isLastMathGround() && profile.getMovementData().isMathGround();

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
