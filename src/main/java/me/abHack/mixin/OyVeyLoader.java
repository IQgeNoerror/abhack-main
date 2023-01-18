/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.relauncher.IFMLLoadingPlugin
 */
package me.abHack.mixin;

import java.util.Map;
import me.abHack.OyVey;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

public class OyVeyLoader
implements IFMLLoadingPlugin {
    private static boolean isObfuscatedEnvironment = false;

    public OyVeyLoader() {
        OyVey.LOGGER.info("  Loading mixins by abHack Cracked by 0zhiGod");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.abHack.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        OyVey.LOGGER.info(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
    }

    public String[] getASMTransformerClass() {
        return new String[0];
    }

    public String getModContainerClass() {
        return null;
    }

    public String getSetupClass() {
        return null;
    }

    public void injectData(Map<String, Object> data) {
        isObfuscatedEnvironment = (Boolean)data.get("runtimeDeobfuscationEnabled");
    }

    public String getAccessTransformerClass() {
        return null;
    }
}

