package ac.emu.check.impl.ground;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.profile.EmuPlayer;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

@CheckInfo(name = "Ground", description = "Spoofing on ground", type = "A")
public class GroundA extends Check {

    public GroundA(EmuPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(packet.isMovement()) {
            boolean exempt = isExempt(
                ExemptType.SPAWNED,
                ExemptType.TELEPORTED,
                ExemptType.IN_VEHICLE,
                ExemptType.BOAT,
                ExemptType.NEAR_WALL
            );

            double mathY = profile.getMovementData().getY() % 0.015625;

            if(profile.getMovementData().isClientGround() && mathY != 0 && !profile.getMovementData().isServerGround() && !exempt) {
                this.fail(String.format("math=%.15f", mathY));
            } else {
                this.reward();
            }
        }
    }

}
