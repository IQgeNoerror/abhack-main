/*
 * Decompiled with CFR 0.152.
 */
package me.abHack.features.modules.movement;

import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;

public final class Flight
extends Module {
    private static Flight INSTANCE = new Flight();
    private final Setting<Float> speed = this.register(new Setting<Float>("Speed", Float.valueOf(1.0f), Float.valueOf(1.0f), Float.valueOf(10.0f)));
    private final Setting<Boolean> glide = this.register(new Setting<Boolean>("Glide", true));

    public Flight() {
        super("Flight", "Allows you to fly.", Module.Category.MOVEMENT, false, false, false);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (Flight.mc.player == null || Flight.mc.world == null) {
            return;
        }
        Flight.mc.player.capabilities.isFlying = false;
        Flight.mc.player.motionX = 0.0;
        Flight.mc.player.motionY = 0.0;
        Flight.mc.player.motionZ = 0.0;
        Flight.mc.player.jumpMovementFactor = this.speed.getValue().floatValue();
        if (this.glide.getValue().booleanValue() && !Flight.mc.player.onGround) {
            Flight.mc.player.motionY = -0.0315f;
            Flight.mc.player.jumpMovementFactor *= 1.21337f;
        }
        if (Flight.mc.gameSettings.keyBindJump.isKeyDown()) {
            Flight.mc.player.motionY += (double)this.speed.getValue().floatValue();
        }
        if (Flight.mc.gameSettings.keyBindSneak.isKeyDown()) {
            Flight.mc.player.motionY -= (double)this.speed.getValue().floatValue();
        }
    }
}

