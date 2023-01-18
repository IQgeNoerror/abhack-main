/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityItemFrame
 *  net.minecraft.entity.item.EntityMinecartChest
 *  net.minecraft.item.ItemShulkerBox
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.tileentity.TileEntityChest
 *  net.minecraft.tileentity.TileEntityDispenser
 *  net.minecraft.tileentity.TileEntityEnderChest
 *  net.minecraft.tileentity.TileEntityFurnace
 *  net.minecraft.tileentity.TileEntityHopper
 *  net.minecraft.tileentity.TileEntityShulkerBox
 *  net.minecraft.util.math.BlockPos
 */
package me.abHack.features.modules.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import me.abHack.event.events.Render3DEvent;
import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import me.abHack.util.ColorUtil;
import me.abHack.util.MathUtil;
import me.abHack.util.RenderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.math.BlockPos;

public class ChestESP
extends Module {
    private final Setting<Float> range = this.register(new Setting<Float>("Range", Float.valueOf(60.0f), Float.valueOf(1.0f), Float.valueOf(140.0f)));
    private final Setting<Boolean> chest = this.register(new Setting<Boolean>("Chest", true));
    private final Setting<Boolean> dispenser = this.register(new Setting<Boolean>("Dispenser", false));
    private final Setting<Boolean> shulker = this.register(new Setting<Boolean>("Shulker", true));
    private final Setting<Boolean> echest = this.register(new Setting<Boolean>("Ender Chest", true));
    private final Setting<Boolean> furnace = this.register(new Setting<Boolean>("Furnace", false));
    private final Setting<Boolean> hopper = this.register(new Setting<Boolean>("Hopper", false));
    private final Setting<Boolean> cart = this.register(new Setting<Boolean>("Minecart", false));
    private final Setting<Boolean> frame = this.register(new Setting<Boolean>("Item Frame", false));
    private final Setting<Boolean> box = this.register(new Setting<Boolean>("Box", false));
    private final Setting<Integer> boxAlpha = this.register(new Setting<Object>("BoxAlpha", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(255), v -> this.box.getValue()));
    private final Setting<Boolean> outline = this.register(new Setting<Boolean>("Outline", true));
    private final Setting<Float> lineWidth = this.register(new Setting<Object>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f), v -> this.outline.getValue()));

    public ChestESP() {
        super("ChestESP", "Helps you to see where container blocks are", Module.Category.RENDER, false, false, false);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        int color;
        BlockPos pos;
        HashMap<BlockPos, Integer> positions = new HashMap<BlockPos, Integer>();
        for (TileEntity tileEntity : ChestESP.mc.world.loadedTileEntityList) {
            if (!((tileEntity instanceof TileEntityChest && this.chest.getValue() != false || tileEntity instanceof TileEntityDispenser && this.dispenser.getValue() != false || tileEntity instanceof TileEntityShulkerBox && this.shulker.getValue() != false || tileEntity instanceof TileEntityEnderChest && this.echest.getValue() != false || tileEntity instanceof TileEntityFurnace && this.furnace.getValue() != false || tileEntity instanceof TileEntityHopper && this.hopper.getValue() != false) && !(ChestESP.mc.player.getDistanceSq(pos = tileEntity.getPos()) > MathUtil.square(this.range.getValue().floatValue())) && (color = this.getTileEntityColor(tileEntity)) != -1)) continue;
            positions.put(pos, color);
        }
        for (Entity entity : ChestESP.mc.world.loadedEntityList) {
            if ((!(entity instanceof EntityItemFrame) || !this.frame.getValue().booleanValue()) && (!(entity instanceof EntityMinecartChest) || !this.cart.getValue().booleanValue()) || ChestESP.mc.player.getDistanceSq(pos = entity.getPosition()) > MathUtil.square(this.range.getValue().floatValue()) || (color = this.getEntityColor(entity)) == -1) continue;
            positions.put(pos, color);
        }
        for (Map.Entry entry : positions.entrySet()) {
            BlockPos blockPos = (BlockPos)entry.getKey();
            int color2 = (Integer)entry.getValue();
            RenderUtil.drawBoxESP(blockPos, new Color(color2), false, new Color(color2), this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), false);
        }
    }

    private int getTileEntityColor(TileEntity tileEntity) {
        if (tileEntity instanceof TileEntityChest) {
            return Colors.ORANGE;
        }
        if (tileEntity instanceof TileEntityShulkerBox) {
            return Colors.PINK;
        }
        if (tileEntity instanceof TileEntityEnderChest) {
            return Colors.PURPLE;
        }
        if (tileEntity instanceof TileEntityFurnace) {
            return Colors.GRAY;
        }
        if (tileEntity instanceof TileEntityHopper) {
            return Colors.GRAY;
        }
        if (tileEntity instanceof TileEntityDispenser) {
            return Colors.GRAY;
        }
        return -1;
    }

    private int getEntityColor(Entity entity) {
        if (entity instanceof EntityMinecartChest) {
            return Colors.GRAY;
        }
        if (entity instanceof EntityItemFrame && ((EntityItemFrame)entity).getDisplayedItem().getItem() instanceof ItemShulkerBox) {
            return Colors.YELLOW;
        }
        if (entity instanceof EntityItemFrame && !(((EntityItemFrame)entity).getDisplayedItem().getItem() instanceof ItemShulkerBox)) {
            return Colors.ORANGE;
        }
        return -1;
    }

    public static abstract class Colors {
        public static final int WHITE = ColorUtil.toRGBA(255, 255, 255, 155);
        public static final int BLACK = ColorUtil.toRGBA(0, 0, 0, 155);
        public static final int RED = ColorUtil.toRGBA(255, 0, 0, 155);
        public static final int GREEN = ColorUtil.toRGBA(0, 255, 0, 155);
        public static final int BLUE = ColorUtil.toRGBA(0, 0, 255, 155);
        public static final int ORANGE = ColorUtil.toRGBA(255, 128, 0, 100);
        public static final int PURPLE = ColorUtil.toRGBA(105, 13, 173, 100);
        public static final int GRAY = ColorUtil.toRGBA(169, 169, 169, 155);
        public static final int DARK_RED = ColorUtil.toRGBA(64, 0, 0, 155);
        public static final int YELLOW = ColorUtil.toRGBA(255, 255, 0, 155);
        public static final int PINK = ColorUtil.toRGBA(255, 120, 203, 100);
        public static final int RAINBOW = Integer.MIN_VALUE;
    }
}

