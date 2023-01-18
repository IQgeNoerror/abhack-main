/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.settings.GameSettings$Options
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.abHack.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.abHack.OyVey;
import me.abHack.event.events.ClientEvent;
import me.abHack.event.events.Render2DEvent;
import me.abHack.features.command.Command;
import me.abHack.features.gui.OyVeyGui;
import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import me.abHack.util.ColorUtil;
import me.abHack.util.RenderUtil;
import me.abHack.util.Util;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClickGui
extends Module {
    private static ClickGui INSTANCE = new ClickGui();
    private final Setting<Settings> setting = this.register(new Setting<Settings>("Settings", Settings.Gui));
    public Setting<String> prefix = this.register(new Setting<Object>("Prefix", ".", v -> this.setting.getValue() == Settings.Gui));
    public Setting<Boolean> customFov = this.register(new Setting<Object>("CustomFov", Boolean.FALSE, v -> this.setting.getValue() == Settings.Gui));
    public Setting<Float> fov = this.register(new Setting<Object>("Fov", Float.valueOf(90.0f), Float.valueOf(-180.0f), Float.valueOf(180.0f), v -> this.setting.getValue() == Settings.Gui && this.customFov.getValue() != false));
    public Setting<Integer> red = this.register(new Setting<Object>("Red", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Gui));
    public Setting<Integer> green = this.register(new Setting<Object>("Green", Integer.valueOf(24), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Gui));
    public Setting<Integer> blue = this.register(new Setting<Object>("Blue", Integer.valueOf(250), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Gui));
    public Setting<Integer> hoverAlpha = this.register(new Setting<Object>("Alpha", Integer.valueOf(225), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Gui));
    public Setting<Integer> alphaBox = this.register(new Setting<Object>("AlphaBox", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Gui));
    public Setting<Integer> alpha = this.register(new Setting<Object>("HoverAlpha", Integer.valueOf(240), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Gui));
    public Setting<Boolean> rainbow = this.register(new Setting<Object>("Rainbow", Boolean.TRUE, v -> this.setting.getValue() == Settings.Gui));
    public Setting<rainbowMode> rainbowModeHud = this.register(new Setting<Object>("HUD", (Object)rainbowMode.Static, v -> this.rainbow.getValue() != false && this.setting.getValue() == Settings.Gui));
    public Setting<rainbowModeArray> rainbowModeA = this.register(new Setting<Object>("ArrayList", (Object)rainbowModeArray.Up, v -> this.rainbow.getValue() != false && this.setting.getValue() == Settings.Gui));
    public Setting<Integer> rainbowHue = this.register(new Setting<Object>("Delay", Integer.valueOf(600), Integer.valueOf(0), Integer.valueOf(600), v -> this.rainbow.getValue() != false && this.setting.getValue() == Settings.Gui));
    public Setting<Float> rainbowBrightness = this.register(new Setting<Object>("Brightness ", Float.valueOf(255.0f), Float.valueOf(1.0f), Float.valueOf(255.0f), v -> this.rainbow.getValue() != false && this.setting.getValue() == Settings.Gui));
    public Setting<Float> rainbowSaturation = this.register(new Setting<Object>("Saturation", Float.valueOf(255.0f), Float.valueOf(1.0f), Float.valueOf(255.0f), v -> this.rainbow.getValue() != false && this.setting.getValue() == Settings.Gui));
    public Setting<Boolean> outline = this.register(new Setting<Boolean>("Outline", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.Gui));
    public Setting<Boolean> moduleDescription = this.register(new Setting<Boolean>("Description", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.Gui));
    public Setting<Boolean> snowing = this.register(new Setting<Boolean>("Snowing", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.Gui));
    public Setting<Boolean> particles = this.register(new Setting<Boolean>("Particles", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.Gui));
    public Setting<Integer> particleLength = this.register(new Setting<Integer>("ParticleLength", Integer.valueOf(80), Integer.valueOf(0), Integer.valueOf(300), v -> this.setting.getValue() == Settings.Gui && this.particles.getValue() != false));
    public Setting<Integer> particlered = this.register(new Setting<Integer>("ParticleRed", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Gui && this.particles.getValue() != false));
    public Setting<Integer> particlegreen = this.register(new Setting<Integer>("ParticleGreen", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Gui && this.particles.getValue() != false));
    public Setting<Integer> particleblue = this.register(new Setting<Integer>("ParticleBlue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Gui && this.particles.getValue() != false));
    public Setting<Boolean> rbg = this.register(new Setting<Object>("Rainbow", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.Gui && this.particles.getValue() != false));
    public Setting<Boolean> rainbowg = this.register(new Setting<Object>("Rainbow", Boolean.FALSE, v -> this.setting.getValue() == Settings.Gradient));
    public Setting<Boolean> guiComponent = this.register(new Setting<Object>("Gui Component", Boolean.TRUE, v -> this.setting.getValue() == Settings.Gradient));
    public Setting<Integer> g_red = this.register(new Setting<Object>("RedL", Integer.valueOf(105), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Gradient));
    public Setting<Integer> g_green = this.register(new Setting<Object>("GreenL", Integer.valueOf(162), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Gradient));
    public Setting<Integer> g_blue = this.register(new Setting<Object>("BlueL", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Gradient));
    public Setting<Integer> g_red1 = this.register(new Setting<Object>("RedR", Integer.valueOf(143), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Gradient));
    public Setting<Integer> g_green1 = this.register(new Setting<Object>("GreenR", Integer.valueOf(140), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Gradient));
    public Setting<Integer> g_blue1 = this.register(new Setting<Object>("BlueR", Integer.valueOf(213), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Gradient));
    public Setting<Integer> g_alpha = this.register(new Setting<Object>("AlphaL", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Gradient));
    public Setting<Integer> g_alpha1 = this.register(new Setting<Object>("AlphaR", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Gradient));
    public Setting<Mode> mode = this.register(new Setting<Object>("Mode", (Object)Mode.BLUR, v -> this.setting.getValue() == Settings.Background));
    public Setting<Integer> backgroundAlpha = this.register(new Setting<Object>("Background Alpha", Integer.valueOf(160), Integer.valueOf(0), Integer.valueOf(255), v -> this.mode.getValue() == Mode.COLOR && this.setting.getValue() == Settings.Background));
    public Setting<Integer> gb_red = this.register(new Setting<Object>("RedBG", Integer.valueOf(20), Integer.valueOf(0), Integer.valueOf(255), v -> this.mode.getValue() == Mode.COLOR && this.setting.getValue() == Settings.Background));
    public Setting<Integer> gb_green = this.register(new Setting<Object>("GreenBG", Integer.valueOf(20), Integer.valueOf(0), Integer.valueOf(255), v -> this.mode.getValue() == Mode.COLOR && this.setting.getValue() == Settings.Background));
    public Setting<Integer> gb_blue = this.register(new Setting<Object>("BlueBG", Integer.valueOf(20), Integer.valueOf(0), Integer.valueOf(255), v -> this.mode.getValue() == Mode.COLOR && this.setting.getValue() == Settings.Background));
    private int color;

    public ClickGui() {
        super("ClickGui", "Opens the ClickGui", Module.Category.CLIENT, true, false, false);
        this.setInstance();
    }

    public static ClickGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGui();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.customFov.getValue().booleanValue()) {
            ClickGui.mc.gameSettings.setOptionFloatValue(GameSettings.Options.FOV, this.fov.getValue().floatValue());
        }
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().getFeature().equals(this)) {
            if (event.getSetting().equals(this.prefix)) {
                OyVey.commandManager.setPrefix(this.prefix.getPlannedValue());
                Command.sendMessage("Prefix set to " + ChatFormatting.DARK_GRAY + OyVey.commandManager.getPrefix());
            }
            OyVey.colorManager.setColor(this.red.getPlannedValue(), this.green.getPlannedValue(), this.blue.getPlannedValue(), this.hoverAlpha.getPlannedValue());
        }
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen((GuiScreen)OyVeyGui.getClickGui());
    }

    @Override
    public void onLoad() {
        OyVey.colorManager.setColor(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.hoverAlpha.getValue());
        OyVey.commandManager.setPrefix(this.prefix.getValue());
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        this.drawBackground();
    }

    public void drawBackground() {
        if (this.mode.getValue() == Mode.COLOR) {
            if (ClickGui.getInstance().isEnabled()) {
                RenderUtil.drawRectangleCorrectly(0, 0, 1920, 1080, ColorUtil.toRGBA(this.gb_red.getValue(), this.gb_green.getValue(), this.gb_blue.getValue(), this.backgroundAlpha.getValue()));
            } else {
                RenderUtil.drawRectangleCorrectly(0, 0, 1920, 1080, ColorUtil.toRGBA(0, 0, 0, 0));
            }
        }
        if (this.mode.getValue() == Mode.NONE) {
            if (ClickGui.getInstance().isEnabled()) {
                RenderUtil.drawRectangleCorrectly(0, 0, 1920, 1080, ColorUtil.toRGBA(this.gb_red.getValue(), this.gb_green.getValue(), this.gb_blue.getValue(), this.backgroundAlpha.getValue()));
            } else {
                RenderUtil.drawRectangleCorrectly(0, 0, 1920, 1080, ColorUtil.toRGBA(0, 0, 0, 0));
            }
        }
    }

    @Override
    public void onTick() {
        if (!(ClickGui.mc.currentScreen instanceof OyVeyGui)) {
            this.disable();
        }
    }

    @Override
    public void onDisable() {
        if (ClickGui.mc.currentScreen instanceof OyVeyGui) {
            Util.mc.displayGuiScreen(null);
        }
    }

    public final int getColor() {
        return this.color;
    }

    public static enum rainbowModeArray {
        Static,
        Up;

    }

    public static enum rainbowMode {
        Static,
        Sideway;

    }

    public static enum Settings {
        Gui,
        Gradient,
        Background;

    }

    public static enum Mode {
        COLOR,
        BLUR,
        NONE;

    }
}

