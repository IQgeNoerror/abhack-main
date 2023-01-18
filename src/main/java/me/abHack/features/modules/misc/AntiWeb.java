/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.abHack.features.modules.misc;

import me.abHack.event.events.UpdateWalkingPlayerEvent;
import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import me.abHack.util.MathUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiWeb
extends Module {
    private final Setting<Float> speed = this.register(new Setting<Float>("Factor", Float.valueOf(10.0f), Float.valueOf(1.0f), Float.valueOf(10.0f)));

    public AntiWeb() {
        super("AntiWeb", "Stops you being slowed down by webs", Module.Category.MISC, true, false, false);
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 1) {
            return;
        }
        if (AntiWeb.mc.player.isInWeb) {
            double[] calc = MathUtil.directionSpeed((double)this.speed.getValue().floatValue() / 10.0);
            AntiWeb.mc.player.motionX = calc[0];
            AntiWeb.mc.player.motionZ = calc[1];
            if (AntiWeb.mc.gameSettings.keyBindSneak.isKeyDown()) {
                AntiWeb.mc.player.motionY -= (double)(this.speed.getValue().floatValue() / 10.0f);
            }
        }
    }
}

