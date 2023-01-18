/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.Cancelable
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package me.abHack.event.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class Packet
extends Event {
    private Object packet;
    private Type type;

    public Packet(Object packet, Type type) {
        this.packet = packet;
        this.type = type;
    }

    public Object getPacket() {
        return this.packet;
    }

    public void setPacket(Object packet) {
        this.packet = packet;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public static enum Type {
        INCOMING,
        OUTGOING;

    }
}

