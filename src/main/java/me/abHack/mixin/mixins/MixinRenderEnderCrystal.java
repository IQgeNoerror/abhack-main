package me.abHack.mixin.mixins;

import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.util.math.MathHelper;
import me.abHack.features.modules.render.ESP;
import me.abHack.OyVey;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import me.abHack.util.EntityUtil;
import java.awt.Color;
import me.abHack.util.RenderUtil;
import me.abHack.util.ColorUtil;
import me.abHack.features.modules.client.ClickGui;
import org.lwjgl.opengl.GL11;
import me.abHack.event.events.RenderEntityModelEvent;
import net.minecraft.client.renderer.GlStateManager;
import me.abHack.features.modules.render.CrystalScale;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.client.model.ModelBase;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RenderEnderCrystal.class })
public abstract class MixinRenderEnderCrystal
{
    @Final
    @Shadow
    private static ResourceLocation ENDER_CRYSTAL_TEXTURES;
    @Shadow
    public ModelBase modelEnderCrystal;
    @Shadow
    public ModelBase modelEnderCrystalNoBase;

    @Shadow
    public abstract void doRender(final EntityEnderCrystal p0, final double p1, final double p2, final double p3, final float p4, final float p5);

    @Redirect(method = { "doRender" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    public void renderModelBaseHook(final ModelBase model, final Entity entity, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (CrystalScale.INSTANCE.isEnabled()) {
            if (CrystalScale.INSTANCE.animateScale.getValue() && CrystalScale.INSTANCE.scaleMap.containsKey(entity)) {
                GlStateManager.scale((float)CrystalScale.INSTANCE.scaleMap.get(entity), (float)CrystalScale.INSTANCE.scaleMap.get(entity), (float)CrystalScale.INSTANCE.scaleMap.get(entity));
            }
            else {
                GlStateManager.scale((float)CrystalScale.INSTANCE.scale.getValue(), (float)CrystalScale.INSTANCE.scale.getValue(), (float)CrystalScale.INSTANCE.scale.getValue());
            }
        }
        if (CrystalScale.INSTANCE.isEnabled() && CrystalScale.INSTANCE.wireframe.getValue()) {
            final RenderEntityModelEvent event = new RenderEntityModelEvent(0, model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            CrystalScale.INSTANCE.onRenderModel(event);
        }
        if (CrystalScale.INSTANCE.isEnabled() && CrystalScale.INSTANCE.chams.getValue()) {
            GL11.glPushAttrib(1048575);
            GL11.glDisable(3008);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glLineWidth(1.5f);
            GL11.glEnable(2960);
            if (CrystalScale.INSTANCE.rainbow.getValue()) {
                final Color rainbowColor1 = CrystalScale.INSTANCE.rainbow.getValue() ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(RenderUtil.getRainbow(CrystalScale.INSTANCE.speed.getValue() * 100, 0, CrystalScale.INSTANCE.saturation.getValue() / 100.0f, CrystalScale.INSTANCE.brightness.getValue() / 100.0f));
                final Color rainbowColor2 = EntityUtil.getColor(entity, rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue(), CrystalScale.INSTANCE.alpha.getValue(), true);
                if (CrystalScale.INSTANCE.throughWalls.getValue()) {
                    GL11.glDisable(2929);
                    GL11.glDepthMask(false);
                }
                GL11.glEnable(10754);
                GL11.glColor4f(rainbowColor2.getRed() / 255.0f, rainbowColor2.getGreen() / 255.0f, rainbowColor2.getBlue() / 255.0f, CrystalScale.INSTANCE.alpha.getValue() / 255.0f);
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                if (CrystalScale.INSTANCE.throughWalls.getValue()) {
                    GL11.glEnable(2929);
                    GL11.glDepthMask(true);
                }
            }
            else if (CrystalScale.INSTANCE.xqz.getValue() && CrystalScale.INSTANCE.throughWalls.getValue()) {
                final Color hiddenColor = EntityUtil.getColor(entity, CrystalScale.INSTANCE.hiddenRed.getValue(), CrystalScale.INSTANCE.hiddenGreen.getValue(), CrystalScale.INSTANCE.hiddenBlue.getValue(), CrystalScale.INSTANCE.hiddenAlpha.getValue(), true);
                final Color color;
                final Color visibleColor = color = EntityUtil.getColor(entity, CrystalScale.INSTANCE.red.getValue(), CrystalScale.INSTANCE.green.getValue(), CrystalScale.INSTANCE.blue.getValue(), CrystalScale.INSTANCE.alpha.getValue(), true);
                if (CrystalScale.INSTANCE.throughWalls.getValue()) {
                    GL11.glDisable(2929);
                    GL11.glDepthMask(false);
                }
                GL11.glEnable(10754);
                GL11.glColor4f(hiddenColor.getRed() / 255.0f, hiddenColor.getGreen() / 255.0f, hiddenColor.getBlue() / 255.0f, CrystalScale.INSTANCE.alpha.getValue() / 255.0f);
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                if (CrystalScale.INSTANCE.throughWalls.getValue()) {
                    GL11.glEnable(2929);
                    GL11.glDepthMask(true);
                }
                GL11.glColor4f(visibleColor.getRed() / 255.0f, visibleColor.getGreen() / 255.0f, visibleColor.getBlue() / 255.0f, CrystalScale.INSTANCE.alpha.getValue() / 255.0f);
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            }
            else {
                final Color color2;
                final Color visibleColor = color2 = (CrystalScale.INSTANCE.rainbow.getValue() ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : EntityUtil.getColor(entity, CrystalScale.INSTANCE.red.getValue(), CrystalScale.INSTANCE.green.getValue(), CrystalScale.INSTANCE.blue.getValue(), CrystalScale.INSTANCE.alpha.getValue(), true));
                if (CrystalScale.INSTANCE.throughWalls.getValue()) {
                    GL11.glDisable(2929);
                    GL11.glDepthMask(false);
                }
                GL11.glEnable(10754);
                GL11.glColor4f(visibleColor.getRed() / 255.0f, visibleColor.getGreen() / 255.0f, visibleColor.getBlue() / 255.0f, CrystalScale.INSTANCE.alpha.getValue() / 255.0f);
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                if (CrystalScale.INSTANCE.throughWalls.getValue()) {
                    GL11.glEnable(2929);
                    GL11.glDepthMask(true);
                }
            }
            GL11.glEnable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            GL11.glPopAttrib();
        }
        else {
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
        if (CrystalScale.INSTANCE.isEnabled()) {
            if (CrystalScale.INSTANCE.animateScale.getValue() && CrystalScale.INSTANCE.scaleMap.containsKey(entity)) {
                GlStateManager.scale(1.0f / CrystalScale.INSTANCE.scaleMap.get(entity), 1.0f / CrystalScale.INSTANCE.scaleMap.get(entity), 1.0f / CrystalScale.INSTANCE.scaleMap.get(entity));
            }
            else {
                GlStateManager.scale(1.0f / CrystalScale.INSTANCE.scale.getValue(), 1.0f / CrystalScale.INSTANCE.scale.getValue(), 1.0f / CrystalScale.INSTANCE.scale.getValue());
            }
        }
    }

    @Inject(method = { "doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V" }, at = { @At("RETURN") }, cancellable = true)
    public void IdoRender(final EntityEnderCrystal var1, final double var2, final double var4, final double var6, final float var8, final float var9, final CallbackInfo var10) {
        final Minecraft mc = Minecraft.getMinecraft();
        mc.gameSettings.fancyGraphics = false;
        if (OyVey.moduleManager.isModuleEnabled("ESP") && ESP.getInstance().others.getValue()) {
            final float var11 = var1.innerRotation + var9;
            GlStateManager.pushMatrix();
            GlStateManager.translate(var2, var4, var6);
            Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(MixinRenderEnderCrystal.ENDER_CRYSTAL_TEXTURES);
            float var12 = MathHelper.sin(var11 * 0.2f) / 2.0f + 0.5f;
            var12 += var12 * var12;
            GL11.glLineWidth(5.0f);
            if (var1.shouldShowBottom()) {
                this.modelEnderCrystal.render((Entity)var1, 0.0f, var11 * 3.0f, var12 * 0.2f, 0.0f, 0.0f, 0.0625f);
            }
            else {
                this.modelEnderCrystalNoBase.render((Entity)var1, 0.0f, var11 * 3.0f, var12 * 0.2f, 0.0f, 0.0f, 0.0625f);
            }
            RenderUtil.renderOne(ESP.getInstance().lineWidth.getValue());
            if (var1.shouldShowBottom()) {
                this.modelEnderCrystal.render((Entity)var1, 0.0f, var11 * 3.0f, var12 * 0.2f, 0.0f, 0.0f, 0.0625f);
            }
            else {
                this.modelEnderCrystalNoBase.render((Entity)var1, 0.0f, var11 * 3.0f, var12 * 0.2f, 0.0f, 0.0f, 0.0625f);
            }
            RenderUtil.renderTwo();
            if (var1.shouldShowBottom()) {
                this.modelEnderCrystal.render((Entity)var1, 0.0f, var11 * 3.0f, var12 * 0.2f, 0.0f, 0.0f, 0.0625f);
            }
            else {
                this.modelEnderCrystalNoBase.render((Entity)var1, 0.0f, var11 * 3.0f, var12 * 0.2f, 0.0f, 0.0f, 0.0625f);
            }
            final Color rainbowColor1 = ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue());
            final Color rainbowColor2 = new Color(rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue());
            final Color n = new Color(rainbowColor2.getRed(), rainbowColor2.getGreen(), rainbowColor2.getBlue());
            RenderUtil.renderThree();
            RenderUtil.renderFour(rainbowColor1);
            RenderUtil.setColor(n);
            if (var1.shouldShowBottom()) {
                this.modelEnderCrystal.render((Entity)var1, 0.0f, var11 * 3.0f, var12 * 0.2f, 0.0f, 0.0f, 0.0625f);
            }
            else {
                this.modelEnderCrystalNoBase.render((Entity)var1, 0.0f, var11 * 3.0f, var12 * 0.2f, 0.0f, 0.0f, 0.0625f);
            }
            RenderUtil.renderFive();
            GlStateManager.popMatrix();
        }
    }
}