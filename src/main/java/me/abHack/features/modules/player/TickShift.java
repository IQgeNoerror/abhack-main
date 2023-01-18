/*
 * Decompiled with CFR 0.152.
 */
package me.abHack.features.modules.player;

import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;

public class TickShift
extends Module {
    public final Setting<Float> multiplier = this.register(new Setting<Float>("Multiplier", Float.valueOf(1.8f), Float.valueOf(0.1f), Float.valueOf(20.0f)));
    public final Setting<Integer> disableTicks = this.register(new Setting<Integer>("Disable Ticks", 30, 1, 100));
    public final Setting<Integer> noPauseTicks = this.register(new Setting<Integer>("UnPause Ticks", 30, 1, 100));
    public final Setting<Mode> disableMode = this.register(new Setting<Mode>("Disable Mode", Mode.Pause));
    public int ticksPassed = 0;
    public int unPauseTicks = 0;
    public boolean pause = false;

    public TickShift() {
        super("TickShift", "Changes tick speed at certain intervals to bypass anticheat and go faster", Module.Category.PLAYER, true, false, false);
    }

    @Override
    public void onUpdate() {
        ++this.ticksPassed;
        if (!this.pause) {
            TickShift.mc.timer.tickLength = 50.0f / this.getMultiplier();
        }
        if (this.pause) {
            ++this.unPauseTicks;
            TickShift.mc.timer.tickLength = 50.0f;
        }
        if (this.disableMode.getValue() != Mode.Pause) {
            this.pause = false;
        }
        if (this.ticksPassed >= this.disableTicks.getValue()) {
            this.ticksPassed = 0;
            if (this.disableMode.getValue() == Mode.Disable) {
                this.disable();
            }
            if (this.disableMode.getValue() == Mode.Pause) {
                if (this.unPauseTicks <= this.noPauseTicks.getValue()) {
                    this.pause = true;
                } else if (this.unPauseTicks >= this.noPauseTicks.getValue()) {
                    this.pause = false;
                    this.unPauseTicks = 0;
                }
            }
        }
    }

    @Override
    public void onDisable() {
        TickShift.mc.timer.tickLength = 50.0f;
    }

    public float getMultiplier() {
        if (this.isEnabled()) {
            return this.multiplier.getValue().floatValue();
        }
        return 1.0f;
    }

    public static enum Mode {
        Pause,
        Disable;

    }
}

