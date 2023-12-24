package ac.emu.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

@Getter @RequiredArgsConstructor
public abstract class Command {

    private final String name, description, permission;
    private final boolean playerOnly;

    public abstract void perform(CommandSender sender, String[] args);

    public Permission getPermission() {
        return new Permission(permission);
    }

}
