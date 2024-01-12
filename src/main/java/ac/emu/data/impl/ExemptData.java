package ac.emu.data.impl;

import ac.emu.user.EmuPlayer;
import ac.emu.exempt.ExemptType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor @Getter
public class ExemptData {

    private final EmuPlayer data;

    public boolean isExempt(ExemptType exempt) {
        return exempt.getFunction().apply(data);
    }

    public boolean isExempt(ExemptType... exempts) {
        return Arrays.stream(exempts).anyMatch(this::isExempt);
    }

}
