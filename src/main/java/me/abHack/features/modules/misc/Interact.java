/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.EnumFacing
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.abHack.features.modules.misc;

import me.abHack.event.events.PacketEvent;
import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Interact
extends Module {
    private static Interact INSTANCE = new Interact();
    public Setting<Boolean> buildHeight = this.register(new Setting<Boolean>("BuildHeight", true));
    public Setting<Boolean> liquid = this.register(new Setting<Boolean>("Liquid", true));
    public Setting<Boolean> portalgui = this.register(new Setting<Boolean>("PortalGui", true));

    public Interact() {
        super("Interact", "ForceInteract", Module.Category.MISC, true, false, false);
        this.setInstance();
    }

    public static Interact getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Interact();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        CPacketPlayerTryUseItemOnBlock packet;
        if (this.buildHeight.getValue().booleanValue() && event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && (packet = (CPacketPlayerTryUseItemOnBlock)event.getPacket()).getPos().getY() >= 255 && packet.getDirection() == EnumFacing.UP) {
            packet.placedBlockDirection = EnumFacing.DOWN;
        }
    }

    @Override
    public void onUpdate() {
        if (Minecraft.getMinecraft().player.inPortal && this.portalgui.getValue().booleanValue()) {
            Minecraft.getMinecraft().player.inPortal = false;
        }
    }
}

