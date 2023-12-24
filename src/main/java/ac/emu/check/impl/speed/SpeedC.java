package ac.emu.check.impl.speed;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.PlayerData;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

@CheckInfo(name = "Speed", description = "Invalid horizontal movement in air", type = "C")
public class SpeedC extends Check {

    public SpeedC(PlayerData data) {
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

            // TODO: Fix false when using swiftness
            if(data.getMovementData().isLastClientGround() && !data.getMovementData().isClientGround()) {
                double prediction = lastSpeed * 1.991;
                double difference = speed - prediction;

                boolean invalid = difference > 6E-4 && speed > 0.61;

                if(invalid && !exempt) { // 0.613 0.644 0.676
                    this.fail(String.format("diff=%.5f, predicted=%.5f, speed=%.5f", difference, prediction, speed));
                }
            }
        }
    }

}
