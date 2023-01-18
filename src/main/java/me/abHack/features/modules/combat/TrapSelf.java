/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 */
package me.abHack.features.modules.combat;

import me.abHack.OyVey;
import me.abHack.features.modules.Module;
import me.abHack.features.modules.combat.Surround;
import me.abHack.features.setting.Setting;
import me.abHack.util.BlockUtil;
import me.abHack.util.EntityUtil;
import me.abHack.util.InventoryUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class TrapSelf
extends Module {
    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", false));
    private final Setting<Boolean> hole = this.register(new Setting<Boolean>("Hole or Burrrow", true));
    private final Setting<Boolean> center = this.register(new Setting<Boolean>("TPCenter", true));
    private final Setting<Boolean> toggle = this.register(new Setting<Boolean>("Toggle", false));
    private int obsidian = -1;

    public TrapSelf() {
        super("TrapSelf", "One Self Trap", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        Surround.breakcrystal();
        BlockPos startPos = EntityUtil.getRoundedBlockPos((Entity)Surround.mc.player);
        if (this.center.getValue().booleanValue()) {
            OyVey.positionManager.setPositionPacket((double)startPos.getX() + 0.5, startPos.getY(), (double)startPos.getZ() + 0.5, true, true, true);
        }
    }

    @Override
    public void onTick() {
        if (TrapSelf.fullNullCheck()) {
            return;
        }
        this.obsidian = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
        if (this.obsidian == -1) {
            return;
        }
        BlockPos pos = new BlockPos(TrapSelf.mc.player.posX, TrapSelf.mc.player.posY, TrapSelf.mc.player.posZ);
        if (!(EntityUtil.isInHole((Entity)TrapSelf.mc.player) && this.isBurrowed((EntityPlayer)TrapSelf.mc.player) || !this.hole.getValue().booleanValue())) {
            return;
        }
        if (this.getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.AIR) {
            this.place(pos.add(1, 0, 0));
        }
        if (this.getBlock(pos.add(1, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 0, 0)).getBlock() != Blocks.AIR) {
            this.place(pos.add(1, 1, 0));
        }
        if (this.getBlock(pos.add(1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 1, 0)).getBlock() != Blocks.AIR) {
            this.place(pos.add(1, 2, 0));
        }
        if (this.getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR) {
            this.place(pos.add(-1, 0, 0));
        }
        if (this.getBlock(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 0, 0)).getBlock() != Blocks.AIR) {
            this.place(pos.add(-1, 1, 0));
        }
        if (this.getBlock(pos.add(-1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 1, 0)).getBlock() != Blocks.AIR) {
            this.place(pos.add(-1, 2, 0));
        }
        if (this.getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.AIR) {
            this.place(pos.add(0, 0, 1));
        }
        if (this.getBlock(pos.add(0, 1, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 0, 1)).getBlock() != Blocks.AIR) {
            this.place(pos.add(0, 1, 1));
        }
        if (this.getBlock(pos.add(0, 2, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 1, 1)).getBlock() != Blocks.AIR) {
            this.place(pos.add(0, 2, 1));
        }
        if (this.getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.AIR) {
            this.place(pos.add(0, 0, -1));
        }
        if (this.getBlock(pos.add(0, 1, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 0, -1)).getBlock() != Blocks.AIR) {
            this.place(pos.add(0, 1, -1));
        }
        if (this.getBlock(pos.add(0, 2, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 1, -1)).getBlock() != Blocks.AIR) {
            this.place(pos.add(0, 2, -1));
        }
        if (this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR) {
            this.place(pos.add(0, 2, 0));
        }
        if (this.toggle.getValue().booleanValue() || OyVey.speedManager.getPlayerSpeed((EntityPlayer)TrapSelf.mc.player) > 10.0) {
            this.toggle();
        }
    }

    private void switchToSlot(int slot) {
        TrapSelf.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(slot));
        TrapSelf.mc.player.inventory.currentItem = slot;
        TrapSelf.mc.playerController.updateController();
    }

    private void place(BlockPos pos) {
        int old = TrapSelf.mc.player.inventory.currentItem;
        this.switchToSlot(this.obsidian);
        BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
        this.switchToSlot(old);
    }

    private IBlockState getBlock(BlockPos block) {
        return TrapSelf.mc.world.getBlockState(block);
    }

    private boolean isBurrowed(EntityPlayer entityPlayer) {
        BlockPos blockPos = new BlockPos(Math.floor(entityPlayer.posX), Math.floor(entityPlayer.posY + 0.2), Math.floor(entityPlayer.posZ));
        return TrapSelf.mc.world.getBlockState(blockPos).getBlock() == Blocks.ENDER_CHEST || TrapSelf.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN || TrapSelf.mc.world.getBlockState(blockPos).getBlock() == Blocks.CHEST;
    }
}

