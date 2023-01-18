/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 */
package me.abHack.features.modules.combat;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import me.abHack.OyVey;
import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import me.abHack.util.BlockUtil;
import me.abHack.util.EntityUtil;
import me.abHack.util.InventoryUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class PistonCrystal
extends Module {
    public EntityPlayer target;
    private final Setting<Float> range = this.register(new Setting<Float>("Range", Float.valueOf(5.0f), Float.valueOf(1.0f), Float.valueOf(6.0f)));
    private final List<Block> godBlocks = Arrays.asList(Blocks.OBSIDIAN, Blocks.BEDROCK);

    public PistonCrystal() {
        super("PistonCrystal", "Trap Head", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onTick() {
        Entity t_crystal;
        if (PistonCrystal.fullNullCheck()) {
            return;
        }
        this.target = this.getTarget(this.range.getValue().floatValue());
        if (this.target == null) {
            return;
        }
        int reblock = this.findMaterials(Blocks.REDSTONE_BLOCK);
        int piston = this.findMaterials((Block)Blocks.PISTON);
        int crystal = InventoryUtil.getItemHotbar(Items.END_CRYSTAL);
        if (reblock == -1 || piston == -1 || crystal == -1) {
            return;
        }
        BlockPos people = new BlockPos(this.target.posX, this.target.posY, this.target.posZ);
        int oldslot = PistonCrystal.mc.player.inventory.currentItem;
        if (!this.godBlocks.contains(this.getBlock(people.add(2, 1, 0)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(2, 0, 0)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(1, 1, 0)).getBlock()) && this.godBlocks.contains(this.getBlock(people.add(1, 0, 0)).getBlock())) {
            if (this.getBlock(people.add(1, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(people.add(2, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(people.add(1, 0, 0)).getBlock() != Blocks.AIR) {
                this.switchToSlot(reblock);
                BlockUtil.placeBlock2(people.add(2, 0, 0), EnumHand.MAIN_HAND, true, true, false);
            }
            if (this.getBlock(people.add(1, 1, 0)).getBlock() == Blocks.AIR && this.godBlocks.contains(this.getBlock(people.add(1, 0, 0)).getBlock()) && this.getBlock(people.add(2, 0, 0)).getBlock() == Blocks.REDSTONE_BLOCK) {
                this.switchToSlot(piston);
                BlockUtil.placeBlock2(people.add(2, 1, 0), EnumHand.MAIN_HAND, true, true, false);
                this.switchToSlot(crystal);
                PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(people.add(1, 0, 0), EnumFacing.UP, PistonCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            }
            this.switchToSlot(oldslot);
            if (this.getBlock(people.add(1, 1, 0)).getBlock() == Blocks.PISTON_HEAD) {
                PistonCrystal.mc.playerController.onPlayerDamageBlock(people.add(1, 1, 0), BlockUtil.getRayTraceFacing(people.add(1, 1, 0)));
            }
        } else if (!this.godBlocks.contains(this.getBlock(people.add(-2, 1, 0)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(-2, 0, 0)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(-1, 1, 0)).getBlock()) && this.godBlocks.contains(this.getBlock(people.add(-1, 0, 0)).getBlock())) {
            if (this.getBlock(people.add(-1, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(people.add(-2, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(people.add(-1, 0, 0)).getBlock() != Blocks.AIR) {
                this.switchToSlot(reblock);
                BlockUtil.placeBlock2(people.add(-2, 0, 0), EnumHand.MAIN_HAND, true, true, false);
            }
            if (this.getBlock(people.add(-1, 1, 0)).getBlock() == Blocks.AIR && this.godBlocks.contains(this.getBlock(people.add(-1, 0, 0)).getBlock()) && this.getBlock(people.add(-2, 0, 0)).getBlock() == Blocks.REDSTONE_BLOCK) {
                this.switchToSlot(piston);
                BlockUtil.placeBlock2(people.add(-2, 1, 0), EnumHand.MAIN_HAND, true, true, false);
                this.switchToSlot(crystal);
                PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(people.add(-1, 0, 0), EnumFacing.UP, PistonCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            }
            this.switchToSlot(oldslot);
            if (this.getBlock(people.add(-1, 1, 0)).getBlock() == Blocks.PISTON_HEAD) {
                PistonCrystal.mc.playerController.onPlayerDamageBlock(people.add(-1, 1, 0), BlockUtil.getRayTraceFacing(people.add(-1, 1, 0)));
            }
        } else if (!this.godBlocks.contains(this.getBlock(people.add(0, 1, 2)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(0, 0, 2)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(0, 1, 1)).getBlock()) && this.godBlocks.contains(this.getBlock(people.add(0, 0, 1)).getBlock())) {
            if (this.getBlock(people.add(0, 1, 1)).getBlock() == Blocks.AIR && this.getBlock(people.add(0, 0, 2)).getBlock() == Blocks.AIR && this.getBlock(people.add(0, 0, 1)).getBlock() != Blocks.AIR) {
                this.switchToSlot(reblock);
                BlockUtil.placeBlock2(people.add(0, 0, 2), EnumHand.MAIN_HAND, true, true, false);
            }
            if (this.getBlock(people.add(0, 1, 1)).getBlock() == Blocks.AIR && this.godBlocks.contains(this.getBlock(people.add(0, 0, 1)).getBlock()) && this.getBlock(people.add(0, 0, 2)).getBlock() == Blocks.REDSTONE_BLOCK) {
                this.switchToSlot(piston);
                BlockUtil.placeBlock2(people.add(0, 1, 2), EnumHand.MAIN_HAND, true, true, false);
                this.switchToSlot(crystal);
                PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(people.add(0, 0, 1), EnumFacing.UP, PistonCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            }
            this.switchToSlot(oldslot);
            if (this.getBlock(people.add(0, 1, 1)).getBlock() == Blocks.PISTON_HEAD) {
                PistonCrystal.mc.playerController.onPlayerDamageBlock(people.add(0, 1, 1), BlockUtil.getRayTraceFacing(people.add(0, 1, 1)));
            }
        } else if (!this.godBlocks.contains(this.getBlock(people.add(0, 1, -2)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(0, 0, -2)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(0, 1, -1)).getBlock()) && this.godBlocks.contains(this.getBlock(people.add(0, 0, -1)).getBlock())) {
            if (this.getBlock(people.add(0, 1, -1)).getBlock() == Blocks.AIR && this.getBlock(people.add(0, 0, -2)).getBlock() == Blocks.AIR && this.getBlock(people.add(0, 0, -1)).getBlock() != Blocks.AIR) {
                this.switchToSlot(reblock);
                BlockUtil.placeBlock2(people.add(0, 0, -2), EnumHand.MAIN_HAND, true, true, false);
            }
            if (this.getBlock(people.add(0, 1, -1)).getBlock() == Blocks.AIR && this.godBlocks.contains(this.getBlock(people.add(0, 0, -1)).getBlock()) && this.getBlock(people.add(0, 0, -2)).getBlock() == Blocks.REDSTONE_BLOCK) {
                this.switchToSlot(piston);
                BlockUtil.placeBlock2(people.add(0, 1, -2), EnumHand.MAIN_HAND, true, true, false);
                this.switchToSlot(crystal);
                PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(people.add(0, 0, -1), EnumFacing.UP, PistonCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            }
            this.switchToSlot(oldslot);
            if (this.getBlock(people.add(0, 1, -1)).getBlock() == Blocks.PISTON_HEAD) {
                PistonCrystal.mc.playerController.onPlayerDamageBlock(people.add(0, 1, -1), BlockUtil.getRayTraceFacing(people.add(0, 1, -1)));
            }
        } else if (!this.godBlocks.contains(this.getBlock(people.add(1, 2, 0)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(3, 2, 0)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(2, 2, 0)).getBlock()) && this.godBlocks.contains(this.getBlock(people.add(1, 1, 0)).getBlock()) && this.godBlocks.contains(this.getBlock(people.add(2, 1, 0)).getBlock())) {
            this.switchToSlot(piston);
            if (this.getBlock(people.add(3, 1, 0)).getBlock() == Blocks.AIR) {
                BlockUtil.placeBlock2(people.add(3, 1, 0), EnumHand.MAIN_HAND, true, true, false);
            }
            BlockUtil.placeBlock2(people.add(2, 2, 0), EnumHand.MAIN_HAND, true, true, false);
            this.switchToSlot(crystal);
            PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(people.add(1, 1, 0), EnumFacing.UP, PistonCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            this.switchToSlot(reblock);
            BlockUtil.placeBlock2(people.add(3, 2, 0), EnumHand.MAIN_HAND, true, true, false);
            this.switchToSlot(oldslot);
            if (this.getBlock(people.add(1, 2, 0)).getBlock() == Blocks.PISTON_HEAD) {
                PistonCrystal.mc.playerController.onPlayerDamageBlock(people.add(1, 2, 0), BlockUtil.getRayTraceFacing(people.add(1, 2, 0)));
            }
        } else if (!this.godBlocks.contains(this.getBlock(people.add(-1, 2, 0)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(-3, 2, 0)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(-2, 2, 0)).getBlock()) && this.godBlocks.contains(this.getBlock(people.add(-1, 1, 0)).getBlock()) && this.godBlocks.contains(this.getBlock(people.add(-2, 1, 0)).getBlock())) {
            this.switchToSlot(piston);
            if (this.getBlock(people.add(-3, 1, 0)).getBlock() == Blocks.AIR) {
                BlockUtil.placeBlock2(people.add(-3, 1, 0), EnumHand.MAIN_HAND, true, true, false);
            }
            BlockUtil.placeBlock2(people.add(-2, 2, 0), EnumHand.MAIN_HAND, true, true, false);
            this.switchToSlot(crystal);
            PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(people.add(-1, 1, 0), EnumFacing.UP, PistonCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            this.switchToSlot(reblock);
            BlockUtil.placeBlock2(people.add(-3, 2, 0), EnumHand.MAIN_HAND, true, true, false);
            this.switchToSlot(oldslot);
            if (this.getBlock(people.add(-1, 2, 0)).getBlock() == Blocks.PISTON_HEAD) {
                PistonCrystal.mc.playerController.onPlayerDamageBlock(people.add(-1, 2, 0), BlockUtil.getRayTraceFacing(people.add(-1, 2, 0)));
            }
        } else if (!this.godBlocks.contains(this.getBlock(people.add(0, 2, 1)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(0, 2, 3)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(0, 2, 2)).getBlock()) && this.godBlocks.contains(this.getBlock(people.add(0, 1, 1)).getBlock()) && this.godBlocks.contains(this.getBlock(people.add(0, 1, 2)).getBlock())) {
            this.switchToSlot(piston);
            if (this.getBlock(people.add(0, 1, 3)).getBlock() == Blocks.AIR) {
                BlockUtil.placeBlock2(people.add(0, 1, 3), EnumHand.MAIN_HAND, true, true, false);
            }
            BlockUtil.placeBlock2(people.add(0, 2, 2), EnumHand.MAIN_HAND, true, true, false);
            this.switchToSlot(crystal);
            PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(people.add(0, 1, 1), EnumFacing.UP, PistonCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            this.switchToSlot(reblock);
            BlockUtil.placeBlock2(people.add(0, 2, 3), EnumHand.MAIN_HAND, true, true, false);
            this.switchToSlot(oldslot);
            if (this.getBlock(people.add(0, 2, 1)).getBlock() == Blocks.PISTON_HEAD) {
                PistonCrystal.mc.playerController.onPlayerDamageBlock(people.add(0, 2, 1), BlockUtil.getRayTraceFacing(people.add(0, 2, 1)));
            }
        } else if (!this.godBlocks.contains(this.getBlock(people.add(0, 2, -1)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(0, 2, -3)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(0, 2, -2)).getBlock()) && this.godBlocks.contains(this.getBlock(people.add(0, 1, -1)).getBlock()) && this.godBlocks.contains(this.getBlock(people.add(0, 1, -2)).getBlock())) {
            this.switchToSlot(piston);
            if (this.getBlock(people.add(0, 1, -3)).getBlock() == Blocks.AIR) {
                BlockUtil.placeBlock2(people.add(0, 1, -3), EnumHand.MAIN_HAND, true, true, false);
            }
            BlockUtil.placeBlock2(people.add(0, 2, -2), EnumHand.MAIN_HAND, true, true, false);
            this.switchToSlot(crystal);
            PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(people.add(0, 1, -1), EnumFacing.UP, PistonCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            this.switchToSlot(reblock);
            BlockUtil.placeBlock2(people.add(0, 2, -3), EnumHand.MAIN_HAND, true, true, false);
            this.switchToSlot(oldslot);
            if (this.getBlock(people.add(0, 2, -1)).getBlock() == Blocks.PISTON_HEAD) {
                PistonCrystal.mc.playerController.onPlayerDamageBlock(people.add(0, 2, -1), BlockUtil.getRayTraceFacing(people.add(0, 2, -1)));
            }
        } else if (this.getBlock(people.add(0, 2, 0)).getBlock() == Blocks.AIR && !this.godBlocks.contains(this.getBlock(people.add(1, 3, 0)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(3, 3, 0)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(2, 3, 0)).getBlock()) && this.godBlocks.contains(this.getBlock(people.add(1, 2, 0)).getBlock()) && this.godBlocks.contains(this.getBlock(people.add(2, 2, 0)).getBlock())) {
            this.switchToSlot(piston);
            if (this.getBlock(people.add(3, 2, 0)).getBlock() == Blocks.AIR) {
                BlockUtil.placeBlock2(people.add(3, 2, 0), EnumHand.MAIN_HAND, true, true, false);
            }
            BlockUtil.placeBlock2(people.add(2, 3, 0), EnumHand.MAIN_HAND, true, true, false);
            this.switchToSlot(crystal);
            PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(people.add(1, 2, 0), EnumFacing.UP, PistonCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            this.switchToSlot(reblock);
            BlockUtil.placeBlock2(people.add(3, 3, 0), EnumHand.MAIN_HAND, true, true, false);
            this.switchToSlot(oldslot);
            if (this.getBlock(people.add(1, 3, 0)).getBlock() == Blocks.PISTON_HEAD) {
                PistonCrystal.mc.playerController.onPlayerDamageBlock(people.add(1, 3, 0), BlockUtil.getRayTraceFacing(people.add(1, 3, 0)));
            }
        } else if (this.getBlock(people.add(0, 2, 0)).getBlock() == Blocks.AIR && !this.godBlocks.contains(this.getBlock(people.add(-1, 3, 0)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(-3, 3, 0)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(-2, 3, 0)).getBlock()) && this.godBlocks.contains(this.getBlock(people.add(-1, 2, 0)).getBlock()) && this.godBlocks.contains(this.getBlock(people.add(-2, 2, 0)).getBlock())) {
            this.switchToSlot(piston);
            if (this.getBlock(people.add(-3, 2, 0)).getBlock() == Blocks.AIR) {
                BlockUtil.placeBlock2(people.add(-3, 2, 0), EnumHand.MAIN_HAND, true, true, false);
            }
            BlockUtil.placeBlock2(people.add(-2, 3, 0), EnumHand.MAIN_HAND, true, true, false);
            this.switchToSlot(crystal);
            PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(people.add(-1, 2, 0), EnumFacing.UP, PistonCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            this.switchToSlot(reblock);
            BlockUtil.placeBlock2(people.add(-3, 3, 0), EnumHand.MAIN_HAND, true, true, false);
            this.switchToSlot(oldslot);
            if (this.getBlock(people.add(-1, 3, 0)).getBlock() == Blocks.PISTON_HEAD) {
                PistonCrystal.mc.playerController.onPlayerDamageBlock(people.add(-1, 3, 0), BlockUtil.getRayTraceFacing(people.add(-1, 3, 0)));
            }
        } else if (this.getBlock(people.add(0, 2, 0)).getBlock() == Blocks.AIR && !this.godBlocks.contains(this.getBlock(people.add(0, 3, 1)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(0, 3, 3)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(0, 3, 2)).getBlock()) && this.godBlocks.contains(this.getBlock(people.add(0, 2, 1)).getBlock()) && this.godBlocks.contains(this.getBlock(people.add(0, 2, 2)).getBlock())) {
            this.switchToSlot(piston);
            if (this.getBlock(people.add(0, 2, 3)).getBlock() == Blocks.AIR) {
                BlockUtil.placeBlock2(people.add(0, 2, 3), EnumHand.MAIN_HAND, true, true, false);
            }
            BlockUtil.placeBlock2(people.add(0, 3, 2), EnumHand.MAIN_HAND, true, true, false);
            this.switchToSlot(crystal);
            PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(people.add(0, 2, 1), EnumFacing.UP, PistonCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            this.switchToSlot(reblock);
            BlockUtil.placeBlock2(people.add(0, 3, 3), EnumHand.MAIN_HAND, true, true, false);
            this.switchToSlot(oldslot);
            if (this.getBlock(people.add(0, 3, 1)).getBlock() == Blocks.PISTON_HEAD) {
                PistonCrystal.mc.playerController.onPlayerDamageBlock(people.add(0, 3, 1), BlockUtil.getRayTraceFacing(people.add(0, 3, 1)));
            }
        } else if (this.getBlock(people.add(0, 2, 0)).getBlock() == Blocks.AIR && !this.godBlocks.contains(this.getBlock(people.add(0, 3, -1)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(0, 3, -3)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(0, 3, -2)).getBlock()) && this.godBlocks.contains(this.getBlock(people.add(0, 2, -1)).getBlock()) && this.godBlocks.contains(this.getBlock(people.add(0, 2, -2)).getBlock())) {
            this.switchToSlot(piston);
            if (this.getBlock(people.add(0, 2, -3)).getBlock() == Blocks.AIR) {
                BlockUtil.placeBlock2(people.add(0, 2, -3), EnumHand.MAIN_HAND, true, true, false);
            }
            BlockUtil.placeBlock2(people.add(0, 3, -2), EnumHand.MAIN_HAND, true, true, false);
            this.switchToSlot(crystal);
            PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(people.add(0, 2, -1), EnumFacing.UP, PistonCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            this.switchToSlot(reblock);
            BlockUtil.placeBlock2(people.add(0, 3, -3), EnumHand.MAIN_HAND, true, true, false);
            this.switchToSlot(oldslot);
            if (this.getBlock(people.add(0, 3, -1)).getBlock() == Blocks.PISTON_HEAD) {
                PistonCrystal.mc.playerController.onPlayerDamageBlock(people.add(0, 3, -1), BlockUtil.getRayTraceFacing(people.add(0, 3, -1)));
            }
        } else if (this.godBlocks.contains(this.getBlock(people.add(1, 0, 0)).getBlock()) && this.godBlocks.contains(this.getBlock(people.add(2, 0, 0)).getBlock()) && this.godBlocks.contains(this.getBlock(people.add(3, 0, 0)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(1, 1, 0)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(2, 1, 0)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(3, 1, 0)).getBlock())) {
            this.switchToSlot(piston);
            BlockUtil.placeBlock2(people.add(2, 1, 0), EnumHand.MAIN_HAND, true, true, false);
            this.switchToSlot(crystal);
            PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(people.add(1, 0, 0), EnumFacing.UP, PistonCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            this.switchToSlot(reblock);
            BlockUtil.placeBlock2(people.add(3, 1, 0), EnumHand.MAIN_HAND, true, true, false);
            this.switchToSlot(oldslot);
            if (this.getBlock(people.add(1, 1, 0)).getBlock() == Blocks.PISTON_HEAD) {
                PistonCrystal.mc.playerController.onPlayerDamageBlock(people.add(1, 1, 0), BlockUtil.getRayTraceFacing(people.add(1, 1, 0)));
            }
        } else if (this.godBlocks.contains(this.getBlock(people.add(-1, 0, 0)).getBlock()) && this.godBlocks.contains(this.getBlock(people.add(-2, 0, 0)).getBlock()) && this.godBlocks.contains(this.getBlock(people.add(-3, 0, 0)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(-1, 1, 0)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(-2, 1, 0)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(-3, 1, 0)).getBlock())) {
            this.switchToSlot(piston);
            BlockUtil.placeBlock2(people.add(-2, 1, 0), EnumHand.MAIN_HAND, true, true, false);
            this.switchToSlot(crystal);
            PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(people.add(-1, 0, 0), EnumFacing.UP, PistonCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            this.switchToSlot(reblock);
            BlockUtil.placeBlock2(people.add(-3, 1, 0), EnumHand.MAIN_HAND, true, true, false);
            this.switchToSlot(oldslot);
            if (this.getBlock(people.add(-1, 1, 0)).getBlock() == Blocks.PISTON_HEAD) {
                PistonCrystal.mc.playerController.onPlayerDamageBlock(people.add(-1, 1, 0), BlockUtil.getRayTraceFacing(people.add(-1, 1, 0)));
            }
        } else if (this.godBlocks.contains(this.getBlock(people.add(0, 0, 1)).getBlock()) && this.godBlocks.contains(this.getBlock(people.add(0, 0, 2)).getBlock()) && this.godBlocks.contains(this.getBlock(people.add(0, 0, 3)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(0, 1, 1)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(0, 1, 2)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(0, 1, 3)).getBlock())) {
            this.switchToSlot(piston);
            BlockUtil.placeBlock2(people.add(0, 1, 2), EnumHand.MAIN_HAND, true, true, false);
            this.switchToSlot(crystal);
            PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(people.add(0, 0, 1), EnumFacing.UP, PistonCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            this.switchToSlot(reblock);
            BlockUtil.placeBlock2(people.add(0, 1, 3), EnumHand.MAIN_HAND, true, true, false);
            this.switchToSlot(oldslot);
            if (this.getBlock(people.add(0, 1, 1)).getBlock() == Blocks.PISTON_HEAD) {
                PistonCrystal.mc.playerController.onPlayerDamageBlock(people.add(0, 1, 1), BlockUtil.getRayTraceFacing(people.add(0, 1, 1)));
            }
        } else if (this.godBlocks.contains(this.getBlock(people.add(0, 0, -1)).getBlock()) && this.godBlocks.contains(this.getBlock(people.add(0, 0, -2)).getBlock()) && this.godBlocks.contains(this.getBlock(people.add(0, 0, -3)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(0, 1, -1)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(0, 1, -2)).getBlock()) && !this.godBlocks.contains(this.getBlock(people.add(0, 1, -3)).getBlock())) {
            this.switchToSlot(piston);
            BlockUtil.placeBlock2(people.add(0, 1, -2), EnumHand.MAIN_HAND, true, true, false);
            this.switchToSlot(crystal);
            PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(people.add(0, 0, -1), EnumFacing.UP, PistonCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            this.switchToSlot(reblock);
            BlockUtil.placeBlock2(people.add(0, 1, -3), EnumHand.MAIN_HAND, true, true, false);
            this.switchToSlot(oldslot);
            if (this.getBlock(people.add(0, 1, -1)).getBlock() == Blocks.PISTON_HEAD) {
                PistonCrystal.mc.playerController.onPlayerDamageBlock(people.add(0, 1, -1), BlockUtil.getRayTraceFacing(people.add(0, 1, -1)));
            }
        }
        if ((t_crystal = (Entity)PistonCrystal.mc.world.loadedEntityList.stream().filter(p -> p instanceof EntityEnderCrystal).min(Comparator.comparing(c -> Float.valueOf(this.target.getDistance(c)))).orElse(null)) == null) {
            return;
        }
        PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(t_crystal));
    }

    private EntityPlayer getTarget(double range) {
        EntityPlayer target = null;
        double distance = range;
        for (EntityPlayer player : PistonCrystal.mc.world.playerEntities) {
            if (EntityUtil.isntValid((Entity)player, range) || OyVey.friendManager.isFriend(player.getName()) || PistonCrystal.mc.player.posY - player.posY >= 5.0) continue;
            if (target == null) {
                target = player;
                distance = EntityUtil.mc.player.getDistanceSq((Entity)player);
                continue;
            }
            if (EntityUtil.mc.player.getDistanceSq((Entity)player) >= distance) continue;
            target = player;
            distance = EntityUtil.mc.player.getDistanceSq((Entity)player);
        }
        return target;
    }

    private void switchToSlot(int slot) {
        PistonCrystal.mc.player.inventory.currentItem = slot;
        PistonCrystal.mc.playerController.updateController();
    }

    private int findMaterials(Block b) {
        for (int i = 0; i < 9; ++i) {
            if (!(PistonCrystal.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) || ((ItemBlock)PistonCrystal.mc.player.inventory.getStackInSlot(i).getItem()).getBlock() != b) continue;
            return i;
        }
        return -1;
    }

    private IBlockState getBlock(BlockPos block) {
        return PistonCrystal.mc.world.getBlockState(block);
    }
}

