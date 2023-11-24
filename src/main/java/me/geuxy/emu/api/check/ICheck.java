package me.geuxy.emu.api.check;

public interface ICheck {

    String getName();
    String getDescription();
    String getType();

    int getMaxLevel();

    boolean isEnabled();

}
