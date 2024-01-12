package ac.emu.user;

import ac.emu.Emu;
import ac.emu.data.impl.*;
import ac.emu.packet.Packet;
import ac.emu.check.Check;
import ac.emu.utils.PlayerUtil;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.bukkit.entity.Player;

import java.util.List;

@Getter @RequiredArgsConstructor
public class EmuPlayer {

    private final Player player;

    private final CombatData combatData = new CombatData(this);
    private final MovementData movementData = new MovementData(this);
    private final VelocityData velocityData = new VelocityData(this);
    private final ActionData actionData = new ActionData(this);
    private final ExemptData exemptData = new ExemptData(this);
    private final GhostBlockData ghostBlockData = new GhostBlockData(this);
    private final SetbackData setbackData = new SetbackData(this);

    private final PlayerUtil utilities = new PlayerUtil(this);

    private final List<Check> checks = Emu.INSTANCE.getCheckManager().loadChecks(this);

    public void handle(Packet packet) {
        movementData.handle(packet);
        velocityData.handle(packet);
        actionData.handle(packet);
        setbackData.handle(packet);
        ghostBlockData.handle(packet);

        checks.forEach(c -> c.processPacket(packet));
    }

}
