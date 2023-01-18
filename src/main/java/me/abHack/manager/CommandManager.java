/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.abHack.manager;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.LinkedList;
import me.abHack.features.Feature;
import me.abHack.features.command.Command;
import me.abHack.features.command.commands.BindCommand;
import me.abHack.features.command.commands.BookCommand;
import me.abHack.features.command.commands.ClearRamCommand;
import me.abHack.features.command.commands.ClientCommand;
import me.abHack.features.command.commands.ConfigCommand;
import me.abHack.features.command.commands.CoordsCommand;
import me.abHack.features.command.commands.FriendCommand;
import me.abHack.features.command.commands.HClipCommand;
import me.abHack.features.command.commands.HatCommand;
import me.abHack.features.command.commands.HelpCommand;
import me.abHack.features.command.commands.IpCommand;
import me.abHack.features.command.commands.ItemSizeCommand;
import me.abHack.features.command.commands.ModuleCommand;
import me.abHack.features.command.commands.PeekCommand;
import me.abHack.features.command.commands.PrefixCommand;
import me.abHack.features.command.commands.ReloadCommand;
import me.abHack.features.command.commands.ReloadSoundCommand;
import me.abHack.features.command.commands.SpawnCommand;
import me.abHack.features.command.commands.ToggleCommand;
import me.abHack.features.command.commands.TpCommand;
import me.abHack.features.command.commands.UnloadCommand;
import me.abHack.features.command.commands.VclipCommand;

public class CommandManager
extends Feature {
    private final ArrayList<Command> commands = new ArrayList();
    private String clientMessage = "<ab-Hack>";
    private String prefix = ".";

    public CommandManager() {
        super("Command");
        this.commands.add(new BindCommand());
        this.commands.add(new ModuleCommand());
        this.commands.add(new PrefixCommand());
        this.commands.add(new ConfigCommand());
        this.commands.add(new FriendCommand());
        this.commands.add(new HelpCommand());
        this.commands.add(new ReloadCommand());
        this.commands.add(new VclipCommand());
        this.commands.add(new BookCommand());
        this.commands.add(new ClearRamCommand());
        this.commands.add(new IpCommand());
        this.commands.add(new CoordsCommand());
        this.commands.add(new SpawnCommand());
        this.commands.add(new ItemSizeCommand());
        this.commands.add(new PeekCommand());
        this.commands.add(new UnloadCommand());
        this.commands.add(new ToggleCommand());
        this.commands.add(new ReloadSoundCommand());
        this.commands.add(new ClientCommand());
        this.commands.add(new HatCommand());
        this.commands.add(new TpCommand());
        this.commands.add(new HClipCommand());
    }

    public static String[] removeElement(String[] input, int indexToDelete) {
        LinkedList<String> result = new LinkedList<String>();
        for (int i = 0; i < input.length; ++i) {
            if (i == indexToDelete) continue;
            result.add(input[i]);
        }
        return result.toArray(input);
    }

    private static String strip(String str, String key) {
        if (str.startsWith(key) && str.endsWith(key)) {
            return str.substring(key.length(), str.length() - key.length());
        }
        return str;
    }

    public void executeCommand(String command) {
        String[] parts = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        String name = parts[0].substring(1);
        String[] args = CommandManager.removeElement(parts, 0);
        for (int i = 0; i < args.length; ++i) {
            if (args[i] == null) continue;
            args[i] = CommandManager.strip(args[i], "\"");
        }
        for (Command c : this.commands) {
            if (!c.getName().equalsIgnoreCase(name)) continue;
            c.execute(parts);
            return;
        }
        Command.sendMessage(ChatFormatting.GRAY + "Command not found, type 'help' for the commands list.");
    }

    public Command getCommandByName(String name) {
        for (Command command : this.commands) {
            if (!command.getName().equals(name)) continue;
            return command;
        }
        return null;
    }

    public ArrayList<Command> getCommands() {
        return this.commands;
    }

    public String getClientMessage() {
        return this.clientMessage;
    }

    public void setClientMessage(String clientMessage) {
        this.clientMessage = clientMessage;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}

