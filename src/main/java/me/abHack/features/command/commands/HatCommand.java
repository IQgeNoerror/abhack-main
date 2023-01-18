/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 */
package me.abHack.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.abHack.features.command.Command;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;

public class HatCommand
extends Command {
    public HatCommand() {
        super("Hat", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 1) {
            Command.sendMessage(ChatFormatting.DARK_RED + ".Hat <item> <count> <data> <nbt>");
            return;
        }
        if (commands.length == 2) {
            HatCommand.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("/replaceitem entity @p slot.armor.head " + commands[0]));
            Command.sendMessage("New Hat.");
        }
        if (commands.length == 3) {
            HatCommand.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("/replaceitem entity @p slot.armor.head " + commands[0] + " " + commands[1]));
            Command.sendMessage("New Hat.");
        }
        if (commands.length == 4) {
            HatCommand.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("/replaceitem entity @p slot.armor.head " + commands[0] + " " + commands[1] + " " + commands[2]));
            Command.sendMessage("New Hat.");
        }
        if (commands.length == 5) {
            HatCommand.mc.player.connection.sendPacket((Packet)new CPacketChatMessage("/replaceitem entity @p slot.armor.head " + commands[0] + " " + commands[1] + " " + commands[2] + " " + commands[3]));
            Command.sendMessage("New Hat.");
        }
    }
}

