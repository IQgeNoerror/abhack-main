/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 */
package me.abHack.manager;

import net.minecraft.entity.EntityLivingBase;

public class TargetManager {
    private EntityLivingBase currentTarget = null;

    public void updateTarget(EntityLivingBase targetIn) {
        this.currentTarget = targetIn;
    }

    public EntityLivingBase getTarget() {
        return this.currentTarget;
    }
}

