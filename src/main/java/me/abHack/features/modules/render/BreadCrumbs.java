/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.entity.player.EntityPlayer
 *  org.lwjgl.opengl.GL11
 */
package me.abHack.features.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import me.abHack.event.events.Render3DEvent;
import me.abHack.features.modules.Module;
import me.abHack.features.modules.client.ClickGui;
import me.abHack.features.setting.Setting;
import me.abHack.util.ColorUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class BreadCrumbs
extends Module {
    public static Setting<Integer> length;
    public static Setting<Float> width;
    public static Setting<Boolean> rainbow;
    public static Setting<Integer> red;
    public static Setting<Integer> green;
    public static Setting<Integer> blue;
    public static Setting<Integer> alpha;
    public static ArrayList<double[]> vecs;
    public Color color;

    public BreadCrumbs() {
        super("BreadCrumbs", "Draws a small line behind you", Module.Category.RENDER, true, false, false);
        length = this.register(new Setting<Integer>("Length", 15, 5, 40));
        width = this.register(new Setting<Float>("Width", Float.valueOf(1.5f), Float.valueOf(0.5f), Float.valueOf(5.0f)));
        red = this.register(new Setting<Integer>("Red", 30, 0, 255));
        green = this.register(new Setting<Integer>("Green", 167, 0, 255));
        blue = this.register(new Setting<Integer>("Blue", 255, 0, 255));
        alpha = this.register(new Setting<Integer>("Alpha", 255, 0, 255));
        rainbow = this.register(new Setting<Boolean>("Rainbow", false));
        vecs = new ArrayList();
    }

    public static double M(double n) {
        if (n == Double.longBitsToDouble(Double.doubleToLongBits(1.7931000183463725E308) ^ 0x7FEFEB11C3AAD037L)) {
            return n;
        }
        if (n < Double.longBitsToDouble(Double.doubleToLongBits(1.1859585260803721E308) ^ 0x7FE51C5AEE8AD07FL)) {
            return n * Double.longBitsToDouble(Double.doubleToLongBits(-12.527781766526259) ^ 0x7FD90E3969654F8FL);
        }
        return n;
    }

    public static void prepareGL() {
        GL11.glBlendFunc((int)770, (int)771);
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth((float)Float.intBitsToFloat(Float.floatToIntBits(5.0675106f) ^ 0x7F22290C));
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask((boolean)false);
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.color((float)Float.intBitsToFloat(Float.floatToIntBits(11.925059f) ^ 0x7EBECD0B), (float)Float.intBitsToFloat(Float.floatToIntBits(18.2283f) ^ 0x7E11D38F), (float)Float.intBitsToFloat(Float.floatToIntBits(9.73656f) ^ 0x7E9BC8F3));
    }

    public static void releaseGL() {
        GlStateManager.enableCull();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
        GlStateManager.color((float)Float.intBitsToFloat(Float.floatToIntBits(12.552789f) ^ 0x7EC8D839), (float)Float.intBitsToFloat(Float.floatToIntBits(7.122752f) ^ 0x7F63ED96), (float)Float.intBitsToFloat(Float.floatToIntBits(5.4278784f) ^ 0x7F2DB12E));
        GL11.glColor4f((float)Float.intBitsToFloat(Float.floatToIntBits(10.5715685f) ^ 0x7EA92525), (float)Float.intBitsToFloat(Float.floatToIntBits(4.9474883f) ^ 0x7F1E51D3), (float)Float.intBitsToFloat(Float.floatToIntBits(4.9044757f) ^ 0x7F1CF177), (float)Float.intBitsToFloat(Float.floatToIntBits(9.482457f) ^ 0x7E97B825));
    }

    public Color getCurrentColor() {
        return new Color(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue());
    }

    @Override
    public void onUpdate() {
        this.color = rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : this.getCurrentColor();
        try {
            double renderPosX = BreadCrumbs.mc.getRenderManager().renderPosX;
            double renderPosY = BreadCrumbs.mc.getRenderManager().renderPosY;
            double renderPosZ = BreadCrumbs.mc.getRenderManager().renderPosZ;
            if (this.isEnabled()) {
                for (EntityPlayer next : BreadCrumbs.mc.world.playerEntities) {
                    if (!(next instanceof EntityPlayer)) continue;
                    EntityPlayer entityPlayer = next;
                    boolean b = entityPlayer == BreadCrumbs.mc.player;
                    double n = renderPosY + Double.longBitsToDouble(Double.doubleToLongBits(0.48965838138858014) ^ 0x7FDF56901B91AE07L);
                    if (BreadCrumbs.mc.player.isElytraFlying()) {
                        n -= Double.longBitsToDouble(Double.doubleToLongBits(29.56900080933637) ^ 0x7FC591AA097B7F4BL);
                    }
                    if (!b) continue;
                    vecs.add(new double[]{renderPosX, n - (double)entityPlayer.height, renderPosZ});
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (vecs.size() > length.getValue()) {
            vecs.remove(0);
        }
    }

    @Override
    public void onDisable() {
        vecs.removeAll(vecs);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        try {
            double renderPosX = BreadCrumbs.mc.getRenderManager().renderPosX;
            double renderPosY = BreadCrumbs.mc.getRenderManager().renderPosY;
            double renderPosZ = BreadCrumbs.mc.getRenderManager().renderPosZ;
            float n = (float)this.color.getRed() / Float.intBitsToFloat(Float.floatToIntBits(0.49987957f) ^ 0x7D80F037);
            float n2 = (float)this.color.getGreen() / Float.intBitsToFloat(Float.floatToIntBits(0.4340212f) ^ 0x7DA13807);
            float n3 = (float)this.color.getBlue() / Float.intBitsToFloat(Float.floatToIntBits(0.0131841665f) ^ 0x7F270267);
            if (this.isEnabled()) {
                Iterator<double[]> iterator3;
                BreadCrumbs.prepareGL();
                GL11.glPushMatrix();
                GL11.glEnable((int)2848);
                GL11.glLineWidth((float)width.getValue().floatValue());
                GL11.glBlendFunc((int)770, (int)771);
                GL11.glLineWidth((float)width.getValue().floatValue());
                GL11.glDepthMask((boolean)false);
                GL11.glBegin((int)3);
                Iterator<double[]> iterator2 = iterator3 = vecs.iterator();
                while (iterator3.hasNext()) {
                    double d = 0.0;
                    double[] array = iterator2.next();
                    double m = BreadCrumbs.M(Math.hypot(array[0] - BreadCrumbs.mc.player.posX, array[1] - BreadCrumbs.mc.player.posY));
                    if (d > (double)length.getValue().intValue()) {
                        iterator3 = iterator2;
                        continue;
                    }
                    GL11.glColor4f((float)n, (float)n2, (float)n3, (float)(Float.intBitsToFloat(Float.floatToIntBits(14.099797f) ^ 0x7EE198C5) - (float)(m / (double)length.getValue().intValue())));
                    iterator3 = iterator2;
                    GL11.glVertex3d((double)(array[0] - renderPosX), (double)(array[1] - renderPosY), (double)(array[2] - renderPosZ));
                }
                GL11.glEnd();
                GL11.glDepthMask((boolean)true);
                GL11.glPopMatrix();
                BreadCrumbs.releaseGL();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

