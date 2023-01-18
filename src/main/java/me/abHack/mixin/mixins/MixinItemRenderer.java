package me.abHack.mixin.mixins;

import net.minecraft.util.math.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.abHack.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import me.abHack.features.modules.render.ViewModel;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import me.abHack.event.events.RenderItemEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.util.EnumHandSide;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ ItemRenderer.class })
public abstract class MixinItemRenderer
{
    private final boolean injection;

    public MixinItemRenderer() {
        this.injection = true;
    }

    @Inject(method = { "transformSideFirstPerson" }, at = { @At("HEAD") }, cancellable = true)
    public void transformSideFirstPerson(final EnumHandSide hand, final float p_187459_2_, final CallbackInfo cancel) {
        final RenderItemEvent event = new RenderItemEvent(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (ViewModel.getInstance().isEnabled()) {
            final boolean bob = ViewModel.getInstance().isDisabled() || ViewModel.getInstance().doBob.getValue();
            final int i = (hand == EnumHandSide.RIGHT) ? 1 : -1;
            GlStateManager.translate(i * 0.56f, -0.52f + (bob ? p_187459_2_ : 0.0f) * -0.6f, -0.72f);
            if (hand == EnumHandSide.RIGHT) {
                GlStateManager.translate(event.getMainX(), event.getMainY(), event.getMainZ());
                RenderUtil.rotationHelper((float)event.getMainRotX(), (float)event.getMainRotY(), (float)event.getMainRotZ());
            }
            else {
                GlStateManager.translate(event.getOffX(), event.getOffY(), event.getOffZ());
                RenderUtil.rotationHelper((float)event.getOffRotX(), (float)event.getOffRotY(), (float)event.getOffRotZ());
            }
            cancel.cancel();
        }
    }

    @Inject(method = { "transformEatFirstPerson" }, at = { @At("HEAD") }, cancellable = true)
    private void transformEatFirstPerson(final float p_187454_1_, final EnumHandSide hand, final ItemStack stack, final CallbackInfo cancel) {
        if (ViewModel.getInstance().isEnabled()) {
            if (!ViewModel.getInstance().noEatAnimation.getValue()) {
                final float f = Minecraft.getMinecraft().player.getItemInUseCount() - p_187454_1_ + 1.0f;
                final float f2 = f / stack.getMaxItemUseDuration();
                if (f2 < 0.8f) {
                    final float f3 = MathHelper.abs(MathHelper.cos(f / 4.0f * 3.1415927f) * 0.1f);
                    GlStateManager.translate(0.0f, f3, 0.0f);
                }
                final float f3 = 1.0f - (float)Math.pow(f2, 27.0);
                final int i = (hand == EnumHandSide.RIGHT) ? 1 : -1;
                GlStateManager.translate(f3 * 0.6f * i * ViewModel.getInstance().eatX.getValue(), f3 * 0.5f * -ViewModel.getInstance().eatY.getValue(), 0.0);
                GlStateManager.rotate(i * f3 * 90.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(f3 * 10.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(i * f3 * 30.0f, 0.0f, 0.0f, 1.0f);
            }
            cancel.cancel();
        }
    }
}