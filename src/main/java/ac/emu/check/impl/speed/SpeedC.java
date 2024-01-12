package ac.emu.check.impl.speed;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.user.EmuPlayer;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;
import org.bukkit.potion.PotionEffectType;

@CheckInfo(name = "Speed", description = "Invalid horizontal movement in air", type = "C")
public class SpeedC extends Check {

    public SpeedC(EmuPlayer data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMovement()) {
            boolean exempt = isExempt(
                ExemptType.VELOCITY,
                ExemptType.EXPLOSION,
                ExemptType.TELEPORTED,
                ExemptType.SPAWNED,
                ExemptType.ALLOWED_FLIGHT,
                ExemptType.IN_VEHICLE
            );

            double lastSpeed = data.getMovementData().getLastSpeed();
            double speed = data.getMovementData().getSpeed();

            double prediction = lastSpeed * 1.991;

            prediction += 0.2 * data.getUtilities().getAmplifier(PotionEffectType.SPEED);

            double difference = speed - prediction;

            boolean invalid = difference > 6E-4 && speed > 0.61 && data.getMovementData().isLastClientGround() && !data.getMovementData().isClientGround();

            if(invalid && !exempt) {
                this.fail(String.format("diff=%.5f, predicted=%.5f, speed=%.5f", difference, prediction, speed));
            } else {
                this.reward();
            }
        }
    }

}
