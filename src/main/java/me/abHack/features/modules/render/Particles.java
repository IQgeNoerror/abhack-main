/*
 * Decompiled with CFR 0.152.
 */
package me.abHack.features.modules.render;

import me.abHack.features.modules.Module;

public class Particles
extends Module {
    public Particles() {
        super("Particles", "Display Particle.", Module.Category.RENDER, false, false, false);
    }

    @Override
    public void onUpdate() {
        int x = (int)(Math.random() * 5.0 + 0.0);
        int y = (int)(Math.random() * 3.0 + 1.0);
        int z = (int)(Math.random() * 5.0 + -1.0);
        int particleId = (int)(Math.random() * 47.0 + 1.0);
        if (particleId != 1 && particleId != 2 && particleId != 41) {
            Particles.mc.effectRenderer.spawnEffectParticle(particleId, Particles.mc.player.posX + 1.5 + (double)(-x), Particles.mc.player.posY + (double)y, Particles.mc.player.posZ + 1.5 + (double)(-z), 0.0, 0.5, 0.0, new int[]{10});
        }
    }
}

