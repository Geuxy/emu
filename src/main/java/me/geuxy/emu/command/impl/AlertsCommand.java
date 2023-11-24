package me.geuxy.emu.command.impl;

import me.geuxy.emu.Emu;
import me.geuxy.emu.command.AbstractCommand;
import me.geuxy.emu.data.PlayerData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AlertsCommand extends AbstractCommand {

    @Override
    public void perform(CommandSender sender, String[] args) {
        PlayerData data = Emu.INSTANCE.getDataManager().get((Player) sender);

        data.setAlertsEnabled(!data.isAlertsEnabled());
    }

    @Override
    public String getName() {
        return "alerts";
    }

    @Override
    public String getDescription() {
        return "toggles alerts sent by the anticheat";
    }

    @Override
    public String getUsage() {
        return "/alerts";
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

}
