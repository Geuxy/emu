package me.geuxy.emu.api.data;

import me.geuxy.emu.check.AbstractCheck;

import java.util.List;

public interface IData {

    boolean isAlertsEnabled();

    List<AbstractCheck> getChecks();

}
