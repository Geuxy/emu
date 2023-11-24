package me.geuxy.emu.check;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import me.geuxy.emu.api.check.CheckInfo;
import me.geuxy.emu.config.ConfigValues;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.packet.Packet;
import me.geuxy.emu.utils.StringUtils;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

@Getter @RequiredArgsConstructor
public abstract class AbstractCheck {

    private final CheckInfo info = this.getClass().getAnnotation(CheckInfo.class);

    public final PlayerData data;

    private int level;
    private double buffer;

    public abstract void processPacket(Packet packet);

    public void fail(String... values) {
        level++;

        String str = "";
        for(String value : values) {
            if(!str.isEmpty()) {
                str += "\n";
            }
            str += value;
        }

            TextComponent comp = new TextComponent();
            comp.setText(StringUtils.replace(this, data.getPlayer(), ConfigValues.MESSAGE_FAIL.stringValue()));
            comp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(str).create()));
            Bukkit.spigot().broadcast(comp);
    }

    public double increaseBuffer() {
        return this.buffer = Math.min(100, this.buffer + 1);
    }

    public double reduceBuffer(double buffer) {
        return this.buffer = Math.max(0, this.buffer - buffer);
    }

    public String getName() {
        return info.name();
    }

    public String getDescription() {
        return info.description();
    }

    public String getType() {
        return info.type();
    }

    public boolean isEnabled() {
        return ConfigValues.Checks.isEnabled(this);
    }

    public int getMaxLevel() {
        return ConfigValues.Checks.getMaxLevel(this);
    }

}
