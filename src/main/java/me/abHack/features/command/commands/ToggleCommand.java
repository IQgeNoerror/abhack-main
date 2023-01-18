/*
 * Decompiled with CFR 0.152.
 */
package me.abHack.features.command.commands;

import me.abHack.OyVey;
import me.abHack.features.command.Command;
import me.abHack.features.modules.Module;

public class ToggleCommand
extends Command {
    public ToggleCommand() {
        super("toggle", new String[]{"<toggle>", "<module>"});
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 2) {
            String name = commands[0].replaceAll("_", " ");
            Module module = OyVey.moduleManager.getModuleByName(name);
            if (module != null) {
                module.toggle();
            } else {
                Command.sendMessage("Unable to find a module with that name!");
            }
        } else {
            Command.sendMessage("Please provide a valid module name!");
        }
    }
}

