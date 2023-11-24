package me.geuxy.emu.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import me.geuxy.emu.command.impl.AlertsCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

@Getter @RequiredArgsConstructor
public enum Commands implements CommandExecutor {

    ALERTS(new AlertsCommand()),

    ;

    private final AbstractCommand abstractCommand;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(abstractCommand.isPlayerOnly() && sender instanceof ConsoleCommandSender) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }

        abstractCommand.perform(sender, args);
        return true;
    }

}
