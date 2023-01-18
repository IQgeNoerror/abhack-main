/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.util.MovementInput
 *  net.minecraft.util.MovementInputFromOptions
 *  org.lwjgl.input.Keyboard
 */
package me.abHack.mixin.mixins;

import me.abHack.event.MixinInterface;
import me.abHack.features.modules.movement.PlayerTweaks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={MovementInputFromOptions.class}, priority=10001)
public class MixinMovementInputFromOptions
extends MovementInput
implements MixinInterface {
    @Redirect(method={"updatePlayerMoveState"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"))
    public boolean isKeyPressed(KeyBinding keyBinding) {
        int keyCode = keyBinding.getKeyCode();
        if (keyCode <= 0) {
            return keyBinding.isKeyDown();
        }
        if (keyCode >= 256) {
            return keyBinding.isKeyDown();
        }
        if (!PlayerTweaks.getInstance().isOn()) {
            return keyBinding.isKeyDown();
        }
        if (!PlayerTweaks.getInstance().guiMove.getValue().booleanValue()) {
            return keyBinding.isKeyDown();
        }
        if (Minecraft.getMinecraft().currentScreen == null) {
            return keyBinding.isKeyDown();
        }
        if (Minecraft.getMinecraft().currentScreen instanceof GuiChat) {
            return keyBinding.isKeyDown();
        }
        return Keyboard.isKeyDown((int)keyCode);
    }
}

