package me.geuxy.emu.check;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import me.geuxy.emu.config.ConfigValues;
import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.exempt.ExemptType;
import me.geuxy.emu.packet.Packet;
import me.geuxy.emu.utils.string.StringUtils;

import net.md_5.bungee.api.ChatColor;
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
        this.level++;

        String hoverText = "§7" + info.description();

        for(String value : values) {
            if(!hoverText.isEmpty()) {
                hoverText += "\n§7";
            }
            hoverText += value;
        }

        TextComponent comp = new TextComponent();
        comp.setText(StringUtils.replace(this, data.getPlayer(), ConfigValues.MESSAGE_FAIL.stringValue()));
        comp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()));
        comp.setColor(ChatColor.DARK_GRAY);
        Bukkit.spigot().broadcast(comp);

        if(level >= getMaxLevel()) {
            this.level = 0;
        }
    }

    public double thriveBuffer() {
        return this.buffer = Math.min(100, this.buffer + 1);
    }

    public double decayBuffer(double buffer) {
        return this.buffer = Math.max(getMinBuffer(), this.buffer - buffer);
    }

    public double resetBuffer() {
        return this.buffer = getMinBuffer();
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

    public int getMinBuffer() {
        return ConfigValues.Checks.getMinBuffer(this);
    }

    public boolean isExempt(ExemptType... exempts) {
        return data.getExemptProcessor().isExempt(exempts);
    }

}
