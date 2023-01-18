package me.abHack.mixin.mixins;

import org.spongepowered.asm.mixin.Final;
import net.minecraft.client.renderer.entity.RenderManager;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.renderer.entity.Render;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.entity.Entity;

@Mixin({ Render.class })
abstract class MixinRenderer<T extends Entity>
{
    @Shadow
    protected boolean renderOutlines;
    @Shadow
    @Final
    protected RenderManager renderManager;

    @Shadow
    protected abstract boolean bindEntityTexture(final T p0);

    @Shadow
    protected abstract int getTeamColor(final T p0);
}