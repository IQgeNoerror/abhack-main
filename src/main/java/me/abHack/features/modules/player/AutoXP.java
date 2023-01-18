/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.NonNullList
 */
package me.abHack.features.modules.player;

import me.abHack.features.modules.Module;
import me.abHack.features.setting.Bind;
import me.abHack.features.setting.Setting;
import me.abHack.util.InventoryUtil;
import me.abHack.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;

public class AutoXP
extends Module {
    public static AutoXP INSTANCE;
    public final Setting<Bind> bind;
    private final Timer timer = new Timer();
    private final Setting<Integer> delay = this.register(new Setting<Integer>("XP Delay", 4, 0, 4));
    private final Setting<Integer> minDamage = this.register(new Setting<Integer>("Min Damage", 50, 0, 100));
    private final Setting<Integer> maxHeal = this.register(new Setting<Integer>("Repair To", 90, 0, 100));
    private final Setting<Boolean> sneakOnly = this.register(new Setting<Boolean>("Sneak Only", false));
    private final Setting<Boolean> predict = this.register(new Setting<Boolean>("Predict", false));
    char toMend = '\u0000';

    public AutoXP() {
        super("AutoXP", "AutoXP", Module.Category.PLAYER, true, false, false);
        this.bind = this.register(new Setting<Bind>("PacketBind", new Bind(-1)));
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (AutoXP.fullNullCheck()) {
            return;
        }
        if (this.bind.getValue().isDown()) {
            this.mendArmor();
        }
        int sumOfDamage = 0;
        NonNullList nonNullList = AutoXP.mc.player.inventory.armorInventory;
        for (int i = 0; i < nonNullList.size(); ++i) {
            ItemStack itemStack = (ItemStack)nonNullList.get(i);
            if (itemStack.isEmpty) continue;
            float damageOnArmor = itemStack.getMaxDamage() - itemStack.getItemDamage();
            float damagePercent = 100.0f - 100.0f * (1.0f - damageOnArmor / (float)itemStack.getMaxDamage());
            if (damagePercent <= (float)this.maxHeal.getValue().intValue()) {
                if (damagePercent <= (float)this.minDamage.getValue().intValue()) {
                    this.toMend = (char)(this.toMend | 1 << i);
                }
                if (!this.predict.getValue().booleanValue()) continue;
                sumOfDamage = (int)((float)sumOfDamage + (float)(itemStack.getMaxDamage() * this.maxHeal.getValue()) / 100.0f - (float)(itemStack.getMaxDamage() - itemStack.getItemDamage()));
                continue;
            }
            this.toMend = (char)(this.toMend & ~(1 << i));
        }
        if (this.toMend > '\u0000' && this.timer.passedMs(this.delay.getValue() * 45)) {
            this.timer.reset();
            if (this.predict.getValue().booleanValue()) {
                int totalXp = AutoXP.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityXPOrb).filter(entity -> entity.getDistanceSq((Entity)AutoXP.mc.player) <= 1.0).mapToInt(entity -> ((EntityXPOrb)entity).xpValue).sum();
                if (totalXp * 2 < sumOfDamage) {
                    this.mendArmor();
                }
            } else if (this.sneakOnly.getValue().booleanValue() && AutoXP.mc.player.isSneaking()) {
                this.mendArmor();
            }
        }
    }

    private void mendArmor() {
        int a = InventoryUtil.getItemHotbar(Items.EXPERIENCE_BOTTLE);
        int b = AutoXP.mc.player.inventory.currentItem;
        if (a == -1) {
            return;
        }
        AutoXP.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(AutoXP.mc.player.rotationYaw, 90.0f, true));
        AutoXP.mc.player.inventory.currentItem = a;
        AutoXP.mc.playerController.updateController();
        AutoXP.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        AutoXP.mc.player.inventory.currentItem = b;
        AutoXP.mc.playerController.updateController();
    }
}

