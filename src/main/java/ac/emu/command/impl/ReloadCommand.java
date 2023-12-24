package ac.emu.command.impl;

import ac.emu.Emu;
import ac.emu.command.Command;

import org.bukkit.command.CommandSender;

public class ReloadCommand extends Command {

    public ReloadCommand() {
        super("reload", "Reloads the anti-cheat's configuration", "emu.command.reload", false);
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        Emu.INSTANCE.getPlugin().reloadConfig();
        sender.sendMessage("§aReloaded configuration!");
    }

}
