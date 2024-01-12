package ac.emu.data;

import ac.emu.packet.Packet;
import ac.emu.user.EmuPlayer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @RequiredArgsConstructor
public abstract class Data {

    protected final EmuPlayer data;

    public abstract void handle(Packet packet);

}
