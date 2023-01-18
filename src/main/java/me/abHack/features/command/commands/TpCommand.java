/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 */
package me.abHack.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.abHack.features.command.Command;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;

public class TpCommand
extends Command {
    public TpCommand() {
        super("tp", new String[]{"<x> <y> <z>"});
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 1 || commands.length == 2 || commands.length > 4) {
            Command.sendMessage(ChatFormatting.DARK_RED + ".tp <x> <y> <z>");
            return;
        }
        if (commands.length == 4) {
            int x = Integer.parseInt(commands[0]);
            int y = Integer.parseInt(commands[1]);
            int z = Integer.parseInt(commands[2]);
            int sum = Math.abs(x) > Math.abs(y) ? Math.abs(x) : Math.abs(y);
            sum = Math.abs(z) > sum ? Math.abs(z) : sum;
            for (int i = 0; i < sum; i += 10) {
                TpCommand.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(TpCommand.mc.player.posX, TpCommand.mc.player.posY + 0.001, TpCommand.mc.player.posZ, true));
            }
            TpCommand.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(TpCommand.mc.player.posX + (double)x, TpCommand.mc.player.posY + (double)y, TpCommand.mc.player.posZ + (double)z, false));
        }
    }
}

