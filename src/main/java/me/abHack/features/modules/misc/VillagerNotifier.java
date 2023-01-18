/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.passive.EntityVillager
 */
package me.abHack.features.modules.misc;

import java.util.HashSet;
import java.util.Set;
import me.abHack.features.command.Command;
import me.abHack.features.modules.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;

public class VillagerNotifier
extends Module {
    private static VillagerNotifier instance;
    private final Set<Entity> entities = new HashSet<Entity>();

    public VillagerNotifier() {
        super("VillagerNotifier", "Notifies you when a Villager is discovered", Module.Category.MISC, true, false, false);
        instance = this;
    }

    @Override
    public void onEnable() {
        this.entities.clear();
    }

    @Override
    public void onUpdate() {
        for (Entity entity : VillagerNotifier.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityVillager) || this.entities.contains(entity)) continue;
            Command.sendMessage("Villager Detected at: X:" + (int)entity.posX + " X: " + (int)entity.posY + " Z:" + (int)entity.posZ);
            this.entities.add(entity);
        }
    }
}

