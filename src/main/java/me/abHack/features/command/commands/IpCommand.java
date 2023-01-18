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
import me.abHack.features.command.Command;
import net.minecraft.client.Minecraft;

public final class IpCommand
extends Command {
    public IpCommand() {
        super("IP", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.getCurrentServerData() != null) {
            StringSelection contents = new StringSelection(mc.getCurrentServerData().serverIP);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(contents, null);
            Command.sendMessage("Copied IP to clipboard");
        } else {
            Command.sendMessage("Error, Join a server");
        }
    }
}

