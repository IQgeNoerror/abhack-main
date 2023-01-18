/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockHopper
 *  net.minecraft.block.BlockShulkerBox
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package me.abHack.features.modules.combat;

import me.abHack.OyVey;
import me.abHack.features.modules.Module;
import me.abHack.features.modules.player.InstantMine;
import me.abHack.features.setting.Setting;
import me.abHack.util.BlockUtil;
import me.abHack.util.EntityUtil;
import me.abHack.util.InventoryUtil;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class AutoCity
extends Module {
    private static AutoCity INSTANCE = new AutoCity();
    private final Setting<Float> range = this.register(new Setting<Float>("Range", Float.valueOf(5.0f), Float.valueOf(1.0f), Float.valueOf(8.0f)));
    private final Setting<Boolean> toggle = this.register(new Setting<Boolean>("AutoToggle", false));
    public final Setting<Boolean> priority = this.register(new Setting<Boolean>("City Priority ", true));
    public static EntityPlayer target;

    public AutoCity() {
        super("AutoCity", "AutoCity", Module.Category.COMBAT, true, false, false);
        this.setInstance();
    }

    public static AutoCity getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AutoCity();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onTick() {
        if (AutoCity.fullNullCheck()) {
            return;
        }
        if (OyVey.moduleManager.isModuleEnabled("AutoCev")) {
            return;
        }
        if (InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) == -1) {
            return;
        }
        target = this.getTarget(this.range.getValue().floatValue());
        this.surroundMine();
    }

    @Override
    public String getDisplayInfo() {
        if (target != null) {
            return target.getName();
        }
        return null;
    }

    private void surroundMine() {
        if (target != null) {
            Vec3d a = target.getPositionVector();
            if (EntityUtil.getSurroundWeakness(a, 1, -1)) {
                this.surroundMine(a, -1.0, 0.0, 0.0);
            } else if (EntityUtil.getSurroundWeakness(a, 2, -1)) {
                this.surroundMine(a, 1.0, 0.0, 0.0);
            } else if (EntityUtil.getSurroundWeakness(a, 3, -1)) {
                this.surroundMine(a, 0.0, 0.0, -1.0);
            } else if (EntityUtil.getSurroundWeakness(a, 4, -1)) {
                this.surroundMine(a, 0.0, 0.0, 1.0);
            } else if (EntityUtil.getSurroundWeakness(a, 5, -1)) {
                this.surroundMine(a, -1.0, 0.0, 0.0);
            } else if (EntityUtil.getSurroundWeakness(a, 6, -1)) {
                this.surroundMine(a, 1.0, 0.0, 0.0);
            } else if (EntityUtil.getSurroundWeakness(a, 7, -1)) {
                this.surroundMine(a, 0.0, 0.0, -1.0);
            } else if (EntityUtil.getSurroundWeakness(a, 8, -1)) {
                this.surroundMine(a, 0.0, 0.0, 1.0);
            } else {
                target = null;
            }
        }
        if (this.toggle.getValue().booleanValue()) {
            this.toggle();
        }
    }

    private void surroundMine(Vec3d pos, double x, double y, double z) {
        BlockPos position = new BlockPos(pos).add(x, y, z);
        if (InstantMine.getInstance().isOff()) {
            InstantMine.getInstance().enable();
            return;
        }
        if (!InstantMine.getInstance().isOn()) {
            return;
        }
        if (InstantMine.breakPos != null) {
            if (InstantMine.breakPos.equals((Object)position)) {
                return;
            }
            if (InstantMine.breakPos.equals((Object)new BlockPos(AutoCity.target.posX, AutoCity.target.posY, AutoCity.target.posZ))) {
                if (AutoCity.mc.world.getBlockState(new BlockPos(AutoCity.target.posX, AutoCity.target.posY, AutoCity.target.posZ)).getBlock() != Blocks.AIR && !this.priority.getValue().booleanValue()) {
                    return;
                }
            }
            if (InstantMine.breakPos.equals((Object)new BlockPos(AutoCity.mc.player.posX, AutoCity.mc.player.posY + 2.0, AutoCity.mc.player.posZ))) {
                return;
            }
            if (InstantMine.breakPos.equals((Object)new BlockPos(AutoCity.mc.player.posX, AutoCity.mc.player.posY - 1.0, AutoCity.mc.player.posZ))) {
                return;
            }
            if (AutoCity.mc.world.getBlockState(InstantMine.breakPos).getBlock() == Blocks.WEB) {
                return;
            }
            if (OyVey.moduleManager.isModuleEnabled("Anti32k") && AutoCity.mc.world.getBlockState(InstantMine.breakPos).getBlock() instanceof BlockHopper) {
                return;
            }
            if (OyVey.moduleManager.isModuleEnabled("AntiShulkerBox") && AutoCity.mc.world.getBlockState(InstantMine.breakPos).getBlock() instanceof BlockShulkerBox) {
                return;
            }
        }
        AutoCity.mc.playerController.onPlayerDamageBlock(position, BlockUtil.getRayTraceFacing(position));
    }

    private EntityPlayer getTarget(double range) {
        EntityPlayer target = null;
        double distance = range;
        for (EntityPlayer player : AutoCity.mc.world.playerEntities) {
            if (EntityUtil.isntValid((Entity)player, range) || this.check(player) || OyVey.friendManager.isFriend(player.getName()) || AutoCity.mc.player.posY - player.posY >= 5.0) continue;
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

    public boolean check(EntityPlayer player) {
        return AutoCity.mc.world.getBlockState(new BlockPos(player.posX + 1.0, player.posY, player.posZ)).getBlock() == Blocks.AIR || AutoCity.mc.world.getBlockState(new BlockPos(player.posX - 1.0, player.posY, player.posZ)).getBlock() == Blocks.AIR || AutoCity.mc.world.getBlockState(new BlockPos(player.posX, player.posY, player.posZ + 1.0)).getBlock() == Blocks.AIR || AutoCity.mc.world.getBlockState(new BlockPos(player.posX, player.posY, player.posZ - 1.0)).getBlock() == Blocks.AIR;
    }
}

