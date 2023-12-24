package ac.emu.check.impl.fly;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.PlayerData;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

@CheckInfo(name = "Fly", description = "Invalid vertical movement", type = "A")
public class FlyA extends Check {

    public FlyA(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMovement()) {
            if(!data.getMovementData().isLastClientGround() && !data.getMovementData().isClientGround()) {
                double lastDeltaY = data.getMovementData().getLastDeltaY();
                double deltaY = data.getMovementData().getDeltaY();

                int airTicks = data.getMovementData().getAirTicks();

                double predicted = (lastDeltaY - 0.08) * 0.9800000190734863;
                double falseFix = Math.abs(predicted) < 0.005 ? 0D : predicted;
                double difference = Math.abs(deltaY - falseFix);

                boolean exempt = isExempt(
                    ExemptType.TELEPORTED,
                    ExemptType.IN_VEHICLE,
                    ExemptType.ON_CLIMBABLE,
                    ExemptType.ALLOWED_FLIGHT,
                    ExemptType.SPAWNED,
                    ExemptType.IN_WEB,
                    ExemptType.IN_LIQUID,
                    ExemptType.CHUNK,
                    ExemptType.UNDER_BLOCK,
                    ExemptType.BOAT,
                    ExemptType.EXPLOSION,
                    ExemptType.ON_SLIME,
                    ExemptType.VELOCITY
                ) || /* difference == 0.7840000152587834E-1D ||
                    difference == 0.6349722830977471 || */
                    difference == 0.01524397154484497 ||
                    isExempt(ExemptType.VELOCITY_LONG) && isExempt(ExemptType.NEAR_WALL);

                boolean invalid = difference > 1E-13D;

                if (invalid && !exempt) {
                    if (thriveBuffer() > 2) {
                        this.fail(String.format("diff=%.5f, tick=%d, predicted=%.5f, deltaY=%.5f", difference, airTicks, predicted, deltaY));
                    }
                } else {
                    this.decayBuffer(0.01D);
                }
            }
        }
    }

}
