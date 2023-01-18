/*
 * Decompiled with CFR 0.152.
 */
package me.abHack.features.modules.misc;

import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import me.abHack.util.EntityUtil;

public class Timer
extends Module {
    public Setting<Float> timer = this.register(new Setting<Float>("Timer", Float.valueOf(1.1f), Float.valueOf(1.0f), Float.valueOf(2.0f)));

    public Timer() {
        super("Timer", "Timer", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onTick() {
        if (Timer.fullNullCheck()) {
            return;
        }
        EntityUtil.setTimer(this.timer.getValue().floatValue());
    }

    @Override
    public void onDisable() {
        EntityUtil.resetTimer();
    }

    @Override
    public String getDisplayInfo() {
        return this.timer.getValue() + "";
    }
}

