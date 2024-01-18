package ac.emu.check.impl.move;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.profile.EmuPlayer;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

// Ground speed detection
@CheckInfo(name = "Move", description = "Invalid jump", type = "A")
public class MoveA extends Check {

    public MoveA(EmuPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(packet.isMovement()) {
            double deltaY = profile.getMovementData().getDeltaY();

            int airTicks = profile.getMovementData().getAirTicks();
            int lastAirTicks = profile.getMovementData().getLastAirTicks();

            boolean exempt = isExempt(
                ExemptType.TELEPORTED,
                ExemptType.ALLOWED_FLIGHT,
                ExemptType.IN_VEHICLE,
                ExemptType.SPAWNED,
                ExemptType.UNDER_BLOCK,
                ExemptType.STEPPING,
                ExemptType.ON_CLIMBABLE,
                ExemptType.BOAT,
                ExemptType.VELOCITY,
                ExemptType.ON_SLIME,
                ExemptType.SETBACK
            );

            boolean step = profile.getMovementData().isLastMathGround() && profile.getMovementData().isMathGround();

            boolean invalid = airTicks == 1 && deltaY > -0.07D && deltaY < 0 && !step;
            boolean invalid2 = lastAirTicks == 1 && profile.getMovementData().isClientGround() && !step;

            if((invalid || invalid2) && !exempt) {
                this.fail(String.format("deltaY=%.5f", deltaY));
            } else {
                this.reward();
            }
        }
    }

}
