package me.abHack.mixin.mixins;

import org.spongepowered.asm.mixin.injection.Inject;
import org.lwjgl.opengl.GL11;
import me.abHack.features.modules.render.Chams;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import me.abHack.event.events.RenderEntityModelEvent;
import me.abHack.features.modules.render.ESP;
import me.abHack.features.modules.render.Skeleton;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.EntityLivingBase;

@Mixin({ RenderLivingBase.class })
public abstract class MixinRenderLivingBase<T extends EntityLivingBase> extends Render<T>
{
    public MixinRenderLivingBase(final RenderManager renderManagerIn, final ModelBase modelBaseIn, final float shadowSizeIn) {
        super(renderManagerIn);
    }

    @Redirect(method = { "renderModel" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    private void renderModelHook(final ModelBase modelBase, final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        boolean cancel = false;
        if (Skeleton.getInstance().isEnabled() || ESP.getInstance().isEnabled()) {
            final RenderEntityModelEvent event = new RenderEntityModelEvent(0, modelBase, entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            if (Skeleton.getInstance().isEnabled()) {
                Skeleton.getInstance().onRenderModel(event);
            }
            if (ESP.getInstance().isEnabled()) {
                ESP.getInstance().onRenderModel(event);
                if (event.isCanceled()) {
                    cancel = true;
                }
            }
        }
        if (!cancel) {
            modelBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Inject(method = { "doRender" }, at = { @At("HEAD") })
    public void doRenderPre(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks, final CallbackInfo info) {
        if (Chams.getInstance().isEnabled() && entity != null) {
            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0f, -1100000.0f);
        }
    }

    @Inject(method = { "doRender" }, at = { @At("RETURN") })
    public void doRenderPost(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks, final CallbackInfo info) {
        if (Chams.getInstance().isEnabled() && entity != null) {
            GL11.glPolygonOffset(1.0f, 1000000.0f);
            GL11.glDisable(32823);
        }
    }
}