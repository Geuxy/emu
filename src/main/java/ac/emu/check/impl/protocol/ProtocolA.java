package ac.emu.check.impl.protocol;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.profile.EmuPlayer;
import ac.emu.packet.Packet;

@CheckInfo(name = "Protocol", description = "Impossible Pitch", type = "A")
public class ProtocolA extends Check {

    public ProtocolA(EmuPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(packet.isRotation()) {
            float pitch = Math.abs(profile.getMovementData().getPitch());

            if(pitch > 90) {
                this.fail("pitch=" + pitch);

            } else {
                this.reward();
            }
        }
    }

}
