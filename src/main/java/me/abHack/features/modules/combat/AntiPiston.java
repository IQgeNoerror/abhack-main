/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockPistonBase
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 */
package me.abHack.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.abHack.features.command.Command;
import me.abHack.features.modules.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class AntiPiston
extends Module {
    public AntiPiston() {
        super("AntiPiston", "Anti PistonCrystal", Module.Category.COMBAT, true, false, false);
    }

    public static Block getBlock(double x, double y, double z) {
        return AntiPiston.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
    }

    public static void breakCrystal(Entity crystal) {
        AntiPiston.mc.playerController.attackEntity((EntityPlayer)AntiPiston.mc.player, crystal);
        AntiPiston.mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    @Override
    public void onUpdate() {
        this.blockPiston();
    }

    private void blockPiston() {
        for (Entity t : AntiPiston.mc.world.loadedEntityList) {
            if (!(t instanceof EntityEnderCrystal) || !(t.posX >= AntiPiston.mc.player.posX - 1.5) || !(t.posX <= AntiPiston.mc.player.posX + 1.5) || !(t.posZ >= AntiPiston.mc.player.posZ - 1.5) || !(t.posZ <= AntiPiston.mc.player.posZ + 1.5)) continue;
            for (int i = -2; i < 3; ++i) {
                for (int j = -2; j < 3; ++j) {
                    if (!(AntiPiston.getBlock(t.posX + (double)i, t.posY, t.posZ + (double)j) instanceof BlockPistonBase)) continue;
                    this.breakCrystalPiston(t);
                    Command.sendMessage(ChatFormatting.GREEN + "PistonCrystal detected... Destroyed crystal!");
                }
            }
        }
    }

    private void breakCrystalPiston(Entity crystal) {
        AntiPiston.breakCrystal(crystal);
    }
}

