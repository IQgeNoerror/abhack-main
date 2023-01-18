package me.abHack.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.abHack.features.modules.movement.EntityControl;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.entity.passive.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ AbstractHorse.class })
public class MixinAbstractHorse
{
    @Inject(method = { "isHorseSaddled" }, at = { @At("HEAD") }, cancellable = true)
    public void isHorseSaddled(final CallbackInfoReturnable<Boolean> cir) {
        if (EntityControl.INSTANCE.isEnabled()) {
            cir.setReturnValue(true);
        }
    }
}