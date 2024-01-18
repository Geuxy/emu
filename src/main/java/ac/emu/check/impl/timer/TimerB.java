package ac.emu.check.impl.timer;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;
import ac.emu.data.profile.EmuPlayer;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;

@CheckInfo(name = "Timer", description = "Invalid timer balance", type = "B")
public class TimerB extends Check {

    private long balance = -50;
    private long lastFlying;

    public TimerB(EmuPlayer profile) {
        super(profile);
    }

    @Override
    public void handle(Packet packet) {
        if(packet.isMovement()) {
            check: {
                boolean exempt = profile.getActionData().getLivingTicks() < 200 || lastFlying == 0;

                if(exempt) {
                    break check;
                }

                if(profile.getActionData().getTeleportTicks() == 1) {
                    this.balance -= 50;
                }

                this.balance += 50;
                this.balance -= packet.getTimeStamp() - this.lastFlying;

                boolean invalid = balance > 0 || balance < -5000;

                if(invalid) {
                    this.fail("balance=" + balance);
                    this.balance = -50;

                } else {
                    this.reward();
                }
            }
            this.lastFlying = packet.getTimeStamp();
        }
    }

}
