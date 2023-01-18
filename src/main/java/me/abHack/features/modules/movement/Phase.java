/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 */
package me.abHack.features.modules.movement;

import me.abHack.OyVey;
import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import net.minecraft.client.entity.EntityPlayerSP;

public class Phase
extends Module {
    private final Setting<Double> speed = this.register(new Setting<Double>("Speed", 5.0, 0.0, 50.0));

    public Phase() {
        super("Phase", "Phase.", Module.Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onEnable() {
        Phase.mc.player.capabilities.isFlying = true;
    }

    @Override
    public void onDisable() {
        Phase.mc.player.capabilities.isFlying = false;
        Phase.mc.player.noClip = false;
    }

    private boolean shouldReturn() {
        return OyVey.moduleManager.isModuleEnabled("Freecam") || OyVey.moduleManager.isModuleEnabled("Step") || OyVey.moduleManager.isModuleEnabled("ElytraFlight") || OyVey.moduleManager.isModuleEnabled("Flight") || OyVey.moduleManager.isModuleEnabled("Strafe");
    }

    @Override
    public void onUpdate() {
        if (this.shouldReturn()) {
            Phase.mc.player.capabilities.isFlying = false;
            Phase.mc.player.noClip = false;
            return;
        }
        Phase.mc.player.motionY = 0.0;
        Phase.mc.player.noClip = true;
        Phase.mc.player.capabilities.isFlying = true;
        Phase.mc.player.onGround = false;
        Phase.mc.player.fallDistance = 0.0f;
        Phase.mc.player.capabilities.setFlySpeed((float)(this.speed.getValue() / 100.0));
        if (Phase.mc.gameSettings.keyBindJump.isPressed()) {
            EntityPlayerSP thePlayer = Phase.mc.player;
            thePlayer.motionY += 0.010000000149011612;
        }
        if (Phase.mc.gameSettings.keyBindSneak.isPressed()) {
            EntityPlayerSP thePlayer2 = Phase.mc.player;
            thePlayer2.motionY -= 0.010000000149011612;
        }
    }
}

