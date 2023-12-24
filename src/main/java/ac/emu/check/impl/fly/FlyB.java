package ac.emu.check.impl.fly;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.PlayerData;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

@CheckInfo(name = "Fly", description = "Sudden motion reduction", type = "B")
public class FlyB extends Check {

    public FlyB(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMovement()) {
            int airTicks = data.getMovementData().getAirTicks();

            double lastDeltaY = data.getMovementData().getLastDeltaY();
            double deltaY = data.getMovementData().getDeltaY();

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

            boolean step = data.getMovementData().isMathGround() && data.getMovementData().isLastMathGround();

            boolean invalid = lastDeltaY > 0.1D && deltaY <= 0.02D && !step;

            if(invalid && !exempt) {
                if(thriveBuffer() > 1) {
                    this.fail(String.format("deltaY=%.5f, lastDeltaY=%.5f, tick=%d", deltaY, lastDeltaY, airTicks));
                }
            }
            this.decayBuffer(0.02);
        }
    }

}
