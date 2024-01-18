package ac.emu.check.impl.fly;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.profile.EmuPlayer;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

@CheckInfo(name = "Fly", description = "Invalid vertical movement", type = "A")
public class FlyA extends Check {

    public FlyA(EmuPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(packet.isMovement()) {
            if(!profile.getMovementData().isLastClientGround() && !profile.getMovementData().isClientGround()) {
                double lastDeltaY = profile.getMovementData().getLastDeltaY();
                double deltaY = profile.getMovementData().getDeltaY();

                int airTicks = profile.getMovementData().getAirTicks();

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
                ) || isExempt(ExemptType.VELOCITY_LONG) && isExempt(ExemptType.NEAR_WALL)

                // The "ima kms bridging up while moving a tiny bit fix" pro max
                || (deltaY >= 0.33319999363422 && deltaY <= 0.40444491418477924 && profile.getActionData().getPlaceTicks() <= 9 && airTicks == 2 && profile.getMovementData().getSpeed() < 0.03);

                boolean invalid = difference > 1E-13D;

                if (invalid && !exempt) {
                    this.fail(String.format("diff=%.5f, tick=%d, predicted=%.5f, deltaY=%.5f", difference, airTicks, predicted, deltaY));
                } else {
                    this.reward();
                }
            }
        }
    }

}
