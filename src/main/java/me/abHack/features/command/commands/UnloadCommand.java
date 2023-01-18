/*
 * Decompiled with CFR 0.152.
 */
package me.abHack.features.command.commands;

import me.abHack.OyVey;
import me.abHack.features.command.Command;

public class UnloadCommand
extends Command {
    public UnloadCommand() {
        super("unload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        OyVey.unload(true);
    }
}

