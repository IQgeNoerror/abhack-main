/*
 * Decompiled with CFR 0.152.
 */
package me.abHack.event.events;

import me.abHack.event.EventStage;

public class Render3DEvent
extends EventStage {
    private final float partialTicks;

    public Render3DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}

