/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.util.EnumHand
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.abHack.features.modules.player;

import me.abHack.event.events.Packet;
import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Swing
extends Module {
    private final Setting<Hand> hand = this.register(new Setting<Hand>("Hand", Hand.OFFHAND));

    public Swing() {
        super("Swing", "Changes the hand you swing with", Module.Category.PLAYER, false, false, false);
    }

    @Override
    public void onUpdate() {
        if (Swing.mc.world == null) {
            return;
        }
        if (this.hand.getValue() == Hand.OFFHAND) {
            Swing.mc.player.swingingHand = EnumHand.OFF_HAND;
        }
        if (this.hand.getValue() == Hand.MAINHAND) {
            Swing.mc.player.swingingHand = EnumHand.MAIN_HAND;
        }
    }

    @SubscribeEvent
    public void onPacket(Packet event) {
        if (Swing.nullCheck() || event.getType() == Packet.Type.INCOMING) {
            return;
        }
        if (event.getPacket() instanceof CPacketAnimation) {
            event.setCanceled(true);
        }
    }

    public static enum Hand {
        OFFHAND,
        MAINHAND;

    }
}

