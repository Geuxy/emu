package me.geuxy.emu.data.processors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import me.geuxy.emu.data.PlayerData;
import me.geuxy.emu.exempt.ExemptType;

import java.util.Arrays;

@RequiredArgsConstructor @Getter
public class ExemptProcessor {

    private final PlayerData data;

    public boolean isExempt(ExemptType exempt) {
        return exempt.getFunction().apply(data);
    }

    public boolean isExempt(ExemptType... exempts) {
        return Arrays.stream(exempts).anyMatch(this::isExempt);
    }

}
