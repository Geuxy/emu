package ac.emu.check.impl.move;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.PlayerData;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

// Ground speed detection
@CheckInfo(name = "Move", description = "Invalid jump", type = "A")
public class MoveA extends Check {

    public MoveA(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMovement()) {
            double deltaY = data.getMovementData().getDeltaY();

            int airTicks = data.getMovementData().getAirTicks();
            int lastAirTicks = data.getMovementData().getLastAirTicks();

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

            boolean step = data.getMovementData().isLastMathGround() && data.getMovementData().isMathGround();

            boolean invalid = airTicks == 1 && deltaY > -0.07D && deltaY < 0 && !step;
            boolean invalid2 = lastAirTicks == 1 && data.getMovementData().isClientGround() && !step;

            if((invalid || invalid2) && !exempt) {
                if(thriveBuffer() > 7) {
                    this.fail(String.format("deltaY=%.5f", deltaY));
                }
            } else {
                this.decayBuffer(0.1);
            }
        }
    }

}
