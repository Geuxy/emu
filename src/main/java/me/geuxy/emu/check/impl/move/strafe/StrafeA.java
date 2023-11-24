package me.geuxy.emu.check.impl.move.strafe;

import me.geuxy.emu.api.check.CheckInfo;
import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;
import me.geuxy.emu.utils.MathUtil;
import org.bukkit.Location;
import org.bukkit.util.Vector;

@CheckInfo(
    name = "Strafe",
    description = "Moved wrong in air",
    type = "A"
)
// Check by PhoenixHaven
public class StrafeA extends AbstractCheck {

    public StrafeA(PlayerData data) {
        super(data);
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet.isMove()) {
            double angle = Math.abs(MathUtil.getAngleRotation(data.getPlayer().getLocation().clone(),
                    data.getPositionProcessor().getLastLocation().clone()));

            if (angle % 45 == 0) {
                if (increaseBuffer() >= 2) {
                    fail("Strafe", String.valueOf(angle));
                }
            } else {
                reduceBuffer(0.025);
            }
        }
    }

}
