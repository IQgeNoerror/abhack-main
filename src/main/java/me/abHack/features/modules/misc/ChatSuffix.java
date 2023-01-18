/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.abHack.features.modules.misc;

import me.abHack.event.events.PacketEvent;
import me.abHack.features.modules.Module;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatSuffix
extends Module {
    public ChatSuffix() {
        super("ChatSuffix", "suffix", Module.Category.MISC, true, false, false);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        CPacketChatMessage packet;
        String message;
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketChatMessage && !(message = (packet = (CPacketChatMessage)event.getPacket()).getMessage()).startsWith("/")) {
            String abHackChat = message + " | \u1d00\u0299\u029c\u1d00\u1d04\u1d0b";
            if (abHackChat.length() >= 256) {
                abHackChat = abHackChat.substring(0, 256);
            }
            packet.message = abHackChat;
        }
    }
}

