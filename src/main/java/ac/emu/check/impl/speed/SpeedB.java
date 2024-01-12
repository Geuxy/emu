package ac.emu.check.impl.speed;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.user.EmuPlayer;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

@CheckInfo(name = "Speed", description = "Invalid horizontal movement on ground", type = "B")
public class SpeedB extends Check {

    private int offSlimeTicks;

    public SpeedB(EmuPlayer data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMovement() && data.getMovementData().isClientGround()) {
            int groundTicks = data.getMovementData().getGroundTicks();
            int lastAirTicks = data.getMovementData().getLastAirTicks();
            int sinceOnIceTicks = data.getMovementData().getSinceOnIceTicks();

            double speed = data.getMovementData().getSpeed();

            boolean exempt = isExempt(
                ExemptType.VELOCITY,
                ExemptType.TELEPORTED,
                ExemptType.SPAWNED,
                ExemptType.ALLOWED_FLIGHT,
                ExemptType.IN_VEHICLE
            );

            double maxSpeed = data.getUtilities().getBaseGroundSpeed();

            this.offSlimeTicks = isExempt(ExemptType.ON_SLIME) ? 0 : ++offSlimeTicks;

            if(groundTicks < 8) {
                maxSpeed += groundTicks == 1 ? (lastAirTicks >= 11 && lastAirTicks <= 12 ? 0.024 : 0.122) : (0.226 / groundTicks);
            }

            if(isExempt(ExemptType.NEAR_STAIRS) || data.getMovementData().getSinceUnderBlockTicks() < 7) {
                maxSpeed += 0.91;
            }

            if(sinceOnIceTicks < 20 || offSlimeTicks < 20) {
                maxSpeed += 0.34;
            }

            boolean invalid = speed >= maxSpeed;

            if(invalid && !exempt) {
                this.fail(String.format("tick=%d, max=%.5f, speed=%.5f, ice=%b, slime=%b", groundTicks, maxSpeed, speed, sinceOnIceTicks == 0, offSlimeTicks == 0));
            } else {
                this.reward();
            }
        }
    }

}
