package ac.emu.data;

import ac.emu.packet.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @RequiredArgsConstructor
public abstract class Data {

    protected final PlayerData data;

    public abstract void handle(Packet packet);

}
