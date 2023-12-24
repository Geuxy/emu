package ac.emu.check.impl.speed;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.PlayerData;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

@CheckInfo(name = "Speed", description = "Invalid horizontal movement", type = "D")
public class SpeedD extends Check {

    public SpeedD(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMovement()) {
            boolean exempt = isExempt(
                ExemptType.TELEPORTED,
                ExemptType.SPAWNED
            );

            double maxSpeed = 2D;
            double speed = data.getMovementData().getSpeed();

            boolean invalid = speed > maxSpeed;

            if(invalid && !exempt) {
                if(thriveBuffer() > 1) {
                    this.fail(String.format("speed=%.5f, max=%.5f", speed, maxSpeed));
                }
            }
            this.decayBuffer(0.02);
        }
    }

}
