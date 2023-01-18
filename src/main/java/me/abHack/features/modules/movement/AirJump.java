/*
 * Decompiled with CFR 0.152.
 */
package me.abHack.features.modules.movement;

import me.abHack.OyVey;
import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;

public class AirJump
extends Module {
    private boolean owo = false;
    private final Setting<Float> speed = this.register(new Setting<Float>("Speed", Float.valueOf(5.0f), Float.valueOf(1.0f), Float.valueOf(10.0f)));
    private final Setting<Float> movementspeed = this.register(new Setting<Float>("MoveSpeed", Float.valueOf(10.0f), Float.valueOf(1.0f), Float.valueOf(10.0f)));

    public AirJump() {
        super("AirJump", "AirJump.", Module.Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (AirJump.mc.player == null) {
            return;
        }
        if (this.shouldReturn()) {
            return;
        }
        AirJump.mc.player.capabilities.isFlying = false;
        AirJump.mc.player.jumpMovementFactor = this.movementspeed.getValue().floatValue() / 100.0f;
        if (AirJump.mc.gameSettings.keyBindJump.isKeyDown()) {
            if (!this.owo) {
                AirJump.mc.player.motionY = this.speed.getValue().floatValue() / 10.0f;
                this.owo = true;
            }
        } else if (!AirJump.mc.gameSettings.keyBindJump.isKeyDown()) {
            this.owo = false;
        }
    }

    private boolean shouldReturn() {
        return OyVey.moduleManager.isModuleEnabled("Freecam") || OyVey.moduleManager.isModuleEnabled("ElytraFlight") || OyVey.moduleManager.isModuleEnabled("Phase") || OyVey.moduleManager.isModuleEnabled("Flight") || OyVey.moduleManager.isModuleEnabled("Strafe");
    }
}

