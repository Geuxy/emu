package ac.emu.data.impl;

import ac.emu.data.Data;
import ac.emu.data.profile.EmuPlayer;
import ac.emu.packet.Packet;

import ac.emu.utils.bukkit.BukkitUtil;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Getter
public class CombatData extends Data {

    private int sinceHitTicks;

    private Player target;

    public CombatData(EmuPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(packet.isMovement()) {
            this.sinceHitTicks++;
        }

        if(packet.getType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity wrapper = new WrapperPlayClientInteractEntity((PacketReceiveEvent) packet.getEvent());

            if(wrapper.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                if(!wrapper.getTarget().isPresent()) {
                    return;
                }

                Player entity = BukkitUtil.getPlayer(wrapper.getEntityId());

                if(entity != null) {
                    this.target = entity;
                    this.sinceHitTicks = 0;
                }
            }
        }
    }

}
