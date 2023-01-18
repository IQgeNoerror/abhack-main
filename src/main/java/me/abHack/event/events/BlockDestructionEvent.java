/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.BlockPos
 */
package me.abHack.event.events;

import me.abHack.event.EventStage;
import net.minecraft.util.math.BlockPos;

public class BlockDestructionEvent
extends EventStage {
    BlockPos nigger;

    public BlockDestructionEvent(BlockPos nigger) {
    }

    public BlockPos getBlockPos() {
        return this.nigger;
    }
}

