/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.vecmath.Tuple2f
 *  javax.vecmath.Vector2f
 *  net.minecraft.client.gui.ScaledResolution
 */
package me.abHack.features.gui.particle;

import javax.vecmath.Tuple2f;
import javax.vecmath.Vector2f;
import me.abHack.features.gui.particle.Particle;
import me.abHack.features.modules.client.ClickGui;
import me.abHack.util.ColorUtil;
import me.abHack.util.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;

public final class ParticleSystem {
    private final int PARTS = 200;
    private final Particle[] particles = new Particle[200];
    private ScaledResolution scaledResolution;

    public ParticleSystem(ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
        for (int i = 0; i < 200; ++i) {
            this.particles[i] = new Particle(new Vector2f((float)(Math.random() * (double)scaledResolution.getScaledWidth()), (float)(Math.random() * (double)scaledResolution.getScaledHeight())));
        }
    }

    public static double map(double value, double a, double b, double c, double d) {
        value = (value - a) / (b - a);
        return c + value * (d - c);
    }

    public void update() {
        for (int i = 0; i < 200; ++i) {
            Particle particle = this.particles[i];
            if (this.scaledResolution != null) {
                boolean isOffScreenY;
                boolean isOffScreenX = particle.getPos().x > (float)this.scaledResolution.getScaledWidth() || particle.getPos().x < 0.0f;
                boolean bl = isOffScreenY = particle.getPos().y > (float)this.scaledResolution.getScaledHeight() || particle.getPos().y < 0.0f;
                if (isOffScreenX || isOffScreenY) {
                    particle.respawn(this.scaledResolution);
                }
            }
            particle.update();
        }
    }

    public void render(int mouseX, int mouseY) {
        if (!ClickGui.getInstance().particles.getValue().booleanValue()) {
            return;
        }
        for (int i = 0; i < 200; ++i) {
            Particle particle = this.particles[i];
            for (int j = 1; j < 200; ++j) {
                int lineAlpha;
                if (i == j) continue;
                Particle otherParticle = this.particles[j];
                Vector2f diffPos = new Vector2f(particle.getPos());
                diffPos.sub((Tuple2f)otherParticle.getPos());
                float diff = diffPos.length();
                int distance = ClickGui.getInstance().particleLength.getValue() / (this.scaledResolution.getScaleFactor() <= 1 ? 3 : this.scaledResolution.getScaleFactor());
                if (!(diff < (float)distance) || (lineAlpha = (int)ParticleSystem.map(diff, distance, 0.0, 0.0, 127.0)) <= 8) continue;
                RenderUtil.drawLine(particle.getPos().x + particle.getSize() / 2.0f, particle.getPos().y + particle.getSize() / 2.0f, otherParticle.getPos().x + otherParticle.getSize() / 2.0f, otherParticle.getPos().y + otherParticle.getSize() / 2.0f, 1.0f, Particle.changeAlpha(ColorUtil.toRGBA(ClickGui.getInstance().rbg.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRed() : ClickGui.getInstance().particlered.getValue().intValue(), ClickGui.getInstance().rbg.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getGreen() : ClickGui.getInstance().particlegreen.getValue().intValue(), ClickGui.getInstance().rbg.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getBlue() : ClickGui.getInstance().particleblue.getValue().intValue()), lineAlpha));
            }
            particle.render(mouseX, mouseY);
        }
    }

    public ScaledResolution getScaledResolution() {
        return this.scaledResolution;
    }

    public void setScaledResolution(ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
    }
}

