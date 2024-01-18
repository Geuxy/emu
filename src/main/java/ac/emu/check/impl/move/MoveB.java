package ac.emu.check.impl.move;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.profile.EmuPlayer;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

@CheckInfo(name = "Move", description = "Invalid movement", type = "B")
public class MoveB extends Check {

    public MoveB(EmuPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(packet.isMovement()) {
            double deltaX = profile.getMovementData().getDeltaX();
            double deltaY = profile.getMovementData().getDeltaY();
            double deltaZ = profile.getMovementData().getDeltaZ();

            boolean exempt = isExempt(
                ExemptType.TELEPORTED,
                ExemptType.SPAWNED,
                ExemptType.ON_SLIME,
                ExemptType.IN_VEHICLE,
                ExemptType.BOAT
            );

            boolean invalid = deltaX >= 3 || deltaY >= 3 || deltaZ >= 3;

            if(invalid && !exempt) {
                this.fail();
            } else {
                this.reward();
            }
        }
    }

}
