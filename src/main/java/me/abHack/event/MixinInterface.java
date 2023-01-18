/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package me.abHack.event;

import net.minecraft.client.Minecraft;

public interface MixinInterface {
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static final boolean nullCheck = MixinInterface.mc.player == null || MixinInterface.mc.world == null;
}

