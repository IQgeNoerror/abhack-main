/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemShulkerBox
 *  net.minecraft.item.ItemStack
 */
package me.abHack.features.command.commands;

import java.util.Map;
import me.abHack.features.command.Command;
import me.abHack.features.modules.misc.ShulkerViewer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;

public class PeekCommand
extends Command {
    public PeekCommand() {
        super("peek", new String[]{"<player>"});
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 1) {
            ItemStack stack = PeekCommand.mc.player.getHeldItemMainhand();
            if (stack != null && stack.getItem() instanceof ItemShulkerBox) {
                ShulkerViewer.displayInv(stack, null);
            } else {
                Command.sendMessage("\u00a7cYou need to hold a Shulker in your mainhand.");
                return;
            }
        }
        if (commands.length > 1) {
            if (ShulkerViewer.getInstance().isOn()) {
                for (Map.Entry<EntityPlayer, ItemStack> entry : ShulkerViewer.getInstance().spiedPlayers.entrySet()) {
                    if (!entry.getKey().getName().equalsIgnoreCase(commands[0])) continue;
                    ItemStack stack = entry.getValue();
                    ShulkerViewer.displayInv(stack, entry.getKey().getName());
                    break;
                }
            } else {
                Command.sendMessage("\u00a7cYou need to turn on ShulkerViewer Module");
            }
        }
    }
}

