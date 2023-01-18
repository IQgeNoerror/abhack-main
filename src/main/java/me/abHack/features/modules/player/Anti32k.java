/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockHopper
 *  net.minecraft.block.BlockShulkerBox
 *  net.minecraft.init.Items
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 */
package me.abHack.features.modules.player;

import me.abHack.features.modules.Module;
import me.abHack.features.modules.player.InstantMine;
import me.abHack.features.setting.Setting;
import me.abHack.util.BlockUtil;
import me.abHack.util.InventoryUtil;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class Anti32k
extends Module {
    private static Anti32k INSTANCE = new Anti32k();
    private final Setting<Integer> range = this.register(new Setting<Integer>("Range", 5, 3, 5));

    public Anti32k() {
        super("Anti32k", "Anti32k", Module.Category.PLAYER, true, false, false);
        this.setInstance();
    }

    public static Anti32k getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Anti32k();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onTick() {
        if (Anti32k.fullNullCheck()) {
            return;
        }
        int mainSlot = Anti32k.mc.player.inventory.currentItem;
        if (InstantMine.getInstance().isOff()) {
            InstantMine.getInstance().enable();
        }
        for (BlockPos blockPos : this.breakPos(this.range.getValue().intValue())) {
            int slotPick = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
            if (slotPick == -1) {
                return;
            }
            if (blockPos == null) continue;
            if (Anti32k.mc.world.getBlockState(blockPos).getBlock() instanceof BlockHopper && Anti32k.mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock() instanceof BlockShulkerBox) {
                Anti32k.mc.player.inventory.currentItem = slotPick;
                Anti32k.mc.player.swingArm(EnumHand.MAIN_HAND);
                Anti32k.mc.playerController.processRightClickBlock(Anti32k.mc.player, Anti32k.mc.world, blockPos.add(0, 1, 0), BlockUtil.getRayTraceFacing(blockPos.add(0, 1, 0)), new Vec3d((Vec3i)blockPos.add(0, 1, 0)), EnumHand.MAIN_HAND);
                Anti32k.mc.playerController.onPlayerDamageBlock(blockPos, BlockUtil.getRayTraceFacing(blockPos));
                continue;
            }
            Anti32k.mc.player.inventory.currentItem = mainSlot;
        }
    }

    private NonNullList<BlockPos> breakPos(float placeRange) {
        NonNullList positions = NonNullList.create();
        positions.addAll(BlockUtil.getSphere(new BlockPos(Math.floor(Anti32k.mc.player.posX), Math.floor(Anti32k.mc.player.posY), Math.floor(Anti32k.mc.player.posZ)), placeRange, 0, false, true, 0));
        return positions;
    }
}

