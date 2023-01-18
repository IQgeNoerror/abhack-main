/*
 * Decompiled with CFR 0.152.
 */
package me.abHack.features.modules.player;

import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;

public class tp
extends Module {
    private final Setting<String> xcoord = this.register(new Setting<String>("X", "100"));
    private final Setting<String> zcoord = this.register(new Setting<String>("Z", "100"));
    private final Setting<Integer> tickblock = this.register(new Setting<Integer>("Tick", 10, 1, 10));

    public tp() {
        super("tp", "tp coord", Module.Category.PLAYER, true, false, false);
    }

    @Override
    public void onTick() {
        if (tp.mc.player.posY < 180.0) {
            tp.mc.player.setPosition(tp.mc.player.posX, tp.mc.player.posY + 5.0, tp.mc.player.posZ);
        }
        if (tp.mc.player.posX <= (double)Integer.parseInt(this.xcoord.getValue()) && Integer.parseInt(this.xcoord.getValue()) >= 0) {
            tp.mc.player.setPosition(tp.mc.player.posX + (double)this.tickblock.getValue().intValue(), tp.mc.player.posY, tp.mc.player.posZ);
        } else if (tp.mc.player.posX >= (double)Integer.parseInt(this.xcoord.getValue()) && (Integer.parseInt(this.xcoord.getValue()) <= 0 || Integer.parseInt(this.xcoord.getValue()) >= 0)) {
            tp.mc.player.setPosition(tp.mc.player.posX - (double)this.tickblock.getValue().intValue(), tp.mc.player.posY, tp.mc.player.posZ);
        } else if (tp.mc.player.posX <= (double)Integer.parseInt(this.xcoord.getValue()) && Integer.parseInt(this.xcoord.getValue()) <= 0) {
            tp.mc.player.setPosition(tp.mc.player.posX + (double)this.tickblock.getValue().intValue(), tp.mc.player.posY, tp.mc.player.posZ);
        }
        if (tp.mc.player.posX + 10.0 >= (double)Integer.parseInt(this.xcoord.getValue()) && tp.mc.player.posX - 10.0 <= (double)Integer.parseInt(this.xcoord.getValue())) {
            if (tp.mc.player.posZ <= (double)Integer.parseInt(this.zcoord.getValue()) && Integer.parseInt(this.zcoord.getValue()) >= 0) {
                tp.mc.player.setPosition(tp.mc.player.posX, tp.mc.player.posY, tp.mc.player.posZ + (double)this.tickblock.getValue().intValue());
            } else if (tp.mc.player.posZ >= (double)Integer.parseInt(this.zcoord.getValue()) && (Integer.parseInt(this.zcoord.getValue()) <= 0 || Integer.parseInt(this.zcoord.getValue()) >= 0)) {
                tp.mc.player.setPosition(tp.mc.player.posX, tp.mc.player.posY, tp.mc.player.posZ - (double)this.tickblock.getValue().intValue());
            } else if (tp.mc.player.posZ <= (double)Integer.parseInt(this.zcoord.getValue()) && Integer.parseInt(this.zcoord.getValue()) <= 0) {
                tp.mc.player.setPosition(tp.mc.player.posX, tp.mc.player.posY, tp.mc.player.posZ + (double)this.tickblock.getValue().intValue());
            }
        }
        tp.mc.player.setPosition(tp.mc.player.posX, tp.mc.player.posY - 1.0, tp.mc.player.posZ);
    }
}

