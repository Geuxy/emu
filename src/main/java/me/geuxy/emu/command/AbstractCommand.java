package me.geuxy.emu.command;

import me.geuxy.emu.api.command.ICommand;

import org.bukkit.command.CommandSender;

public abstract class AbstractCommand implements ICommand {

    public abstract void perform(CommandSender sender, String[] args);

}
