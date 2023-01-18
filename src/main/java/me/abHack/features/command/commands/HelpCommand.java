/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.abHack.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.abHack.OyVey;
import me.abHack.features.command.Command;

public class HelpCommand
extends Command {
    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(String[] commands) {
        HelpCommand.sendMessage("Commands: ");
        for (Command command : OyVey.commandManager.getCommands()) {
            HelpCommand.sendMessage(ChatFormatting.GRAY + OyVey.commandManager.getPrefix() + command.getName());
        }
    }
}

