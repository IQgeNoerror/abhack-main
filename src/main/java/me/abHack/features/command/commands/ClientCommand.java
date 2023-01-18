/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  org.lwjgl.opengl.Display
 */
package me.abHack.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.abHack.features.command.Command;
import org.lwjgl.opengl.Display;

public class ClientCommand
extends Command {
    public ClientCommand() {
        super("Client", new String[]{"<name>"});
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 1 || commands[0] == null) {
            Display.setTitle((String)"ab-Hack 0.0.1");
            Command.sendMessage(ChatFormatting.DARK_RED + ".Client <Client Name>");
            return;
        }
        String name = "ab-Hack 0.0.1";
        if (commands[0] != null) {
            name = commands[0];
        }
        if (commands[0] != null && commands[1] != null) {
            name = commands[0] + " " + commands[1];
        }
        Display.setTitle((String)name);
        Command.sendMessage("new Client Name.");
    }
}

