/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package me.abHack.features.command.commands;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.text.DecimalFormat;
import me.abHack.features.command.Command;
import net.minecraft.client.Minecraft;

public class SpawnCommand
extends Command {
    public SpawnCommand() {
        super("spawnCoord", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        DecimalFormat format = new DecimalFormat("#");
        StringSelection contents = new StringSelection(Minecraft.getMinecraft().player.getBedLocation() + " !");
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(contents, null);
        Command.sendMessage("Saved Spawn Coords to Clipboard, use CTRL + V to paste.");
    }
}

