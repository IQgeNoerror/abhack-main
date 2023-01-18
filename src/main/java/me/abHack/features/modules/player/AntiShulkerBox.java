/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockShulkerBox
 *  net.minecraft.init.Items
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.math.BlockPos
 */
package me.abHack.features.modules.player;

import me.abHack.features.modules.Module;
import me.abHack.features.modules.player.InstantMine;
import me.abHack.features.setting.Setting;
import me.abHack.util.BlockUtil;
import me.abHack.util.InventoryUtil;
import me.abHack.util.MathUtil;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

public class AntiShulkerBox
extends Module {
    private static AntiShulkerBox INSTANCE = new AntiShulkerBox();
    private final Setting<Integer> range = this.register(new Setting<Integer>("Range", 5, 1, 6));
    private final Setting<Integer> saferange = this.register(new Setting<Integer>("SafeRange", 2, 0, 6));

    public AntiShulkerBox() {
        super("AntiShulkerBox", "Automatically dig someone else's box", Module.Category.PLAYER, true, false, false);
        this.setInstance();
    }

    public static AntiShulkerBox getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AntiShulkerBox();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onTick() {
        if (AntiShulkerBox.fullNullCheck()) {
            return;
        }
        int mainSlot = AntiShulkerBox.mc.player.inventory.currentItem;
        if (InstantMine.getInstance().isOff()) {
            InstantMine.getInstance().enable();
        }
        for (BlockPos blockPos : this.breakPos(this.range.getValue().intValue())) {
            int slotPick = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
            if (slotPick == -1) {
                return;
            }
            if (AntiShulkerBox.mc.player.getDistanceSq(blockPos) < MathUtil.square(this.saferange.getValue().intValue()) || blockPos == null) continue;
            if (AntiShulkerBox.mc.world.getBlockState(blockPos).getBlock() instanceof BlockShulkerBox) {
                AntiShulkerBox.mc.player.inventory.currentItem = slotPick;
                AntiShulkerBox.mc.player.swingArm(EnumHand.MAIN_HAND);
                AntiShulkerBox.mc.playerController.onPlayerDamageBlock(blockPos, BlockUtil.getRayTraceFacing(blockPos));
                continue;
            }
            AntiShulkerBox.mc.player.inventory.currentItem = mainSlot;
        }
    }

    private NonNullList<BlockPos> breakPos(float placeRange) {
        NonNullList positions = NonNullList.create();
        positions.addAll(BlockUtil.getSphere(new BlockPos(Math.floor(AntiShulkerBox.mc.player.posX), Math.floor(AntiShulkerBox.mc.player.posY), Math.floor(AntiShulkerBox.mc.player.posZ)), placeRange, 0, false, true, 0));
        return positions;
    }
}

