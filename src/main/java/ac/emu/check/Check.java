package ac.emu.check;

import lombok.Getter;

import ac.emu.utils.StaffUtil;
import ac.emu.config.ConfigValues;
import ac.emu.user.EmuPlayer;
import ac.emu.exempt.ExemptType;
import ac.emu.packet.Packet;

@Getter
public abstract class Check {

    protected final CheckInfo info;
    protected final EmuPlayer data;

    private String display;

    private int level;
    private int maximumLevel;

    private double currentBuffer;
    private double flagBuffer;
    private double decay;

    private boolean enabled;
    private boolean punishable;

    public Check(EmuPlayer data) {
        this.data = data;
        this.info = this.getClass().getAnnotation(CheckInfo.class);
        this.flagBuffer = ConfigValues.getFlagBuffer(this);
        this.maximumLevel = ConfigValues.getMaxLevel(this);
        this.enabled = ConfigValues.isEnabled(this);
        this.punishable = ConfigValues.isPunishable(this);
        this.decay = ConfigValues.getBufferDecay(this);
        this.display = ConfigValues.getDisplay(this);
        this.currentBuffer = 0;
    }

    public abstract void processPacket(Packet packet);

    protected void reward() {
        this.decayBuffer(decay);
    }

    public void fail() {
        this.fail("");
    }

    public boolean fail(String values) {
        if(thriveBuffer() <= flagBuffer) {
            return false;
        }

        this.level++;

        StaffUtil.sendAlert(ConfigValues.MESSAGE_FAIL.stringValue().replace("{display}", display).replace("{values}", values), this, data.getPlayer());

        if(level >= maximumLevel) {
            if(punishable) {
                // TODO: Punish player with custom command

            }
            this.level = 0;
        }

        return true;
    }

    public double thriveBuffer() {
        return this.currentBuffer = Math.min(100, this.currentBuffer + 1);
    }

    public double decayBuffer(double buffer) {
        return this.currentBuffer = Math.max(0, this.currentBuffer - buffer);
    }

    public double resetBuffer() {
        return this.currentBuffer = 0;
    }

    public boolean isExempt(ExemptType... types) {
        return data.getExemptData().isExempt(types);
    }

}
