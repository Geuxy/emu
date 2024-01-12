package ac.emu.utils;

import ac.emu.utils.location.PastLocation;
import ac.emu.utils.mcp.MathHelper;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowConfirmation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import ac.emu.user.EmuPlayer;

import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Getter @RequiredArgsConstructor
public class PlayerUtil {

    private final EmuPlayer data;

    public boolean isOnNewerVersionThan(ClientVersion version) {
        return getClientVersion().isNewerThan(version);
    }

    public boolean isOnOlderVersionThan(ClientVersion version) {
        return getClientVersion().isOlderThan(version);
    }

    public boolean isOnNewerVersionThanOrEquals(ClientVersion version) {
        return getClientVersion().isNewerThanOrEquals(version);
    }

    public boolean isOnOlderVersionThanOrEquals(ClientVersion version) {
        return getClientVersion().isOlderThanOrEquals(version);
    }

    public boolean isClientVersion(ClientVersion version) {
        return getClientVersion() == version;
    }

    public int getAmplifier(PotionEffectType type) {
        PotionEffect effect = getEffect(type);

        if(effect != null) {
            return effect.getAmplifier() + 1;
        }

        return 0;
    }

    public ClientVersion getClientVersion() {
        return PacketEvents.getAPI().getPlayerManager().getClientVersion(data.getPlayer());
    }

    // PhoenixHaven
    public boolean isNearRideableEntity() {
        List<Entity> entities = data.getPlayer().getNearbyEntities(5, 5, 5);

        for (Entity entity : entities) {
            if (entity instanceof Boat || entity instanceof Horse || entity instanceof Minecart) {
                return true;
            }
        }

        return false;
    }

    // PhoenixHaven
    public boolean isNearBoat() {
        List<Entity> entities = data.getPlayer().getNearbyEntities(1, 1, 1);

        for (Entity entity : entities) {
            if (entity instanceof Boat) {
                return true;
            }
        }

        return false;
    }

    public double getJumpHeight() {
        return 0.41999998688697815D + (getPotionLevel(PotionEffectType.JUMP) * 0.10000000149011615D);
    }

    public double getSpeedMultiplier() {
        PotionEffect effect = getEffect(PotionEffectType.SPEED);

        if(effect != null) {
            return ((effect.getAmplifier() + 1) * 0.062f) + ((data.getPlayer().getWalkSpeed() - 0.2f) * 1.6f);
        }

        return 0;
    }

    public double getSpeedMultiplier(double speed) {
        PotionEffect effect = getEffect(PotionEffectType.SPEED);

        if(effect != null) {
            return (effect.getAmplifier() + 1) * speed;
        }

        return 0;
    }

    public PotionEffect getEffect(PotionEffectType type) {
        return data.getPlayer().getActivePotionEffects().stream().filter(e -> e.getType().equals(type)).findFirst().orElse(null);
    }

    public int getPotionLevel(PotionEffectType type) {
        PotionEffect effect = getEffect(type);

        return effect != null ? effect.getAmplifier() + 1 : 0;
    }

    public double getBaseGroundSpeed() {
        return 0.289 + (getPotionLevel(PotionEffectType.SPEED) * 0.062f) + ((data.getPlayer().getWalkSpeed() - 0.2f) * 1.6f);
    }

    public void sendPacket(PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getPlayerManager().sendPacket(data.getPlayer(), wrapper);
    }

    public short sendTransaction() {
        short id = (short) ThreadLocalRandom.current().nextInt(Short.MAX_VALUE);

        this.sendPacket(new WrapperPlayServerWindowConfirmation(0, id, false));
        return id;
    }

    public List<PastLocation> getPastLocations() {
        int ticks = MathHelper.clamp_int(Math.round((float) data.getActionData().getPing() / 50), 2, 6);

        List<PastLocation> locations = new ArrayList<>();
        List<PastLocation> pastLocations = data.getMovementData().getPastLocations();

        for(int i = 0; i < ticks; i++) {
            locations.add(pastLocations.get(pastLocations.size() - 1 - i));
        }

        return locations;
    }

    public float getEyeHeight(boolean sneak) {
        return sneak ? 1.54F : 1.62F;
    }

}
