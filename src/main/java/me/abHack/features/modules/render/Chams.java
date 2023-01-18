/*
 * Decompiled with CFR 0.152.
 */
package me.abHack.features.modules.render;

import me.abHack.features.modules.Module;

public class Chams
extends Module {
    private static Chams INSTANCE = new Chams();

    public Chams() {
        super("Chams", "Player behind rendered wall.", Module.Category.RENDER, false, false, false);
        this.setInstance();
    }

    public static Chams getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Chams();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

