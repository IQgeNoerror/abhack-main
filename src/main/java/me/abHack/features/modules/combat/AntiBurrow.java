/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockHopper
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.BlockShulkerBox
 *  net.minecraft.client.gui.GuiHopper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 */
package me.abHack.features.modules.combat;

import me.abHack.OyVey;
import me.abHack.features.modules.Module;
import me.abHack.features.modules.combat.AutoCity;
import me.abHack.features.modules.combat.AutoTrap;
import me.abHack.features.modules.player.InstantMine;
import me.abHack.features.setting.Setting;
import me.abHack.util.BlockUtil;
import me.abHack.util.EntityUtil;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class AntiBurrow
extends Module {
    private final Setting<Double> range = this.register(new Setting<Double>("Range", 5.0, 1.0, 8.0));
    private final Setting<Boolean> toggle = this.register(new Setting<Boolean>("Toggle", false));
    public static BlockPos pos;

    public AntiBurrow() {
        super("AntiBurrow", "AntiBurrow", Module.Category.COMBAT, true, false, false);
    }

    private EntityPlayer getTarget(double range) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (EntityPlayer player : AutoTrap.mc.world.playerEntities) {
            if (EntityUtil.isntValid((Entity)player, range) || OyVey.speedManager.getPlayerSpeed(player) > 10.0) continue;
            if (target == null) {
                target = player;
                distance = AutoTrap.mc.player.getDistanceSq((Entity)player);
                continue;
            }
            if (AutoTrap.mc.player.getDistanceSq((Entity)player) >= distance) continue;
            target = player;
            distance = AutoTrap.mc.player.getDistanceSq((Entity)player);
        }
        return target;
    }

    @Override
    public void onUpdate() {
        if (AntiBurrow.fullNullCheck()) {
            return;
        }
        if (AntiBurrow.mc.currentScreen instanceof GuiHopper) {
            return;
        }
        EntityPlayer player = this.getTarget(this.range.getValue());
        if (this.toggle.getValue().booleanValue()) {
            this.toggle();
        }
        if (player == null) {
            return;
        }
        pos = new BlockPos(player.posX, player.posY + 0.5, player.posZ);
        if (pos == null) {
            return;
        }
        if (InstantMine.breakPos != null) {
            if (InstantMine.breakPos.equals((Object)pos)) {
                return;
            }
            if (OyVey.moduleManager.isModuleEnabled("AutoCity") && AutoCity.target != null && AutoCity.getInstance().priority.getValue().booleanValue()) {
                return;
            }
            if (InstantMine.breakPos.equals((Object)new BlockPos(AntiBurrow.mc.player.posX, AntiBurrow.mc.player.posY + 2.0, AntiBurrow.mc.player.posZ))) {
                return;
            }
            if (InstantMine.breakPos.equals((Object)new BlockPos(AntiBurrow.mc.player.posX, AntiBurrow.mc.player.posY - 1.0, AntiBurrow.mc.player.posZ))) {
                return;
            }
            if (AntiBurrow.mc.world.getBlockState(InstantMine.breakPos).getBlock() == Blocks.WEB) {
                return;
            }
            if (OyVey.moduleManager.isModuleEnabled("Anti32k") && AntiBurrow.mc.world.getBlockState(InstantMine.breakPos).getBlock() instanceof BlockHopper) {
                return;
            }
            if (OyVey.moduleManager.isModuleEnabled("AntiShulkerBox") && AntiBurrow.mc.world.getBlockState(InstantMine.breakPos).getBlock() instanceof BlockShulkerBox) {
                return;
            }
        }
        if (AntiBurrow.mc.world.getBlockState(pos).getBlock() != Blocks.AIR && !this.isOnLiquid() && !this.isInLiquid() && AntiBurrow.mc.world.getBlockState(pos).getBlock() != Blocks.WATER && AntiBurrow.mc.world.getBlockState(pos).getBlock() != Blocks.LAVA) {
            AntiBurrow.mc.player.swingArm(EnumHand.MAIN_HAND);
            AntiBurrow.mc.playerController.onPlayerDamageBlock(pos, BlockUtil.getRayTraceFacing(pos));
        }
    }

    private boolean isOnLiquid() {
        double y = AntiBurrow.mc.player.posY - 0.03;
        for (int x = MathHelper.floor((double)AntiBurrow.mc.player.posX); x < MathHelper.ceil((double)AntiBurrow.mc.player.posX); ++x) {
            for (int z = MathHelper.floor((double)AntiBurrow.mc.player.posZ); z < MathHelper.ceil((double)AntiBurrow.mc.player.posZ); ++z) {
                BlockPos pos = new BlockPos(x, MathHelper.floor((double)y), z);
                if (!(AntiBurrow.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid)) continue;
                return true;
            }
        }
        return false;
    }

    private boolean isInLiquid() {
        double y = AntiBurrow.mc.player.posY + 0.01;
        for (int x = MathHelper.floor((double)AntiBurrow.mc.player.posX); x < MathHelper.ceil((double)AntiBurrow.mc.player.posX); ++x) {
            for (int z = MathHelper.floor((double)AntiBurrow.mc.player.posZ); z < MathHelper.ceil((double)AntiBurrow.mc.player.posZ); ++z) {
                BlockPos pos = new BlockPos(x, (int)y, z);
                if (!(AntiBurrow.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid)) continue;
                return true;
            }
        }
        return false;
    }
}

