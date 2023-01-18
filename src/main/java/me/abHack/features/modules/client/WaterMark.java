/*
 * Decompiled with CFR 0.152.
 */
package me.abHack.features.modules.client;

import me.abHack.event.events.Render2DEvent;
import me.abHack.features.modules.Module;
import me.abHack.features.modules.client.ClickGui;
import me.abHack.features.setting.Setting;
import me.abHack.util.ColorUtil;
import me.abHack.util.HudUtil;
import me.abHack.util.RenderUtil;

public class WaterMark
extends Module {
    private final Setting<Boolean> rainbow = this.register(new Setting<Boolean>("Rainbow", true));
    public Setting<Integer> compactX = this.register(new Setting<Integer>("WaterMarkX", 0, 0, 1080));
    public Setting<Integer> compactY = this.register(new Setting<Integer>("WaterMarkY", 0, 0, 530));
    public Setting<Integer> red = this.register(new Setting<Integer>("Red", 20, 0, 255));
    public Setting<Integer> green = this.register(new Setting<Integer>("Green", 20, 0, 255));
    public Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 20, 0, 255));
    private int color1;
    private int color2;

    public WaterMark() {
        super("WaterMark", "watermark", Module.Category.CLIENT, true, false, false);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        if (this.rainbow.getValue().booleanValue()) {
            this.color1 = ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB();
            this.color2 = ColorUtil.rainbow(100).getRGB();
            RenderUtil.drawRectangleCorrectly(this.compactX.getValue(), this.compactY.getValue(), 200, 15, ColorUtil.toRGBA(20, 20, 20, 200));
            RenderUtil.drawGradientSideways(this.compactX.getValue().intValue(), 0.0 + (double)this.compactY.getValue().intValue(), 200 + this.compactX.getValue(), 1.5 + (double)this.compactY.getValue().intValue(), this.color1, this.color2);
            this.renderer.drawString("abHack v0.0.1 | " + HudUtil.getPingSatus() + "ms | " + HudUtil.getFpsStatus() + "fps | " + HudUtil.getTpsStatus() + "tps", this.compactX.getValue().intValue(), this.compactY.getValue() + 3, this.color1, true);
        } else {
            this.color1 = ColorUtil.toRGBA(this.red.getValue(), this.green.getValue(), this.blue.getValue(), 255);
            RenderUtil.drawRectangleCorrectly(this.compactX.getValue(), this.compactY.getValue(), 200, 15, ColorUtil.toRGBA(20, 20, 20, 200));
            RenderUtil.drawGradientSideways(this.compactX.getValue().intValue(), 0.0 + (double)this.compactY.getValue().intValue(), 200 + this.compactX.getValue(), 1.5 + (double)this.compactY.getValue().intValue(), this.color1, this.color1);
            this.renderer.drawString("abHack v0.0.1 | " + HudUtil.getPingSatus() + "ms | " + HudUtil.getFpsStatus() + "fps | " + HudUtil.getTpsStatus() + "tps", this.compactX.getValue().intValue(), this.compactY.getValue() + 3, this.color1, true);
        }
    }
}

