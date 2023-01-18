/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.Unpooled
 *  net.minecraft.client.Minecraft
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.PacketBuffer
 */
package me.abHack.features.command.commands;

import io.netty.buffer.Unpooled;
import me.abHack.features.command.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class ItemSizeCommand
extends Command {
    public ItemSizeCommand() {
        super("size", new String[0]);
    }

    public static int getItemSize(ItemStack stack) {
        PacketBuffer buff = new PacketBuffer(Unpooled.buffer());
        buff.writeItemStack(stack);
        int size = buff.writerIndex();
        buff.release();
        return size;
    }

    @Override
    public void execute(String[] commands) {
        ItemStack stack = Minecraft.getMinecraft().player.getHeldItemMainhand();
        if (stack.isEmpty()) {
            Command.sendMessage("You are not holding any item");
        }
        Command.sendMessage("Item weights " + ItemSizeCommand.getItemSize(stack) + " bytes");
    }
}

