package ac.emu.check.impl.inventory;

import ac.emu.check.Check;
import ac.emu.check.CheckInfo;
import ac.emu.data.profile.EmuPlayer;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;

@CheckInfo(name = "Inventory", description = "Moving while in gui", type = "A")
public class InventoryA extends Check {

    public InventoryA(EmuPlayer profile) {
        super(profile);
    }

    @Override
    public void handle(Packet packet) {
        if(packet.getType() == PacketType.Play.Client.CLICK_WINDOW) {
            double accel = profile.getMovementData().getSpeed() - profile.getMovementData().getLastSpeed();

            boolean exempt = isExempt(ExemptType.TELEPORTED, ExemptType.IN_LIQUID, ExemptType.EXPLOSION, ExemptType.VELOCITY, ExemptType.ON_CLIMBABLE, ExemptType.CREATIVE) || profile.getMovementData().getSpeed() <= 0.01;

            boolean invalid = accel >= 0;
            boolean invalid2 = profile.getMovementData().getSpeed() >= profile.getBaseSpeed(0.1) && profile.getMovementData().isClientGround();

            if((invalid || invalid2) && !exempt) {
                this.fail();

            } else {
                this.reward();
            }
        }
    }

}
