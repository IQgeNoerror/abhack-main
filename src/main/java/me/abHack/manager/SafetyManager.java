/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 */
package me.abHack.manager;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import me.abHack.features.Feature;
import me.abHack.features.modules.combat.AutoCrystal;
import me.abHack.util.DamageUtil;
import me.abHack.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;

public class SafetyManager extends Feature implements Runnable
{
    private final Timer syncTimer;
    private final AtomicBoolean SAFE;
    private ScheduledExecutorService service;

    public SafetyManager() {
        this.syncTimer = new Timer();
        this.SAFE = new AtomicBoolean(false);
    }

    @Override
    public void run() {
        if (AutoCrystal.getInstance().isOff() || AutoCrystal.getInstance().threadMode.getValue() == AutoCrystal.ThreadMode.NONE) {
            this.doSafetyCheck();
        }
    }

    public void doSafetyCheck() {
        if (!Feature.fullNullCheck()) {
            boolean safe = true;
            final ArrayList<Entity> crystals = new ArrayList<Entity>(SafetyManager.mc.world.loadedEntityList);
            for (final Entity crystal : crystals) {
                if (crystal instanceof EntityEnderCrystal) {
                    if (DamageUtil.calculateDamage(crystal, (Entity)SafetyManager.mc.player) <= 4.0) {
                        continue;
                    }
                    safe = false;
                    break;
                }
            }
            this.SAFE.set(safe);
        }
    }

    public void onUpdate() {
        this.run();
    }

    public String getSafetyString() {
        if (this.SAFE.get()) {
            return "§aSecure";
        }
        return "§cUnsafe";
    }

    public boolean isSafe() {
        return this.SAFE.get();
    }

    public ScheduledExecutorService getService() {
        final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        return service;
    }
}
