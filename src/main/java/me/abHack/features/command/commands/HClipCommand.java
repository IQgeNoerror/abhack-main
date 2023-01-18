/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.util.math.Vec3d
 */
package me.abHack.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.abHack.features.command.Command;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.Vec3d;

public class HClipCommand
extends Command {
    public HClipCommand() {
        super("hclip", new String[]{"<distance>"});
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 1) {
            Command.sendMessage(ChatFormatting.DARK_RED + ".Hclip <distance>");
            return;
        }
        double weight = Double.parseDouble(commands[0]);
        Vec3d direction = new Vec3d(Math.cos((HClipCommand.mc.player.rotationYaw + 90.0f) * ((float)Math.PI / 180)), 0.0, Math.sin((HClipCommand.mc.player.rotationYaw + 90.0f) * ((float)Math.PI / 180)));
        EntityPlayerSP target = HClipCommand.mc.player.isRiding() ? (EntityPlayerSP) HClipCommand.mc.player.getRidingEntity() : HClipCommand.mc.player;
        target.setPosition(target.posX + direction.x * weight, target.posY, target.posZ + direction.z * weight);
        Command.sendMessage("Hclip to " + weight);
    }
}

