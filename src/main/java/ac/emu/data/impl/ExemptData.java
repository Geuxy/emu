package ac.emu.data.impl;

import ac.emu.data.PlayerData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import ac.emu.exempt.ExemptType;

import java.util.Arrays;

@RequiredArgsConstructor @Getter
public class ExemptData {

    private final PlayerData data;

    public boolean isExempt(ExemptType exempt) {
        return exempt.getFunction().apply(data);
    }

    public boolean isExempt(ExemptType... exempts) {
        return Arrays.stream(exempts).anyMatch(this::isExempt);
    }

}
