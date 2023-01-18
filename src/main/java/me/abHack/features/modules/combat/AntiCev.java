/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.abHack.features.modules.combat;

import me.abHack.event.events.PacketEvent;
import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import me.abHack.util.BlockUtil;
import me.abHack.util.EntityUtil;
import me.abHack.util.InventoryUtil;
import me.abHack.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiCev
extends Module {
    public Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    public Setting<Boolean> packet = this.register(new Setting<Boolean>("Packet", true));
    private float yaw = 0.0f;
    private float pitch = 0.0f;
    private boolean rotating = false;
    private boolean isSneaking;

    public AntiCev() {
        super("AntiCev", "AntiCev", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onTick() {
        if (!AntiCev.fullNullCheck() && InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) {
            this.main();
        }
    }

    @Override
    public void onDisable() {
        this.rotating = false;
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getStage() == 0 && this.rotate.getValue().booleanValue() && this.rotating && event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            packet.yaw = this.yaw;
            packet.pitch = this.pitch;
            this.rotating = false;
        }
    }

    private void main() {
        Vec3d a = AntiCev.mc.player.getPositionVector();
        if (this.checkTrap(a, EntityUtil.getOffsets(2, false)) && this.checkTrap(a, EntityUtil.getVarOffsets(0, 2, 0)) && this.checkCrystal(a, EntityUtil.getVarOffsets(0, 3, 0)) != null) {
            AntiCev.mc.player.motionY += 1.0;
            if (this.packet.getValue().booleanValue()) {
                this.rotateTo(this.checkCrystal(a, EntityUtil.getVarOffsets(0, 3, 0)));
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(0, 3, 0)), true);
            } else {
                this.rotateTo(this.checkCrystal(a, EntityUtil.getVarOffsets(0, 3, 0)));
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(0, 3, 0)), false);
            }
            this.rotateToPos(a, EntityUtil.getVarOffsets(0, 3, 0));
            this.place(a, EntityUtil.getVarOffsets(0, 3, 0));
        }
    }

    private void rotateTo(Entity entity) {
        if (this.rotate.getValue().booleanValue()) {
            float[] angle = MathUtil.calcAngle(AntiCev.mc.player.getPositionEyes(mc.getRenderPartialTicks()), entity.getPositionVector());
            this.yaw = angle[0];
            this.pitch = angle[1];
            this.rotating = true;
        }
    }

    private void rotateToPos(Vec3d pos, Vec3d[] list) {
        if (this.rotate.getValue().booleanValue()) {
            Vec3d[] var3 = list;
            int var4 = list.length;
            for (int var5 = 0; var5 < var4; ++var5) {
                Vec3d vec3d = var3[var5];
                BlockPos position = new BlockPos(pos).add(vec3d.x, vec3d.y, vec3d.z);
                float[] angle = MathUtil.calcAngle(AntiCev.mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((double)((float)position.getX() + 0.5f), (double)((float)position.getY() - 0.5f), (double)((float)position.getZ() + 0.5f)));
                this.yaw = angle[0];
                this.pitch = angle[1];
                this.rotating = true;
            }
        }
    }

    private void place(Vec3d pos, Vec3d[] list) {
        Vec3d[] var3 = list;
        int var4 = list.length;
        for (int var5 = 0; var5 < var4; ++var5) {
            Vec3d vec3d = var3[var5];
            BlockPos position = new BlockPos(pos).add(vec3d.x, vec3d.y, vec3d.z);
            int a = AntiCev.mc.player.inventory.currentItem;
            AntiCev.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            AntiCev.mc.playerController.updateController();
            this.isSneaking = BlockUtil.placeBlock(position, EnumHand.MAIN_HAND, false, this.packet.getValue(), true);
            AntiCev.mc.player.inventory.currentItem = a;
            AntiCev.mc.playerController.updateController();
        }
    }

    Entity checkCrystal(Vec3d pos, Vec3d[] list) {
        Entity crystal = null;
        Vec3d[] var4 = list;
        int var5 = list.length;
        for (int var6 = 0; var6 < var5; ++var6) {
            Vec3d vec3d = var4[var6];
            BlockPos position = new BlockPos(pos).add(vec3d.x, vec3d.y, vec3d.z);
            for (Entity entity : AntiCev.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(position))) {
                if (!(entity instanceof EntityEnderCrystal) || crystal != null) continue;
                crystal = entity;
            }
        }
        return crystal;
    }

    private boolean checkTrap(Vec3d pos, Vec3d[] list) {
        Vec3d[] var3 = list;
        int var4 = list.length;
        for (int var5 = 0; var5 < var4; ++var5) {
            Vec3d vec3d = var3[var5];
            BlockPos position = new BlockPos(pos).add(vec3d.x, vec3d.y, vec3d.z);
            Block block = EntityUtil.mc.world.getBlockState(position).getBlock();
            if (block != Blocks.OBSIDIAN) continue;
            return true;
        }
        return false;
    }
}

