package ac.emu.data.impl;

import ac.emu.user.EmuPlayer;
import ac.emu.utils.MathUtil;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientWindowConfirmation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityVelocity;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerExplosion;

import lombok.Getter;

import ac.emu.data.Data;
import ac.emu.packet.Packet;

import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

@Getter
public class VelocityData extends Data {

    private double x, y, z, speed, lastX, lastY, lastZ, lastSpeed;
    private int ticksSinceVelocityPong, ticksSinceExplosion, ticksSinceVelocity;
    private short velocityID;

    private final Map<Short, Vector> pending = new HashMap<>();

    public VelocityData(EmuPlayer data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(packet.getType() == PacketType.Play.Server.ENTITY_VELOCITY) {
            WrapperPlayServerEntityVelocity wrapper = new WrapperPlayServerEntityVelocity((PacketSendEvent) packet.getEvent());

            if(wrapper.getEntityId() == data.getPlayer().getEntityId()) {
                this.lastX = x;
                this.lastY = y;
                this.lastZ = z;

                this.x = wrapper.getVelocity().x;
                this.y = wrapper.getVelocity().y;
                this.z = wrapper.getVelocity().z;

                this.lastSpeed = speed;
                this.speed = MathUtil.hypot(x, z);

                this.velocityID = data.getUtilities().sendTransaction();

                pending.put(velocityID, new Vector(x, y, z));
                this.ticksSinceVelocity = 0;
            }
        }

        if(packet.getType() == PacketType.Play.Client.WINDOW_CONFIRMATION) {
            WrapperPlayClientWindowConfirmation wrapper = new WrapperPlayClientWindowConfirmation((PacketReceiveEvent) packet.getEvent());

            pending.computeIfPresent(wrapper.getActionId(), (id, vector) -> {
                this.ticksSinceVelocityPong = 0;
                pending.remove(wrapper.getActionId());

                return vector;
            });
        }

        // TODO: get XYZ and stuff
        if(packet.getType() == PacketType.Play.Server.EXPLOSION) {
            WrapperPlayServerExplosion wrapper = new WrapperPlayServerExplosion((PacketSendEvent) packet.getEvent());

            this.ticksSinceVelocityPong = 0;
            this.ticksSinceExplosion = 0;
        }

        if(packet.isMovement()) {
            this.ticksSinceVelocityPong++;
            this.ticksSinceExplosion++;
            this.ticksSinceVelocity++;
        }

    }

}
