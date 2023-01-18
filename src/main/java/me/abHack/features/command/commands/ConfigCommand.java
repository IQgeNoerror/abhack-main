package me.abHack.features.command.commands;

import java.util.Iterator;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.abHack.OyVey;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;
import java.io.File;
import me.abHack.features.command.Command;

public class ConfigCommand extends Command
{
    public ConfigCommand() {
        super("config", new String[] { "<save/load/list>" });
    }

    @Override
    public void execute(final String[] commands) {
        if (commands.length == 1) {
            Command.sendMessage(".config <save/load/list>");
            return;
        }
        if (commands.length == 2) {
            if ("list".equals(commands[0])) {
                String configs = "Configs: ";
                File file = new File("ab-Hack/");
                List<File> directories = Arrays.stream(file.listFiles()).filter(File::isDirectory).filter(f -> !f.getName().equals("util")).collect(Collectors.toList());
                StringBuilder builder = new StringBuilder(configs);
                for (File file1 : directories)
                    builder.append(file1.getName() + ", ");
                configs = builder.toString();
                Command.sendMessage(configs);
            }
            else {
                Command.sendMessage("Not a valid command... Possible usage: <list>");
            }
        }
        if (commands.length >= 3) {
            final String s = commands[0];
            switch (s) {
                case "save": {
                    OyVey.configManager.saveConfig(commands[1]);
                    Command.sendMessage(ChatFormatting.GREEN + "Config '" + commands[1] + "' has been saved.");
                }
                case "load": {
                    if (OyVey.configManager.configExists(commands[1])) {
                        OyVey.configManager.loadConfig(commands[1]);
                        Command.sendMessage(ChatFormatting.GREEN + "Config '" + commands[1] + "' has been loaded.");
                    }
                    else {
                        Command.sendMessage(ChatFormatting.RED + "Config '" + commands[1] + "' does not exist.");
                    }
                }
            }
        }
    }
}