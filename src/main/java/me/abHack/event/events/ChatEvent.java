/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.Cancelable
 */
package me.abHack.event.events;

import me.abHack.event.EventStage;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class ChatEvent
extends EventStage {
    private final String msg;

    public ChatEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }
}

