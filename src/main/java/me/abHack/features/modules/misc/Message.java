/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  org.apache.commons.lang3.RandomStringUtils
 */
package me.abHack.features.modules.misc;

import me.abHack.OyVey;
import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import me.abHack.util.Timer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import org.apache.commons.lang3.RandomStringUtils;

public class Message
extends Module {
    private final Timer timer = new Timer();
    private final Setting<String> custom = this.register(new Setting<String>("Custom", "/kit ab "));
    private final Setting<Integer> random = this.register(new Setting<Integer>("Random", 0, 0, 20));
    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 5, 0, 20));
    private final Setting<Boolean> toggle = this.register(new Setting<Boolean>("Toggle", true));
    private final Setting<Boolean> escoff = this.register(new Setting<Boolean>("EscOff", true));

    public Message() {
        super("Message", "Message", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onLogout() {
        if (this.escoff.getValue().booleanValue() && OyVey.moduleManager.isModuleEnabled("Message")) {
            this.disable();
        }
    }

    @Override
    public void onLogin() {
        if (this.escoff.getValue().booleanValue() && OyVey.moduleManager.isModuleEnabled("Message")) {
            this.disable();
        }
    }

    @Override
    public void onUpdate() {
        if (Message.fullNullCheck()) {
            return;
        }
        if (this.timer.passedS(this.delay.getValue().intValue())) {
            Message.mc.player.connection.sendPacket((Packet)new CPacketChatMessage(this.custom.getValue() + RandomStringUtils.randomAlphanumeric((int)this.random.getValue())));
            this.timer.reset();
        }
        if (this.toggle.getValue().booleanValue()) {
            this.toggle();
        }
    }
}

