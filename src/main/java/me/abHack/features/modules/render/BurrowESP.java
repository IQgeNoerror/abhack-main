/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 */
package me.abHack.features.modules.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import me.abHack.OyVey;
import me.abHack.event.events.Render3DEvent;
import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import me.abHack.util.EntityUtil;
import me.abHack.util.RenderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class BurrowESP
extends Module {
    private final Setting<Integer> boxRed;
    private final Setting<Integer> outlineGreen;
    private final Setting<Integer> boxGreen;
    private final Setting<Boolean> box;
    private final Setting<Boolean> cOutline;
    private final Setting<Integer> outlineBlue;
    private final Setting<Boolean> name = this.register(new Setting<Boolean>("Name", false));
    private final Setting<Integer> boxAlpha;
    private final Setting<Float> outlineWidth;
    private final Setting<Integer> outlineRed;
    private final Setting<Boolean> outline;
    private final Setting<Integer> boxBlue;
    private final Map<EntityPlayer, BlockPos> burrowedPlayers;
    private final Setting<Integer> outlineAlpha;

    public BurrowESP() {
        super("BurrowESP", "Show burrow players .", Module.Category.RENDER, true, false, false);
        this.box = new Setting<Boolean>("Box", true);
        this.boxRed = this.register(new Setting<Integer>("BoxRed", 255, 0, 255));
        this.boxGreen = this.register(new Setting<Integer>("BoxGreen", 255, 0, 255));
        this.boxBlue = this.register(new Setting<Integer>("BoxBlue", 255, 0, 255));
        this.boxAlpha = this.register(new Setting<Integer>("BoxAlpha", 125, 0, 255));
        this.outline = this.register(new Setting<Boolean>("Outline", true));
        this.outlineWidth = this.register(new Setting<Float>("OutlineWidth", Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(5.0f), v -> this.outline.getValue()));
        this.cOutline = this.register(new Setting<Boolean>("CustomOutline", Boolean.valueOf(false), v -> this.outline.getValue()));
        this.outlineRed = this.register(new Setting<Integer>("OutlineRed", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.cOutline.getValue()));
        this.outlineGreen = this.register(new Setting<Integer>("OutlineGreen", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.cOutline.getValue()));
        this.outlineBlue = this.register(new Setting<Integer>("OutlineBlue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.cOutline.getValue()));
        this.outlineAlpha = this.register(new Setting<Integer>("OutlineAlpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.cOutline.getValue()));
        this.burrowedPlayers = new HashMap<EntityPlayer, BlockPos>();
    }

    private void getPlayers() {
        for (EntityPlayer entityPlayer : BurrowESP.mc.world.playerEntities) {
            if (entityPlayer == BurrowESP.mc.player || OyVey.friendManager.isFriend(entityPlayer.getName()) || !EntityUtil.isLiving((Entity)entityPlayer) || !this.isBurrowed(entityPlayer)) continue;
            this.burrowedPlayers.put(entityPlayer, new BlockPos(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ));
        }
    }

    @Override
    public void onEnable() {
        this.burrowedPlayers.clear();
    }

    private void lambda$onRender3D$8(Map.Entry entry) {
        this.renderBurrowedBlock((BlockPos)entry.getValue());
        if (this.name.getValue().booleanValue()) {
            RenderUtil.drawText((BlockPos)entry.getValue(), ((EntityPlayer)entry.getKey()).getGameProfile().getName());
        }
    }

    private boolean isBurrowed(EntityPlayer entityPlayer) {
        BlockPos blockPos = new BlockPos(Math.floor(entityPlayer.posX), Math.floor(entityPlayer.posY + 0.2), Math.floor(entityPlayer.posZ));
        return BurrowESP.mc.world.getBlockState(blockPos).getBlock() == Blocks.ENDER_CHEST || BurrowESP.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN || BurrowESP.mc.world.getBlockState(blockPos).getBlock() == Blocks.CHEST;
    }

    @Override
    public void onUpdate() {
        if (BurrowESP.fullNullCheck()) {
            return;
        }
        this.burrowedPlayers.clear();
        this.getPlayers();
    }

    private void renderBurrowedBlock(BlockPos blockPos) {
        RenderUtil.drawBoxESP(blockPos, new Color(this.boxRed.getValue(), this.boxGreen.getValue(), this.boxBlue.getValue(), this.boxAlpha.getValue()), true, new Color(this.outlineRed.getValue(), this.outlineGreen.getValue(), this.outlineBlue.getValue(), this.outlineAlpha.getValue()), this.outlineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true);
    }

    @Override
    public void onRender3D(Render3DEvent render3DEvent) {
        if (!this.burrowedPlayers.isEmpty()) {
            this.burrowedPlayers.entrySet().forEach(this::lambda$onRender3D$8);
        }
    }
}

