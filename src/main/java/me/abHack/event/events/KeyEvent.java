/*
 * Decompiled with CFR 0.152.
 */
package me.abHack.event.events;

import me.abHack.event.EventStage;

public class KeyEvent
extends EventStage {
    private final int key;

    public KeyEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }
}

