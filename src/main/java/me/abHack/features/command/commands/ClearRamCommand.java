/*
 * Decompiled with CFR 0.152.
 */
package me.abHack.features.command.commands;

import me.abHack.features.command.Command;

public class ClearRamCommand
extends Command {
    public ClearRamCommand() {
        super("clearram");
    }

    @Override
    public void execute(String[] commands) {
        System.gc();
        Command.sendMessage("Finished clearing the ram.");
    }
}

