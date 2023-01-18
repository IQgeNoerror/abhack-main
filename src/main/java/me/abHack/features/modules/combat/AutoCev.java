/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.MoverType
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3i
 */
package me.abHack.features.modules.combat;

import java.util.Arrays;
import me.abHack.OyVey;
import me.abHack.features.command.Command;
import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import me.abHack.util.BlockUtils;
import me.abHack.util.CrystalUtil;
import me.abHack.util.InventoryUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class AutoCev
extends Module {
    private final Setting<Double> range = this.register(new Setting<Double>("Range", 4.9, 0.0, 6.0));
    Entity currentEntity;
    boolean flag;
    int progress = 0;
    int sleep;
    int civCounter;
    boolean breakFlag;
    private BlockPos breakPos;

    public AutoCev() {
        super("AutoCev", "AutoCev", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private int findItem(Item item) {
        if (item == Items.END_CRYSTAL && AutoCev.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            return 999;
        }
        for (int i = 0; i < 9; ++i) {
            if (AutoCev.mc.player.inventory.getStackInSlot(i).getItem() != item) continue;
            return i;
        }
        return -1;
    }

    @Override
    public void onEnable() {
        this.findTarget();
        this.progress = 0;
        this.breakFlag = false;
        this.flag = false;
        this.civCounter = 0;
        this.sleep = 0;
        super.onEnable();
    }

    @Override
    public void onTick() {
        int n = this.findItem(Items.DIAMOND_PICKAXE);
        int n2 = this.findItem(Items.END_CRYSTAL);
        int n3 = this.findMaterials(Blocks.OBSIDIAN);
        BlockPos[] objectArray = new BlockPos[]{new BlockPos(0, 0, 1), new BlockPos(0, 1, 1), new BlockPos(0, 2, 1), new BlockPos(0, 2, 0)};
        int n4 = InventoryUtil.getSlot();
        if (n != -1 && n2 != -1 && n3 != -1) {
            if (this.currentEntity == null || (double)this.currentEntity.getDistance((Entity)AutoCev.mc.player) > this.range.getValue()) {
                this.findTarget();
            }
            if (this.currentEntity != null) {
                if (this.currentEntity.isDead) {
                    this.disable();
                    return;
                }
                Entity entity = this.currentEntity;
                OyVey.targetManager.updateTarget((EntityLivingBase)entity);
                if (entity instanceof EntityPlayer && !OyVey.friendManager.isFriend(entity.getName())) {
                    if (this.sleep > 0) {
                        --this.sleep;
                    } else {
                        entity.move(MoverType.SELF, 0.0, -2.0, 0.0);
                        switch (this.progress) {
                            case 0: {
                                BlockPos blockPos = new BlockPos(entity);
                                BlockPos[] var16 = objectArray;
                                int var17 = objectArray.length;
                                for (int var10 = 0; var10 < var17; ++var10) {
                                    BlockPos blockPos2 = var16[var10];
                                    if (Arrays.asList(objectArray).indexOf(blockPos2) != -1 && this.civCounter < 1) {
                                        this.flag = true;
                                        InventoryUtil.setSlot(n3);
                                    } else {
                                        InventoryUtil.setSlot(n3);
                                    }
                                    BlockUtils blockUtils = BlockUtils.isPlaceable(blockPos.add((Vec3i)blockPos2), 0.0, true);
                                    if (blockUtils == null) continue;
                                    blockUtils.doPlace(true);
                                }
                                InventoryUtil.setSlot(n2);
                                CrystalUtil.placeCrystal(new BlockPos(entity.posX, entity.posY + 3.0, entity.posZ));
                                ++this.progress;
                                break;
                            }
                            case 1: {
                                InventoryUtil.setSlot(n);
                                AutoCev.mc.playerController.onPlayerDamageBlock(new BlockPos(entity).add(0, 2, 0), EnumFacing.UP);
                                mc.getConnection().sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, new BlockPos(entity).add(0, 2, 0), EnumFacing.UP));
                                if (AutoCev.mc.world.isAirBlock(new BlockPos(entity).add(0, 2, 0))) {
                                    for (Entity entity2 : AutoCev.mc.world.loadedEntityList) {
                                        if (!((double)entity.getDistance(entity2) <= this.range.getValue()) || !(entity2 instanceof EntityEnderCrystal)) continue;
                                        AutoCev.mc.playerController.attackEntity((EntityPlayer)AutoCev.mc.player, entity2);
                                    }
                                    this.breakFlag = true;
                                }
                                if (this.civCounter < 1) {
                                    AutoCev.mc.playerController.onPlayerDamageBlock(new BlockPos(entity).add(0, 2, 0), EnumFacing.UP);
                                    this.sleep += 30;
                                }
                                ++this.progress;
                                break;
                            }
                            case 2: {
                                int n5 = 0;
                                for (Entity entity3 : AutoCev.mc.world.loadedEntityList) {
                                    if (!((double)entity.getDistance(entity3) <= this.range.getValue()) || !(entity3 instanceof EntityEnderCrystal)) continue;
                                    AutoCev.mc.playerController.attackEntity((EntityPlayer)AutoCev.mc.player, entity3);
                                    ++n5;
                                }
                                if (n5 != 0 && !this.flag) break;
                                ++this.progress;
                                break;
                            }
                            case 3: {
                                BlockUtils.doPlace(BlockUtils.isPlaceable(new BlockPos(entity.posX, entity.posY + 2.0, entity.posZ), 0.0, true), true);
                                InventoryUtil.setSlot(n3);
                                this.progress = 0;
                                ++this.civCounter;
                            }
                        }
                    }
                    InventoryUtil.setSlot(n4);
                    return;
                }
                InventoryUtil.setSlot(n4);
            }
        } else {
            Command.sendMessage("Pix or Crystal or Obsidian No Material");
            this.disable();
        }
    }

    private int findMaterials(Block block) {
        for (int i = 0; i < 9; ++i) {
            if (!(AutoCev.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) || ((ItemBlock)AutoCev.mc.player.inventory.getStackInSlot(i).getItem()).getBlock() != block) continue;
            return i;
        }
        return -1;
    }

    public void findTarget() {
        this.currentEntity = AutoCev.mc.world.loadedEntityList.stream().filter(entity -> entity != AutoCev.mc.player && entity instanceof EntityLivingBase && (double)entity.getDistance((Entity)AutoCev.mc.player) < this.range.getValue() && !OyVey.friendManager.isFriend(entity.getName())).findFirst().orElse(null);
    }
}

