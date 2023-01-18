/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.network.play.server.SPacketSoundEffect
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.abHack.features.modules.misc;

import me.abHack.event.events.PacketEvent;
import me.abHack.features.modules.Module;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiCrash
extends Module {
    public AntiCrash() {
        super("AntiCrash", "anti offhand crash", Module.Category.MISC, true, false, false);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Receive event) {
        SPacketSoundEffect packet;
        if (event.getPacket() instanceof SPacketSoundEffect && (packet = (SPacketSoundEffect)event.getPacket()).getSound() == SoundEvents.ITEM_ARMOR_EQUIP_GENERIC) {
            event.setCanceled(true);
        }
    }
}

