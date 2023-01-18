/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EnumPlayerModelParts
 */
package me.abHack.features.modules.misc;

import java.util.Random;
import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class SkinFlicker
extends Module {
    private static final EnumPlayerModelParts[] PARTS_HORIZONTAL = new EnumPlayerModelParts[]{EnumPlayerModelParts.LEFT_SLEEVE, EnumPlayerModelParts.JACKET, EnumPlayerModelParts.HAT, EnumPlayerModelParts.LEFT_PANTS_LEG, EnumPlayerModelParts.RIGHT_PANTS_LEG, EnumPlayerModelParts.RIGHT_SLEEVE};
    private static final EnumPlayerModelParts[] PARTS_VERTICAL = new EnumPlayerModelParts[]{EnumPlayerModelParts.HAT, EnumPlayerModelParts.JACKET, EnumPlayerModelParts.LEFT_SLEEVE, EnumPlayerModelParts.RIGHT_SLEEVE, EnumPlayerModelParts.LEFT_PANTS_LEG, EnumPlayerModelParts.RIGHT_PANTS_LEG};
    private final Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.HORIZONTAL));
    private final Setting<Integer> slowness = this.register(new Setting<Integer>("Slowness", 2, 1, 10));
    private final Random r = new Random();
    private final int len = EnumPlayerModelParts.values().length;

    public SkinFlicker() {
        super("SkinFlicker", "Dynamic skin.", Module.Category.MISC, false, false, false);
    }

    @Override
    public void onTick() {
        if (SkinFlicker.mc.player == null) {
            return;
        }
        switch (this.mode.getValue()) {
            case RANDOM: {
                if (SkinFlicker.mc.player.ticksExisted % this.slowness.getValue() != 0) {
                    return;
                }
                SkinFlicker.mc.gameSettings.switchModelPartEnabled(EnumPlayerModelParts.values()[this.r.nextInt(this.len)]);
                break;
            }
            case VERTICAL: 
            case HORIZONTAL: {
                int i = SkinFlicker.mc.player.ticksExisted / this.slowness.getValue() % (PARTS_HORIZONTAL.length * 2);
                boolean on = false;
                if (i >= PARTS_HORIZONTAL.length) {
                    on = true;
                    i -= PARTS_HORIZONTAL.length;
                }
                SkinFlicker.mc.gameSettings.setModelPartEnabled(this.mode.getValue().equals("VERTICAL") ? PARTS_VERTICAL[i] : PARTS_HORIZONTAL[i], on);
            }
        }
    }

    public static enum Mode {
        HORIZONTAL,
        VERTICAL,
        RANDOM;

    }
}

