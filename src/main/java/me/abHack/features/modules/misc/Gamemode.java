/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.GameType
 */
package me.abHack.features.modules.misc;

import me.abHack.features.modules.Module;
import net.minecraft.world.GameType;

public class Gamemode
extends Module {
    public Gamemode() {
        super("Gamemode", "fake gamemode", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onTick() {
        if (Gamemode.mc.player == null) {
            return;
        }
        Gamemode.mc.playerController.setGameType(GameType.CREATIVE);
    }

    @Override
    public void onDisable() {
        if (Gamemode.mc.player == null) {
            return;
        }
        Gamemode.mc.playerController.setGameType(GameType.SURVIVAL);
    }
}

