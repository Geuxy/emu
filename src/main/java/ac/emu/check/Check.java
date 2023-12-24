package ac.emu.check;

import lombok.Getter;

import ac.emu.utils.StaffUtil;
import ac.emu.config.ConfigValues;
import ac.emu.data.PlayerData;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

@Getter
public abstract class Check {

    protected final CheckInfo info;
    protected final PlayerData data;

    private int level;
    private int maximumLevel;

    private double buffer;
    private double minimumBuffer;

    private boolean enabled;
    private boolean punishable;

    public Check(PlayerData data) {
        this.data = data;
        this.info = this.getClass().getAnnotation(CheckInfo.class);
        this.minimumBuffer = ConfigValues.getMinBuffer(this);
        this.maximumLevel = ConfigValues.getMaxLevel(this);
        this.enabled = ConfigValues.isEnabled(this);
        this.punishable = ConfigValues.isPunishable(this);
        this.buffer = minimumBuffer;
    }

    public abstract void processPacket(Packet packet);

    public void fail() {
        this.fail("");
    }

    public void fail(String values) {
        this.level++;

        StaffUtil.sendAlert(ConfigValues.MESSAGE_FAIL.stringValue().replace("{values}", values), this, data.getPlayer());

        if(level >= maximumLevel) {
            if(isPunishable()) {
                // TODO: Punish player with custom command

            }
            this.level = 0;
        }
    }

    public double thriveBuffer() {
        return this.buffer = Math.min(100, this.buffer + 1);
    }

    public double decayBuffer(double buffer) {
        return this.buffer = Math.max(minimumBuffer, this.buffer - buffer);
    }

    public double resetBuffer() {
        return this.buffer = minimumBuffer;
    }

    public boolean isExempt(ExemptType... exempts) {
        return data.getExemptData().isExempt(exempts);
    }

}
