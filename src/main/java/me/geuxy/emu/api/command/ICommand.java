package me.geuxy.emu.api.command;

public interface ICommand {

    String getName();
    String getDescription();
    String getUsage();

    boolean isPlayerOnly();

}
