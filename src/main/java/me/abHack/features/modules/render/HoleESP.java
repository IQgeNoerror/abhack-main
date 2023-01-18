/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3i
 */
package me.abHack.features.modules.render;

import java.awt.Color;
import me.abHack.event.events.Render3DEvent;
import me.abHack.features.modules.Module;
import me.abHack.features.modules.client.ClickGui;
import me.abHack.features.setting.Setting;
import me.abHack.util.BlockUtil;
import me.abHack.util.ColorUtil;
import me.abHack.util.RenderUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class HoleESP
extends Module {
    private static HoleESP INSTANCE = new HoleESP();
    private final Setting<Integer> range = this.register(new Setting<Integer>("RangeX", 8, 0, 20));
    private final Setting<Integer> rangeY = this.register(new Setting<Integer>("RangeY", 8, 0, 20));
    private final Setting<Integer> red = this.register(new Setting<Integer>("Red", 255, 0, 255));
    private final Setting<Integer> green = this.register(new Setting<Integer>("Green", 0, 0, 255));
    private final Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 0, 0, 255));
    private final Setting<Integer> alpha = this.register(new Setting<Integer>("Alpha", 255, 0, 255));
    public Setting<Boolean> renderOwn = this.register(new Setting<Boolean>("RenderOwn", true));
    public Setting<Boolean> fov = this.register(new Setting<Boolean>("InFov", true));
    public Setting<Boolean> box = this.register(new Setting<Boolean>("Box", true));
    private final Setting<Integer> boxAlpha = this.register(new Setting<Object>("BoxAlpha", Integer.valueOf(40), Integer.valueOf(0), Integer.valueOf(255), v -> this.box.getValue()));
    public Setting<Boolean> outline = this.register(new Setting<Boolean>("Outline", true));
    private final Setting<Float> lineWidth = this.register(new Setting<Object>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f), v -> this.outline.getValue()));
    public Setting<Double> height = this.register(new Setting<Double>("Height", 0.3, -2.0, 2.0));
    public Setting<Boolean> rainbow = this.register(new Setting<Boolean>("Rainbow", false));
    public Setting<Boolean> safeColor = this.register(new Setting<Boolean>("BedrockColor", false));
    private final Setting<Integer> safeRed = this.register(new Setting<Object>("BedrockRed", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.safeColor.getValue()));
    private final Setting<Integer> safeGreen = this.register(new Setting<Object>("BedrockGreen", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.safeColor.getValue()));
    private final Setting<Integer> safeBlue = this.register(new Setting<Object>("BedrockBlue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.safeColor.getValue()));
    private final Setting<Integer> safeAlpha = this.register(new Setting<Object>("BedrockAlpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.safeColor.getValue()));

    public HoleESP() {
        super("HoleESP", "hole esp", Module.Category.RENDER, false, false, false);
        this.setInstance();
    }

    public static HoleESP getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HoleESP();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        assert (HoleESP.mc.renderViewEntity != null);
        Vec3i playerPos = new Vec3i(HoleESP.mc.renderViewEntity.posX, HoleESP.mc.renderViewEntity.posY, HoleESP.mc.renderViewEntity.posZ);
        for (int x = playerPos.getX() - this.range.getValue(); x < playerPos.getX() + this.range.getValue(); ++x) {
            for (int z = playerPos.getZ() - this.range.getValue(); z < playerPos.getZ() + this.range.getValue(); ++z) {
                for (int y = playerPos.getY() + this.rangeY.getValue(); y > playerPos.getY() - this.rangeY.getValue(); --y) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (!HoleESP.mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR) || !HoleESP.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) || !HoleESP.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR) || pos.equals((Object)new BlockPos(HoleESP.mc.player.posX, HoleESP.mc.player.posY, HoleESP.mc.player.posZ)) && !this.renderOwn.getValue().booleanValue() || !BlockUtil.isPosInFov(pos).booleanValue() && this.fov.getValue().booleanValue()) continue;
                    int currentAlpha = 0;
                    if (HoleESP.mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK) {
                        RenderUtil.drawBoxESP(pos, this.rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.safeRed.getValue(), this.safeGreen.getValue(), this.safeBlue.getValue(), this.safeAlpha.getValue()), false, new Color(1, 1, 1, 1), this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true, this.height.getValue(), true, true, false, true, currentAlpha);
                        continue;
                    }
                    if (!BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.down()).getBlock()) || !BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.east()).getBlock()) || !BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.west()).getBlock()) || !BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.south()).getBlock()) || !BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.north()).getBlock())) continue;
                    RenderUtil.drawBoxESP(pos, this.rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), false, new Color(1, 1, 1, 1), this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true, this.height.getValue(), true, true, false, true, currentAlpha);
                }
            }
        }
    }
}

