/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.ResourceLocation
 */
package me.abHack.features.modules.client;

import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import net.minecraft.util.ResourceLocation;

public class Capes
extends Module {
    private static Capes instance;
    public Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.Cool));

    public Capes() {
        super("Capes", "Renders the client's capes", Module.Category.CLIENT, false, false, false);
        instance = this;
    }

    public static Capes getInstance() {
        if (instance == null) {
            instance = new Capes();
        }
        return instance;
    }

    public ResourceLocation Cape() {
        if (this.mode.getValue() == Mode.Cool) {
            return new ResourceLocation("textures/cool.png");
        }
        if (this.mode.getValue() == Mode.Cape) {
            return new ResourceLocation("textures/cape.png");
        }
        if (this.mode.getValue() == Mode.Chicken) {
            return new ResourceLocation("textures/chicken.png");
        }
        if (this.mode.getValue() == Mode.Duck) {
            return new ResourceLocation("textures/duck.png");
        }
        if (this.mode.getValue() == Mode.Galaxy) {
            return new ResourceLocation("textures/galaxy.png");
        }
        if (this.mode.getValue() == Mode.Future) {
            return new ResourceLocation("textures/future.png");
        }
        if (this.mode.getValue() == Mode.Akatsuki) {
            return new ResourceLocation("textures/akatsuki.png");
        }
        if (this.mode.getValue() == Mode.Lunar) {
            return new ResourceLocation("textures/lunar.png");
        }
        if (this.mode.getValue() == Mode.Lightning) {
            return new ResourceLocation("textures/lightning.gif");
        }
        if (this.mode.getValue() == Mode.OF) {
            return new ResourceLocation("textures/of.png");
        }
        if (this.mode.getValue() == Mode.Moon) {
            return new ResourceLocation("textures/moon.png");
        }
        if (this.mode.getValue() == Mode.Enderman) {
            return new ResourceLocation("textures/enderman.png");
        }
        if (this.mode.getValue() == Mode.Panda) {
            return new ResourceLocation("textures/panda.png");
        }
        if (this.mode.getValue() == Mode.Scenery) {
            return new ResourceLocation("textures/scenery.png");
        }
        if (this.mode.getValue() == Mode.Heart) {
            return new ResourceLocation("textures/heart.png");
        }
        if (this.mode.getValue() == Mode.Purple) {
            return new ResourceLocation("textures/purple.png");
        }
        if (this.mode.getValue() == Mode.Sad) {
            return new ResourceLocation("textures/sad.png");
        }
        if (this.mode.getValue() == Mode.Shawchi) {
            return new ResourceLocation("textures/shawchi.png");
        }
        if (this.mode.getValue() == Mode.Sunset) {
            return new ResourceLocation("textures/sunset.png");
        }
        if (this.mode.getValue() == Mode.Steam) {
            return new ResourceLocation("textures/steam.png");
        }
        if (this.mode.getValue() == Mode.Sun) {
            return new ResourceLocation("textures/sun.png");
        }
        if (this.mode.getValue() == Mode.Reimu) {
            return new ResourceLocation("textures/reimu.png");
        }
        if (this.mode.getValue() == Mode.Uwu) {
            return new ResourceLocation("textures/uwu.png");
        }
        if (this.mode.getValue() == Mode.Vape) {
            return new ResourceLocation("textures/vape.png");
        }
        if (this.mode.getValue() == Mode.Sad_two) {
            return new ResourceLocation("textures/sad_two.png");
        }
        if (this.mode.getValue() == Mode.Black) {
            return new ResourceLocation("textures/black.png");
        }
        return null;
    }

    public static enum Mode {
        Cool,
        Cape,
        Chicken,
        Duck,
        Galaxy,
        Future,
        Akatsuki,
        Lunar,
        Lightning,
        OF,
        Moon,
        Enderman,
        Panda,
        Scenery,
        Heart,
        Purple,
        Sad,
        Shawchi,
        Sunset,
        Steam,
        Sun,
        Reimu,
        Uwu,
        Vape,
        Sad_two,
        Black;

    }
}

