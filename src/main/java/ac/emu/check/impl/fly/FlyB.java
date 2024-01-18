package ac.emu.check.impl.fly;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.profile.EmuPlayer;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

@CheckInfo(name = "Fly", description = "Sudden motion reduction", type = "B")
public class FlyB extends Check {

    public FlyB(EmuPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(packet.isMovement()) {
            int airTicks = profile.getMovementData().getAirTicks();

            double lastDeltaY = profile.getMovementData().getLastDeltaY();
            double deltaY = profile.getMovementData().getDeltaY();

            boolean exempt = isExempt(
                ExemptType.TELEPORTED,
                ExemptType.ALLOWED_FLIGHT,
                ExemptType.IN_VEHICLE,
                ExemptType.SPAWNED,
                ExemptType.UNDER_BLOCK,
                ExemptType.STEPPING,
                ExemptType.ON_CLIMBABLE,
                ExemptType.BOAT,
                ExemptType.VELOCITY_LONG,
                ExemptType.ON_SLIME
            );

            if(isExempt(ExemptType.STEPPING) && deltaY <= 0.5) {
                this.resetBuffer();
            }

            boolean step = profile.getMovementData().isMathGround() && profile.getMovementData().isLastMathGround();

            boolean invalid = lastDeltaY > 0.1D && deltaY <= 0.02D && !step;

            if(invalid && !exempt) {
                this.fail(String.format("deltaY=%.5f, lastDeltaY=%.5f, tick=%d", deltaY, lastDeltaY, airTicks));
            }
            this.reward();
        }
    }

}
