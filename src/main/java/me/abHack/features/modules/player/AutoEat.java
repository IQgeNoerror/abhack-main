/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemFood
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.util.EnumHand
 *  net.minecraft.world.World
 */
package me.abHack.features.modules.player;

import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class AutoEat
extends Module {
    public final Setting<Float> health = this.register(new Setting<Float>("Health", Float.valueOf(10.0f), Float.valueOf(0.0f), Float.valueOf(36.0f)));
    public final Setting<Float> hunger = this.register(new Setting<Float>("Hunger", Float.valueOf(15.0f), Float.valueOf(0.0f), Float.valueOf(20.0f)));
    public final Setting<Boolean> autoSwitch = this.register(new Setting<Boolean>("AutoSwitch", true));
    public final Setting<Boolean> preferGaps = this.register(new Setting<Boolean>("PreferGaps", false));
    int originalSlot = -1;
    boolean firstSwap = true;
    boolean resetKeyBind = false;

    public AutoEat() {
        super("AutoEat", "Auto eat", Module.Category.PLAYER, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (AutoEat.mc.player == null || AutoEat.mc.world == null) {
            return;
        }
        if (AutoEat.mc.player.isCreative()) {
            return;
        }
        if (AutoEat.mc.player.getHealth() + AutoEat.mc.player.getAbsorptionAmount() <= this.health.getValue().floatValue() || (float)AutoEat.mc.player.getFoodStats().getFoodLevel() <= this.hunger.getValue().floatValue()) {
            if (this.autoSwitch.getValue().booleanValue()) {
                int foodSlot = this.findFoodSlot();
                if (this.firstSwap) {
                    this.originalSlot = AutoEat.mc.player.inventory.currentItem;
                    this.firstSwap = false;
                }
                if (foodSlot != -1) {
                    AutoEat.mc.player.inventory.currentItem = foodSlot;
                    AutoEat.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(foodSlot));
                }
            }
            if (AutoEat.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemFood) {
                if (AutoEat.mc.currentScreen == null || AutoEat.mc.currentScreen instanceof GuiInventory) {
                    KeyBinding.setKeyBindState((int)AutoEat.mc.gameSettings.keyBindUseItem.getKeyCode(), (boolean)true);
                    this.resetKeyBind = true;
                } else {
                    AutoEat.mc.playerController.processRightClick((EntityPlayer)AutoEat.mc.player, (World)AutoEat.mc.world, EnumHand.MAIN_HAND);
                }
            } else if (AutoEat.mc.currentScreen == null || AutoEat.mc.currentScreen instanceof GuiInventory) {
                KeyBinding.setKeyBindState((int)AutoEat.mc.gameSettings.keyBindUseItem.getKeyCode(), (boolean)GameSettings.isKeyDown((KeyBinding)AutoEat.mc.gameSettings.keyBindUseItem));
            }
        } else {
            if (this.resetKeyBind) {
                KeyBinding.setKeyBindState((int)AutoEat.mc.gameSettings.keyBindUseItem.getKeyCode(), (boolean)GameSettings.isKeyDown((KeyBinding)AutoEat.mc.gameSettings.keyBindUseItem));
                this.resetKeyBind = false;
            }
            if (this.originalSlot != -1) {
                AutoEat.mc.player.inventory.currentItem = this.originalSlot;
                AutoEat.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.originalSlot));
                this.originalSlot = -1;
                this.firstSwap = true;
            }
        }
    }

    private int findFoodSlot() {
        int foodSlot = -1;
        float bestHealAmount = 0.0f;
        if (foodSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                ItemStack item = AutoEat.mc.player.inventory.getStackInSlot(l);
                if (!(item.getItem() instanceof ItemFood)) continue;
                if (this.preferGaps.getValue().booleanValue() && item.getItem() == Items.GOLDEN_APPLE) {
                    foodSlot = l;
                    break;
                }
                float healAmount = ((ItemFood)item.getItem()).getHealAmount(item);
                if (!(healAmount > bestHealAmount)) continue;
                bestHealAmount = healAmount;
                foodSlot = l;
            }
        }
        return foodSlot;
    }
}

