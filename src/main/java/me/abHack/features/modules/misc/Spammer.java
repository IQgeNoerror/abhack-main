/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 */
package me.abHack.features.modules.misc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import me.abHack.OyVey;
import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import me.abHack.util.FileUtil;
import me.abHack.util.Timer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;

public class Spammer
extends Module {
    private static final String fileName = "ab-Hack/Spammer.txt";
    private static final String defaultMessage = "Welcome to use abHack";
    private static final List<String> spamMessages = new ArrayList<String>();
    private static final Random rnd = new Random();
    private final Setting<Boolean> escoff = this.register(new Setting<Boolean>("EscOff", true));
    private final Timer timer = new Timer();
    private final List<String> sendPlayers = new ArrayList<String>();
    public Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.FILE));
    public Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 5, 0, 20));
    public Setting<String> custom = this.register(new Setting<Object>("Custom", "String", v -> this.mode.getValue() == Mode.MSG));
    public Setting<String> msgTarget = this.register(new Setting<Object>("MsgTarget", "Target...", v -> this.mode.getValue() == Mode.MSG));
    public Setting<Boolean> greentext = this.register(new Setting<Object>("Greentext", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.FILE));
    public Setting<Boolean> random = this.register(new Setting<Object>("Random", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.FILE));
    public Setting<Boolean> loadFile = this.register(new Setting<Object>("LoadFile", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.FILE));

    public Spammer() {
        super("Spammer", "Spams stuff.", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onEnable() {
        if (Spammer.fullNullCheck()) {
            this.disable();
            return;
        }
        this.readSpamFile();
    }

    @Override
    public void onLogin() {
        if (this.escoff.getValue().booleanValue() && OyVey.moduleManager.isModuleEnabled("Spammer")) {
            this.disable();
        }
    }

    @Override
    public void onLogout() {
        if (this.escoff.getValue().booleanValue() && OyVey.moduleManager.isModuleEnabled("Spammer")) {
            this.disable();
        }
    }

    @Override
    public void onDisable() {
        spamMessages.clear();
        this.timer.reset();
    }

    @Override
    public void onUpdate() {
        if (Spammer.fullNullCheck()) {
            this.disable();
            return;
        }
        if (this.loadFile.getValue().booleanValue()) {
            this.readSpamFile();
            this.loadFile.setValue(false);
        }
        if (this.timer.passedS(this.delay.getValue().intValue())) {
            if (this.mode.getValue() == Mode.MSG) {
                String msg = this.custom.getValue();
                msg = "/msg " + this.msgTarget.getValue() + " " + msg;
                Spammer.mc.player.sendChatMessage(msg);
            } else if (spamMessages.size() > 0) {
                String messageOut;
                if (this.random.getValue().booleanValue()) {
                    int index = rnd.nextInt(spamMessages.size());
                    messageOut = spamMessages.get(index);
                    spamMessages.remove(index);
                } else {
                    messageOut = spamMessages.get(0);
                    spamMessages.remove(0);
                }
                spamMessages.add(messageOut);
                if (this.greentext.getValue().booleanValue()) {
                    messageOut = "> " + messageOut;
                }
                Spammer.mc.player.connection.sendPacket((Packet)new CPacketChatMessage(messageOut.replaceAll("\u00a7", "")));
            }
            this.timer.reset();
        }
    }

    private void readSpamFile() {
        List<String> fileInput = FileUtil.readTextFileAllLines(fileName);
        Iterator<String> i = fileInput.iterator();
        spamMessages.clear();
        while (i.hasNext()) {
            String s = i.next();
            if (s.replaceAll("\\s", "").isEmpty()) continue;
            spamMessages.add(s);
        }
        if (spamMessages.size() == 0) {
            spamMessages.add(defaultMessage);
        }
    }

    public static enum Mode {
        FILE,
        MSG;

    }
}

