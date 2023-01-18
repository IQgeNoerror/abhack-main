/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.abHack.features.modules.combat;

import me.abHack.OyVey;
import me.abHack.event.events.UpdateWalkingPlayerEvent;
import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import me.abHack.util.DamageUtil;
import me.abHack.util.EntityUtil;
import me.abHack.util.InventoryUtil;
import me.abHack.util.MathUtil;
import me.abHack.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Killaura
extends Module {
    public static Entity target;
    private final Timer timer = new Timer();
    public Setting<Float> range = this.register(new Setting<Float>("Range", Float.valueOf(6.0f), Float.valueOf(0.1f), Float.valueOf(7.0f)));
    public Setting<Boolean> delay = this.register(new Setting<Boolean>("HitDelay", true));
    public Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    public Setting<Boolean> onlySharp = this.register(new Setting<Boolean>("SwordOnly", true));
    public Setting<Float> raytrace = this.register(new Setting<Float>("Raytrace", Float.valueOf(3.0f), Float.valueOf(0.1f), Float.valueOf(3.0f), "Wall Range."));
    public Setting<Boolean> players = this.register(new Setting<Boolean>("Players", true));
    public Setting<Boolean> mobs = this.register(new Setting<Boolean>("Mobs", false));
    public Setting<Boolean> animals = this.register(new Setting<Boolean>("Animals", false));
    public Setting<Boolean> vehicles = this.register(new Setting<Boolean>("Entities", false));
    public Setting<Boolean> projectiles = this.register(new Setting<Boolean>("Projectiles", false));
    public Setting<Boolean> tps = this.register(new Setting<Boolean>("TpsSync", true));
    public Setting<Boolean> silent = this.register(new Setting<Boolean>("Silent", false));
    public Setting<Boolean> packet = this.register(new Setting<Boolean>("Packet", false));

    public Killaura() {
        super("KillAura", "Attacks entities automatically.", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onTick() {
        if (!this.rotate.getValue().booleanValue()) {
            this.doKillaura();
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayerEvent(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0 && this.rotate.getValue().booleanValue()) {
            this.doKillaura();
        }
    }

    private void doKillaura() {
        int wait = 0;
        if (this.onlySharp.getValue().booleanValue() && !EntityUtil.holdingWeapon((EntityPlayer)Killaura.mc.player) && !this.silent.getValue().booleanValue()) {
            target = null;
            return;
        }
        int n = this.delay.getValue() == false ? 0 : (wait = (int)((float)DamageUtil.getCooldownByWeapon((EntityPlayer)Killaura.mc.player) * (this.tps.getValue() != false ? OyVey.serverManager.getTpsFactor() : 1.0f)));
        if (this.silent.getValue().booleanValue() && InventoryUtil.getItemHotbar(Items.DIAMOND_SWORD) != -1) {
            wait = 600;
        }
        if (!this.timer.passedMs(wait)) {
            return;
        }
        target = this.getTarget();
        if (target == null) {
            return;
        }
        if (this.rotate.getValue().booleanValue()) {
            OyVey.rotationManager.lookAtEntity(target);
        }
        if (this.silent.getValue().booleanValue() && InventoryUtil.getItemHotbar(Items.DIAMOND_SWORD) != -1) {
            int old = Killaura.mc.player.inventory.currentItem;
            int sword = InventoryUtil.getItemHotbar(Items.DIAMOND_SWORD);
            this.switchToSlot(sword);
            EntityUtil.attackEntity(target, (boolean)this.packet.getValue(), true);
            this.switchToSlot(old);
        } else {
            EntityUtil.attackEntity(target, (boolean)this.packet.getValue(), true);
        }
        this.timer.reset();
    }

    private Entity getTarget() {
        Entity closestEntity = null;
        for (Entity entity : Killaura.mc.world.loadedEntityList) {
            if (!(this.players.getValue() != false && entity instanceof EntityPlayer || this.mobs.getValue() != false && EntityUtil.isMobAggressive(entity) || this.animals.getValue() != false && EntityUtil.isPassive(entity) || this.vehicles.getValue() != false && EntityUtil.isVehicle(entity)) && (!this.projectiles.getValue().booleanValue() || !EntityUtil.isProjectile(entity)) || entity instanceof EntityLivingBase && EntityUtil.isntValid(entity, this.range.getValue().floatValue()) || !Killaura.mc.player.canEntityBeSeen(entity) && Killaura.mc.player.getDistanceSq(entity) > MathUtil.square(this.raytrace.getValue().floatValue())) continue;
            if (closestEntity == null) {
                closestEntity = entity;
                continue;
            }
            if (closestEntity.getDistance((Entity)Killaura.mc.player) <= entity.getDistance((Entity)Killaura.mc.player)) continue;
            closestEntity = entity;
        }
        return closestEntity;
    }

    private void switchToSlot(int slot) {
        Killaura.mc.player.inventory.currentItem = slot;
        Killaura.mc.playerController.updateController();
    }

    @Override
    public String getDisplayInfo() {
        if (target instanceof EntityPlayer) {
            return target.getName();
        }
        return null;
    }
}

