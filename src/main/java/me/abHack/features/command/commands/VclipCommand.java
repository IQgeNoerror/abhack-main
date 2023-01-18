/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.abHack.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.abHack.features.command.Command;

public class VclipCommand
extends Command {
    public VclipCommand() {
        super("vclip", new String[]{"<y>"});
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 1) {
            Command.sendMessage(ChatFormatting.DARK_RED + ".Vclip <Highly>");
            return;
        }
        int y = Integer.parseInt(commands[0]);
        Command.sendMessage("Vclip to " + ChatFormatting.GRAY + y);
        VclipCommand.mc.player.setPosition(VclipCommand.mc.player.posX, VclipCommand.mc.player.posY + (double)y, VclipCommand.mc.player.posZ);
    }
}

