// 
// Decompiled by Procyon v0.5.36
// 

package me.abHack.util;

import net.minecraft.client.Minecraft;
import me.abHack.OyVey;

public class HudUtil implements Util
{
    public static String getPingSatus() {
        final String line = "";
        final int ping = OyVey.serverManager.getPing();
        return line + " " + ping;
    }
    
    public static String getTpsStatus() {
        final String line = "";
        final double tps = Math.ceil(OyVey.serverManager.getTPS());
        return line + " " + tps;
    }
    
    public static String getFpsStatus() {
        final String line = "";
        final int fps = Minecraft.getDebugFPS();
        return line + " " + fps;
    }
}
