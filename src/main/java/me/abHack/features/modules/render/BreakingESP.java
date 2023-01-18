/*
 * Decompiled with CFR 0.152.
 */
package me.abHack.features.modules.render;

import java.awt.Color;
import me.abHack.event.events.Render3DEvent;
import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import me.abHack.util.RenderUtil;

public class BreakingESP
extends Module {
    public final Setting<Float> lineWidth;
    public final Setting<Integer> boxAlpha;
    public Setting<Integer> red;
    public Setting<Integer> green;
    public Setting<Integer> blue;
    public Setting<Boolean> box = this.register(new Setting<Boolean>("Box", true));
    public Setting<Boolean> outline = this.register(new Setting<Boolean>("Outline", true));

    public BreakingESP() {
        super("BreakingESP", "Rendering broken blocks", Module.Category.RENDER, true, false, false);
        this.red = this.register(new Setting<Integer>("Red", 125, 0, 255));
        this.green = this.register(new Setting<Integer>("Green", 0, 0, 255));
        this.blue = this.register(new Setting<Integer>("Blue", 255, 0, 255));
        this.lineWidth = this.register(new Setting<Object>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f), object -> this.outline.getValue()));
        this.boxAlpha = this.register(new Setting<Object>("BoxAlpha", Integer.valueOf(85), Integer.valueOf(0), Integer.valueOf(255), object -> this.box.getValue()));
    }

    @Override
    public void onRender3D(Render3DEvent render3DEvent) {
        if (BreakingESP.mc.playerController.currentBlock != null) {
            Color color = new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.boxAlpha.getValue());
            RenderUtil.boxESP(BreakingESP.mc.playerController.currentBlock, color, this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), false);
        }
    }
}

