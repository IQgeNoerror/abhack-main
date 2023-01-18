/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.item.ItemBow
 *  net.minecraft.item.ItemEgg
 *  net.minecraft.item.ItemEnderPearl
 *  net.minecraft.item.ItemSnowball
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.util.EnumHand
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.abHack.features.modules.misc;

import me.abHack.event.events.PacketEvent;
import me.abHack.features.command.Command;
import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BowGod
extends Module {
    private final Setting<String> spoofs = this.register(new Setting<String>("Spoofs", "10"));
    public Setting<Boolean> Bows = this.register(new Setting<Boolean>("Bows", true));
    public Setting<Boolean> pearls = this.register(new Setting<Boolean>("Pearls", true));
    public Setting<Boolean> eggs = this.register(new Setting<Boolean>("Eggs", true));
    public Setting<Boolean> snowballs = this.register(new Setting<Boolean>("SnowBallz", true));
    public Setting<Integer> Timeout = this.register(new Setting<Integer>("Timeout", 500, 0, 2000));
    public Setting<Boolean> bypass = this.register(new Setting<Boolean>("Bypass", false));
    public Setting<Boolean> debug = this.register(new Setting<Boolean>("Debug", false));
    private boolean shooting;
    private long lastShootTime;

    public BowGod() {
        super("BowGod", "super bow", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onEnable() {
        if (this.isEnabled()) {
            this.shooting = false;
            this.lastShootTime = System.currentTimeMillis();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        ItemStack handStack;
        CPacketPlayerTryUseItem packet2;
        if (event.getStage() != 0) {
            return;
        }
        if (event.getPacket() instanceof CPacketPlayerDigging) {
            ItemStack handStack2;
            CPacketPlayerDigging packet = (CPacketPlayerDigging)event.getPacket();
            if (packet.getAction() == CPacketPlayerDigging.Action.RELEASE_USE_ITEM && !(handStack2 = BowGod.mc.player.getHeldItem(EnumHand.MAIN_HAND)).isEmpty() && handStack2.getItem() != null && handStack2.getItem() instanceof ItemBow && this.Bows.getValue().booleanValue()) {
                this.doSpoofs();
                if (this.debug.getValue().booleanValue()) {
                    Command.sendMessage("trying to spoof");
                }
            }
        } else if (event.getPacket() instanceof CPacketPlayerTryUseItem && (packet2 = (CPacketPlayerTryUseItem)event.getPacket()).getHand() == EnumHand.MAIN_HAND && !(handStack = BowGod.mc.player.getHeldItem(EnumHand.MAIN_HAND)).isEmpty() && handStack.getItem() != null) {
            if (handStack.getItem() instanceof ItemEgg && this.eggs.getValue().booleanValue()) {
                this.doSpoofs();
            } else if (handStack.getItem() instanceof ItemEnderPearl && this.pearls.getValue().booleanValue()) {
                this.doSpoofs();
            } else if (handStack.getItem() instanceof ItemSnowball && this.snowballs.getValue().booleanValue()) {
                this.doSpoofs();
            }
        }
    }

    private void doSpoofs() {
        if (System.currentTimeMillis() - this.lastShootTime >= (long)this.Timeout.getValue().intValue()) {
            this.shooting = true;
            this.lastShootTime = System.currentTimeMillis();
            BowGod.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BowGod.mc.player, CPacketEntityAction.Action.START_SPRINTING));
            for (int index = 0; index < Integer.valueOf(this.spoofs.getValue()); ++index) {
                if (this.bypass.getValue().booleanValue()) {
                    BowGod.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(BowGod.mc.player.posX, BowGod.mc.player.posY + 1.0E-10, BowGod.mc.player.posZ, false));
                    BowGod.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(BowGod.mc.player.posX, BowGod.mc.player.posY - 1.0E-10, BowGod.mc.player.posZ, true));
                    continue;
                }
                BowGod.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(BowGod.mc.player.posX, BowGod.mc.player.posY - 1.0E-10, BowGod.mc.player.posZ, true));
                BowGod.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(BowGod.mc.player.posX, BowGod.mc.player.posY + 1.0E-10, BowGod.mc.player.posZ, false));
            }
            if (this.debug.getValue().booleanValue()) {
                Command.sendMessage("Spoofed");
            }
            this.shooting = false;
        }
    }
}

