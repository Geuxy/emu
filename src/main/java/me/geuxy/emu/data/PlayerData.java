package me.geuxy.emu.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import lombok.Setter;
import me.geuxy.emu.Emu;
import me.geuxy.emu.check.AbstractCheck;
import me.geuxy.emu.data.processors.*;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

@Getter @RequiredArgsConstructor
public class PlayerData {

    private final Player player;

    private final PositionProcessor positionProcessor = new PositionProcessor(this);
    private final VelocityProcessor velocityProcessor = new VelocityProcessor(this);
    private final ActionProcessor actionProcessor = new ActionProcessor(this);
    private final CombatProcessor combatProcessor = new CombatProcessor(this);
    private final ExemptProcessor exemptProcessor = new ExemptProcessor(this);

    private final List<AbstractCheck> checks = Emu.INSTANCE.getCheckManager().loadChecks(this);

    @Setter
    private boolean alertsEnabled;

    public void handleTeleport() {
        actionProcessor.handleTeleport();
    }

    public double getSpeedMultiplier() {
        PotionEffect effect = this.getEffect(PotionEffectType.SPEED);

        if(effect != null) {
            return ((effect.getAmplifier() + 1) * 0.062f) + ((player.getWalkSpeed() - 0.2f) * 1.6f);
        }

        return 0;
    }

    public PotionEffect getEffect(PotionEffectType type) {
        return player.getActivePotionEffects().stream().filter(e -> e.getType().equals(type)).findFirst().orElse(null);
    }

}
