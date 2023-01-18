/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.entity.player.EntityPlayer
 */
package me.abHack.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.HashMap;
import me.abHack.features.command.Command;
import me.abHack.features.modules.Module;
import net.minecraft.entity.player.EntityPlayer;

public class PopCounter
extends Module {
    public static HashMap<String, Integer> TotemPopContainer = new HashMap();
    private static PopCounter INSTANCE = new PopCounter();

    public PopCounter() {
        super("PopCounter", "Counts other players totem pops.", Module.Category.MISC, true, false, false);
        this.setInstance();
    }

    public static PopCounter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PopCounter();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        TotemPopContainer.clear();
    }

    public void onDeath(EntityPlayer player) {
        if (TotemPopContainer.containsKey(player.getName())) {
            int l_Count = TotemPopContainer.get(player.getName());
            TotemPopContainer.remove(player.getName());
            if (l_Count == 1) {
                if (PopCounter.mc.player.equals((Object)player)) {
                    Command.sendMessage(ChatFormatting.BLUE + "You died after popping " + ChatFormatting.RED + l_Count + ChatFormatting.RED + " Totem!");
                } else {
                    Command.sendMessage(ChatFormatting.RED + player.getName() + " died after popping " + ChatFormatting.GREEN + l_Count + ChatFormatting.RED + " Totem!");
                }
            } else if (PopCounter.mc.player.equals((Object)player)) {
                Command.sendMessage(ChatFormatting.BLUE + "You died after popping " + ChatFormatting.RED + l_Count + ChatFormatting.RED + " Totems!");
            } else {
                Command.sendMessage(ChatFormatting.RED + player.getName() + " died after popping " + ChatFormatting.GREEN + l_Count + ChatFormatting.RED + " Totems!");
            }
        }
    }

    public void onTotemPop(EntityPlayer player) {
        if (PopCounter.fullNullCheck()) {
            return;
        }
        int l_Count = 1;
        if (TotemPopContainer.containsKey(player.getName())) {
            l_Count = TotemPopContainer.get(player.getName());
            TotemPopContainer.put(player.getName(), ++l_Count);
        } else {
            TotemPopContainer.put(player.getName(), l_Count);
        }
        if (l_Count == 1) {
            if (PopCounter.mc.player.equals((Object)player)) {
                Command.sendMessage(ChatFormatting.BLUE + "You popped " + ChatFormatting.RED + l_Count + ChatFormatting.RED + " Totem.");
            } else {
                Command.sendMessage(ChatFormatting.RED + player.getName() + " popped " + ChatFormatting.GREEN + l_Count + ChatFormatting.RED + " Totem.");
            }
        } else if (PopCounter.mc.player.equals((Object)player)) {
            Command.sendMessage(ChatFormatting.BLUE + "You popped " + ChatFormatting.RED + l_Count + ChatFormatting.RED + " Totems.");
        } else {
            Command.sendMessage(ChatFormatting.RED + player.getName() + " popped " + ChatFormatting.GREEN + l_Count + ChatFormatting.RED + " Totems.");
        }
    }
}

