/*
 * Decompiled with CFR 0.152.
 */
package me.abHack.features.modules.player;

import me.abHack.features.modules.Module;

public class PacketEat
extends Module {
    private static PacketEat INSTANCE = new PacketEat();

    public PacketEat() {
        super("PacketEat", "PacketEat", Module.Category.PLAYER, true, false, false);
        this.setInstance();
    }

    public static PacketEat getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PacketEat();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

