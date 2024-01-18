package ac.emu.check.impl.speed;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.profile.EmuPlayer;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

@CheckInfo(name = "Speed", description = "Invalid horizontal movement on ground", type = "B")
public class SpeedB extends Check {

    private int offSlimeTicks;

    public SpeedB(EmuPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(packet.isMovement() && profile.getMovementData().isClientGround()) {
            int groundTicks = profile.getMovementData().getGroundTicks();
            int lastAirTicks = profile.getMovementData().getLastAirTicks();
            int sinceOnIceTicks = profile.getMovementData().getSinceOnIceTicks();

            double speed = profile.getMovementData().getSpeed();

            boolean exempt = isExempt(
                ExemptType.VELOCITY,
                ExemptType.TELEPORTED,
                ExemptType.SPAWNED,
                ExemptType.ALLOWED_FLIGHT,
                ExemptType.IN_VEHICLE
            );

            double maxSpeed = profile.getBaseGroundSpeed();

            this.offSlimeTicks = isExempt(ExemptType.ON_SLIME) ? 0 : ++offSlimeTicks;

            if(groundTicks < 8) {
                maxSpeed += groundTicks == 1 ? (lastAirTicks >= 11 && lastAirTicks <= 12 ? 0.024 : 0.122) : (0.226 / groundTicks);
            }

            if(isExempt(ExemptType.NEAR_STAIRS) || profile.getMovementData().getSinceUnderBlockTicks() < 7) {
                maxSpeed += 0.91;
            }

            if(isExempt(ExemptType.NEAR_SLABS)) {
                maxSpeed += 0.05;
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
