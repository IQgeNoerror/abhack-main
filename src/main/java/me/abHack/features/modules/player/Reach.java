/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 */
package me.abHack.features.modules.player;

import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import net.minecraft.entity.player.EntityPlayer;

public class Reach
extends Module {
    private final Setting<Integer> Reach = this.register(new Setting<Integer>("Reach", 6, 5, 10));

    public Reach() {
        super("Reach", "reach", Module.Category.PLAYER, true, false, false);
    }

    @Override
    public void onUpdate() {
        me.abHack.features.modules.player.Reach.mc.player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).setBaseValue((double)this.Reach.getValue().intValue());
    }

    @Override
    public void onDisable() {
        me.abHack.features.modules.player.Reach.mc.player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).setBaseValue(5.0);
    }
}

