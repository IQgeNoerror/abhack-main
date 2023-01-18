/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 */
package me.abHack.features.modules.player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import me.abHack.util.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class Burrow
extends Module {
    private int oldSlot = -1;
    private BlockPos originalPos;
    public Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    private final Setting<Double> offset = this.register(new Setting<Double>("Offset", 1.2, -5.0, 10.0));

    public Burrow() {
        super("Burrow", "Rubberbands you into a block", Module.Category.PLAYER, true, false, false);
    }

    @Override
    public void onEnable() {
        this.originalPos = new BlockPos(Burrow.mc.player.posX, Burrow.mc.player.posY, Burrow.mc.player.posZ);
        if (Burrow.mc.world.getBlockState(new BlockPos(Burrow.mc.player.posX, Burrow.mc.player.posY, Burrow.mc.player.posZ)).getBlock().equals(Blocks.OBSIDIAN) || Burrow.mc.world.getBlockState(new BlockPos(Burrow.mc.player.posX, Burrow.mc.player.posY, Burrow.mc.player.posZ)).getBlock().equals(Blocks.ENDER_CHEST) || this.intersectsWithEntity(this.originalPos)) {
            this.toggle();
            return;
        }
        this.oldSlot = Burrow.mc.player.inventory.currentItem;
    }

    @Override
    public void onUpdate() {
        if (Burrow.getBlockSlot(Blocks.OBSIDIAN) == -1 && Burrow.getBlockSlot(Blocks.ENDER_CHEST) == -1) {
            this.toggle();
            return;
        }
        if (Burrow.getBlockSlot(Blocks.OBSIDIAN) != -1) {
            this.switchToSlot(Burrow.getBlockSlot(Blocks.OBSIDIAN), false);
        } else {
            this.switchToSlot(Burrow.getBlockSlot(Blocks.ENDER_CHEST), false);
        }
        Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 0.41999998688698, Burrow.mc.player.posZ, true));
        Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 0.7531999805211997, Burrow.mc.player.posZ, true));
        Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 1.00133597911214, Burrow.mc.player.posZ, true));
        Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 1.06610926093821, Burrow.mc.player.posZ, true));
        this.placeBlock(this.originalPos, EnumHand.MAIN_HAND, this.rotate.getValue(), false);
        Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + this.offset.getValue(), Burrow.mc.player.posZ, false));
        this.switchToSlot(this.oldSlot, false);
        this.toggle();
    }

    public List<EnumFacing> getPossibleSides(BlockPos pos) {
        ArrayList<EnumFacing> facings = new ArrayList<EnumFacing>();
        if (Burrow.mc.world == null || pos == null) {
            return facings;
        }
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbour = pos.offset(side);
            IBlockState blockState = Burrow.mc.world.getBlockState(neighbour);
            if (blockState == null || !blockState.getBlock().canCollideCheck(blockState, false) || blockState.getMaterial().isReplaceable()) continue;
            facings.add(side);
        }
        return facings;
    }

    public void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction, boolean packet) {
        if (packet) {
            float f = (float)(vec.x - (double)pos.getX());
            float f1 = (float)(vec.y - (double)pos.getY());
            float f2 = (float)(vec.z - (double)pos.getZ());
            Burrow.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
        } else {
            Burrow.mc.playerController.processRightClickBlock(Burrow.mc.player, Burrow.mc.world, pos, direction, vec, hand);
        }
        Burrow.mc.player.swingArm(EnumHand.MAIN_HAND);
        Burrow.mc.rightClickDelayTimer = 4;
    }

    public boolean placeBlock(BlockPos pos, EnumHand hand, boolean rotate, boolean isSneaking) {
        boolean sneaking = false;
        EnumFacing side = null;
        Iterator<EnumFacing> iterator = this.getPossibleSides(pos).iterator();
        if (iterator.hasNext()) {
            side = iterator.next();
        }
        if (side == null) {
            return isSneaking;
        }
        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        Block neighbourBlock = Burrow.mc.world.getBlockState(neighbour).getBlock();
        if (!Burrow.mc.player.isSneaking() && (BlockUtil.blackList.contains(neighbourBlock) || BlockUtil.shulkerList.contains(neighbourBlock))) {
            Burrow.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Burrow.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            Burrow.mc.player.setSneaking(true);
            sneaking = true;
        }
        this.rightClickBlock(neighbour, hitVec, hand, opposite, true);
        Burrow.mc.player.swingArm(EnumHand.MAIN_HAND);
        Burrow.mc.rightClickDelayTimer = 4;
        return sneaking || isSneaking;
    }

    private boolean intersectsWithEntity(BlockPos pos) {
        for (Entity entity : Burrow.mc.world.loadedEntityList) {
            if (entity.equals((Object)Burrow.mc.player) || entity instanceof EntityItem || !new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) continue;
            return true;
        }
        return false;
    }

    public static int getBlockSlot(Block block) {
        for (int i = 0; i < 9; ++i) {
            ItemBlock itemBlock;
            ItemStack itemStack = Burrow.mc.player.inventory.getStackInSlot(i);
            if (itemStack == ItemStack.EMPTY) {
                return -1;
            }
            if (!(itemStack.getItem() instanceof ItemBlock) || (itemBlock = (ItemBlock)itemStack.getItem()).getBlock() != block) continue;
            return i;
        }
        return -1;
    }

    public void switchToSlot(int Slot2, boolean packet) {
        if (packet) {
            Burrow.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(Slot2));
            Burrow.mc.playerController.updateController();
        } else {
            Burrow.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(Slot2));
            Burrow.mc.player.inventory.currentItem = Slot2;
            Burrow.mc.playerController.updateController();
        }
    }
}

