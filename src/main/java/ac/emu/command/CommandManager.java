package ac.emu.command;

import ac.emu.command.impl.ReloadCommand;

import lombok.Getter;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class CommandManager implements CommandExecutor {

    private final List<Command> commands = new ArrayList<>();

    public CommandManager() {
        commands.addAll(Arrays.asList(
            new ReloadCommand()
        ));
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command useless, String label, String[] args) {
        Command command = commands.stream().filter(c -> c.getName().equalsIgnoreCase(args[0])).findFirst().orElse(null);

        if(command == null) {
            sender.sendMessage("§cInvalid Command!");
            return false;
        }

        if(command.isPlayerOnly() && sender instanceof ConsoleCommandSender) {
            sender.sendMessage("§cOnly players can use this command!");
            return false;
        }

        if(!sender.hasPermission(command.getPermission())) {
            sender.sendMessage("§cNo permission!");
            return false;
        }

        command.perform(sender, args);
        return true;
    }

}
