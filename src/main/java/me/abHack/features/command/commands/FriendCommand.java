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
import me.abHack.manager.FriendManager;

public class FriendCommand
extends Command {
    public FriendCommand() {
        super("friend", new String[]{"<add/del/reset/list>", "<name>"});
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 1) {
            FriendCommand.sendMessage(".friend <add/del/reset/list>");
            return;
        }
        if (commands.length == 2) {
            switch (commands[0]) {
                case "reset": {
                    OyVey.friendManager.onLoad();
                    FriendCommand.sendMessage("Friends got reset.");
                    return;
                }
                case "add": {
                    FriendCommand.sendMessage(".friend add <player>");
                    return;
                }
                case "del": {
                    FriendCommand.sendMessage(".friend del <player>");
                    return;
                }
                case "list": {
                    if (OyVey.friendManager.getFriends().isEmpty()) {
                        FriendCommand.sendMessage("No Friends.");
                        break;
                    }
                    String f = "Friends: ";
                    for (FriendManager.Friend friend : OyVey.friendManager.getFriends()) {
                        try {
                            f = f + friend.getUsername() + ", ";
                        }
                        catch (Exception exception) {}
                    }
                    FriendCommand.sendMessage(f);
                }
            }
            return;
        }
        if (commands.length >= 3) {
            switch (commands[0]) {
                case "add": {
                    OyVey.friendManager.addFriend(commands[1]);
                    FriendCommand.sendMessage(ChatFormatting.GREEN + commands[1] + " has been friended");
                    return;
                }
                case "del": {
                    OyVey.friendManager.removeFriend(commands[1]);
                    FriendCommand.sendMessage(ChatFormatting.RED + commands[1] + " has been unfriended");
                    return;
                }
            }
        }
    }
}

