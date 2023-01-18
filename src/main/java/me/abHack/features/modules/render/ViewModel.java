/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.abHack.features.modules.render;

import me.abHack.event.events.RenderItemEvent;
import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ViewModel
extends Module {
    private static ViewModel INSTANCE = new ViewModel();
    public Setting<Settings> settings = this.register(new Setting<Settings>("Settings", Settings.TRANSLATE));
    public Setting<Boolean> noEatAnimation = this.register(new Setting<Object>("NoEatAnimation", Boolean.valueOf(false), v -> this.settings.getValue() == Settings.TWEAKS));
    public Setting<Double> eatX = this.register(new Setting<Object>("EatX", Double.valueOf(1.0), Double.valueOf(-2.0), Double.valueOf(5.0), v -> this.settings.getValue() == Settings.TWEAKS && this.noEatAnimation.getValue() == false));
    public Setting<Double> eatY = this.register(new Setting<Object>("EatY", Double.valueOf(1.0), Double.valueOf(-2.0), Double.valueOf(5.0), v -> this.settings.getValue() == Settings.TWEAKS && this.noEatAnimation.getValue() == false));
    public Setting<Boolean> doBob = this.register(new Setting<Object>("ItemBob", Boolean.valueOf(true), v -> this.settings.getValue() == Settings.TWEAKS));
    public Setting<Double> mainX = this.register(new Setting<Object>("MainX", Double.valueOf(1.2), Double.valueOf(-2.0), Double.valueOf(4.0), v -> this.settings.getValue() == Settings.TRANSLATE));
    public Setting<Double> mainY = this.register(new Setting<Object>("MainY", Double.valueOf(-0.95), Double.valueOf(-3.0), Double.valueOf(3.0), v -> this.settings.getValue() == Settings.TRANSLATE));
    public Setting<Double> mainZ = this.register(new Setting<Object>("MainZ", Double.valueOf(-1.45), Double.valueOf(-5.0), Double.valueOf(5.0), v -> this.settings.getValue() == Settings.TRANSLATE));
    public Setting<Double> offX = this.register(new Setting<Object>("OffX", Double.valueOf(1.2), Double.valueOf(-2.0), Double.valueOf(4.0), v -> this.settings.getValue() == Settings.TRANSLATE));
    public Setting<Double> offY = this.register(new Setting<Object>("OffY", Double.valueOf(-0.95), Double.valueOf(-3.0), Double.valueOf(3.0), v -> this.settings.getValue() == Settings.TRANSLATE));
    public Setting<Double> offZ = this.register(new Setting<Object>("OffZ", Double.valueOf(-1.45), Double.valueOf(-5.0), Double.valueOf(5.0), v -> this.settings.getValue() == Settings.TRANSLATE));
    public Setting<Integer> mainRotX = this.register(new Setting<Object>("MainRotationX", Integer.valueOf(0), Integer.valueOf(-36), Integer.valueOf(36), v -> this.settings.getValue() == Settings.ROTATE));
    public Setting<Integer> mainRotY = this.register(new Setting<Object>("MainRotationY", Integer.valueOf(0), Integer.valueOf(-36), Integer.valueOf(36), v -> this.settings.getValue() == Settings.ROTATE));
    public Setting<Integer> mainRotZ = this.register(new Setting<Object>("MainRotationZ", Integer.valueOf(0), Integer.valueOf(-36), Integer.valueOf(36), v -> this.settings.getValue() == Settings.ROTATE));
    public Setting<Integer> offRotX = this.register(new Setting<Object>("OffRotationX", Integer.valueOf(0), Integer.valueOf(-36), Integer.valueOf(36), v -> this.settings.getValue() == Settings.ROTATE));
    public Setting<Integer> offRotY = this.register(new Setting<Object>("OffRotationY", Integer.valueOf(0), Integer.valueOf(-36), Integer.valueOf(36), v -> this.settings.getValue() == Settings.ROTATE));
    public Setting<Integer> offRotZ = this.register(new Setting<Object>("OffRotationZ", Integer.valueOf(0), Integer.valueOf(-36), Integer.valueOf(36), v -> this.settings.getValue() == Settings.ROTATE));
    public Setting<Double> mainScaleX = this.register(new Setting<Object>("MainScaleX", Double.valueOf(1.0), Double.valueOf(0.1), Double.valueOf(5.0), v -> this.settings.getValue() == Settings.SCALE));
    public Setting<Double> mainScaleY = this.register(new Setting<Object>("MainScaleY", Double.valueOf(1.0), Double.valueOf(0.1), Double.valueOf(5.0), v -> this.settings.getValue() == Settings.SCALE));
    public Setting<Double> mainScaleZ = this.register(new Setting<Object>("MainScaleZ", Double.valueOf(1.0), Double.valueOf(0.1), Double.valueOf(5.0), v -> this.settings.getValue() == Settings.SCALE));
    public Setting<Double> offScaleX = this.register(new Setting<Object>("OffScaleX", Double.valueOf(1.0), Double.valueOf(0.1), Double.valueOf(5.0), v -> this.settings.getValue() == Settings.SCALE));
    public Setting<Double> offScaleY = this.register(new Setting<Object>("OffScaleY", Double.valueOf(1.0), Double.valueOf(0.1), Double.valueOf(5.0), v -> this.settings.getValue() == Settings.SCALE));
    public Setting<Double> offScaleZ = this.register(new Setting<Object>("OffScaleZ", Double.valueOf(1.0), Double.valueOf(0.1), Double.valueOf(5.0), v -> this.settings.getValue() == Settings.SCALE));

    public ViewModel() {
        super("ViewModel", "Change the position of the arm", Module.Category.RENDER, true, false, false);
        this.setInstance();
    }

    public static ViewModel getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ViewModel();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onItemRender(RenderItemEvent event) {
        event.setMainX(this.mainX.getValue());
        event.setMainY(this.mainY.getValue());
        event.setMainZ(this.mainZ.getValue());
        event.setOffX(-this.offX.getValue().doubleValue());
        event.setOffY(this.offY.getValue());
        event.setOffZ(this.offZ.getValue());
        event.setMainRotX(this.mainRotX.getValue() * 5);
        event.setMainRotY(this.mainRotY.getValue() * 5);
        event.setMainRotZ(this.mainRotZ.getValue() * 5);
        event.setOffRotX(this.offRotX.getValue() * 5);
        event.setOffRotY(this.offRotY.getValue() * 5);
        event.setOffRotZ(this.offRotZ.getValue() * 5);
        event.setOffHandScaleX(this.offScaleX.getValue());
        event.setOffHandScaleY(this.offScaleY.getValue());
        event.setOffHandScaleZ(this.offScaleZ.getValue());
        event.setMainHandScaleX(this.mainScaleX.getValue());
        event.setMainHandScaleY(this.mainScaleY.getValue());
        event.setMainHandScaleZ(this.mainScaleZ.getValue());
    }

    private static enum Settings {
        TRANSLATE,
        ROTATE,
        SCALE,
        TWEAKS;

    }
}

