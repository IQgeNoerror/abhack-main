/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemExpBottle
 */
package me.abHack.features.modules.player;

import me.abHack.features.modules.Module;
import me.abHack.util.InventoryUtil;
import net.minecraft.item.ItemExpBottle;

public class FastPlace
extends Module {
    public FastPlace() {
        super("FastPlace", "Fast Use.", Module.Category.PLAYER, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (FastPlace.fullNullCheck()) {
            return;
        }
        if (InventoryUtil.holdingItem(ItemExpBottle.class)) {
            FastPlace.mc.rightClickDelayTimer = 0;
        }
    }
}

