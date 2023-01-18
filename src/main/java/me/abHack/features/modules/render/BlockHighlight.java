/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 */
package me.abHack.features.modules.render;

import java.awt.Color;
import me.abHack.event.events.Render3DEvent;
import me.abHack.features.modules.Module;
import me.abHack.features.modules.client.ClickGui;
import me.abHack.features.setting.Setting;
import me.abHack.util.ColorUtil;
import me.abHack.util.RenderUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public class BlockHighlight
extends Module {
    public final Setting<Boolean> rainbow = this.register(new Setting<Boolean>("Rainbow", true));
    private final Setting<Integer> red = this.register(new Setting<Integer>("Red", 255, 0, 255));
    private final Setting<Integer> green = this.register(new Setting<Integer>("Green", 255, 0, 255));
    private final Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 255, 0, 255));
    private final Setting<Float> lineWidth = this.register(new Setting<Float>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f)));
    private final Setting<Integer> cAlpha = this.register(new Setting<Integer>("Alpha", 255, 0, 255));

    public BlockHighlight() {
        super("BlockHighlight", "Render the block you are looking at.", Module.Category.RENDER, false, false, false);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        RayTraceResult ray = BlockHighlight.mc.objectMouseOver;
        if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos blockpos = ray.getBlockPos();
            RenderUtil.drawBlockOutline(blockpos, this.rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.cAlpha.getValue()), this.lineWidth.getValue().floatValue(), false);
        }
    }
}

