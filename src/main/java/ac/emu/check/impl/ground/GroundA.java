package ac.emu.check.impl.ground;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.PlayerData;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

@CheckInfo(name = "Ground", description = "Spoofing on ground", type = "A")
public class GroundA extends Check {

    public GroundA(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMovement()) {
            boolean exempt = isExempt(
                ExemptType.SPAWNED,
                ExemptType.TELEPORTED,
                ExemptType.IN_VEHICLE,
                ExemptType.BOAT,
                ExemptType.NEAR_WALL
            );

            double mathY = data.getMovementData().getY() % 0.015625;

            if(data.getMovementData().isClientGround() && mathY != 0 && !data.getMovementData().isServerGround() && !exempt) {
                if(thriveBuffer() > 1) {
                    this.fail(String.format("math=%.15f", mathY));
                    System.out.println(mathY);
                }
            } else {
                this.decayBuffer(0.025);
            }
        }
    }

}
