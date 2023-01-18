/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemShulkerBox
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 */
package me.abHack.features.modules.player;

import me.abHack.features.modules.Module;
import me.abHack.features.modules.player.InstantMine;
import me.abHack.features.setting.Setting;
import me.abHack.util.BlockUtil;
import me.abHack.util.Timer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class AutoDupe
extends Module {
    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 0, 0, 2000));
    private final Timer timer = new Timer();
    private BlockPos pos;
    int box;

    public AutoDupe() {
        super("AutoDupe", "Automatically places Shulker", Module.Category.PLAYER, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (InstantMine.breakPos == null) {
            return;
        }
        this.pos = InstantMine.breakPos;
        IBlockState blockState = AutoDupe.mc.world.getBlockState(this.pos);
        this.box = this.getItemShulkerBox();
        if (blockState.getBlock() == Blocks.AIR && this.box != -1) {
            AutoDupe.mc.player.inventory.currentItem = this.box;
            BlockUtil.placeBlock(this.pos, EnumHand.MAIN_HAND, false, false, false);
            this.timer.passedDms(this.delay.getValue().intValue());
        }
    }

    public int getItemShulkerBox() {
        int box = -1;
        for (int x = 0; x <= 8; ++x) {
            Item item = AutoDupe.mc.player.inventory.getStackInSlot(x).getItem();
            if (!(item instanceof ItemShulkerBox)) continue;
            box = x;
        }
        return box;
    }
}

