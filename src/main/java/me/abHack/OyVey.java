/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.Util
 *  net.minecraft.util.Util$EnumOS
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.Mod
 *  net.minecraftforge.fml.common.Mod$EventHandler
 *  net.minecraftforge.fml.common.Mod$Instance
 *  net.minecraftforge.fml.common.event.FMLInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPreInitializationEvent
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.opengl.Display
 */
package me.abHack;

import easiervillagertrading.ConfigurationHandler;
import easiervillagertrading.OpenTradeEventHandler;
import java.io.InputStream;
import java.nio.ByteBuffer;
import me.abHack.manager.ColorManager;
import me.abHack.manager.CommandManager;
import me.abHack.manager.ConfigManager;
import me.abHack.manager.EventManager;
import me.abHack.manager.FileManager;
import me.abHack.manager.FriendManager;
import me.abHack.manager.HoleManager;
import me.abHack.manager.InventoryManager;
import me.abHack.manager.ModuleManager;
import me.abHack.manager.PacketManager;
import me.abHack.manager.PositionManager;
import me.abHack.manager.PotionManager;
import me.abHack.manager.ReloadManager;
import me.abHack.manager.RotationManager;
import me.abHack.manager.SafetyManager;
import me.abHack.manager.ServerManager;
import me.abHack.manager.SpeedManager;
import me.abHack.manager.TargetManager;
import me.abHack.manager.TextManager;
import me.abHack.manager.VerificationManager;
import me.abHack.util.IconUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

@Mod(modid="ab", name="ab-Hack", version="0.0.1")
public class OyVey {
    public static final String MODID = "ab";
    public static final String MODNAME = "ab-Hack";
    public static final String MODVER = "0.0.1";
    public static final Logger LOGGER = LogManager.getLogger((String)"ab-Hack");
    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static ModuleManager moduleManager;
    public static PacketManager packetManager;
    public static ColorManager colorManager;
    public static HoleManager holeManager;
    public static InventoryManager inventoryManager;
    public static PotionManager potionManager;
    public static RotationManager rotationManager;
    public static PositionManager positionManager;
    public static SpeedManager speedManager;
    public static ReloadManager reloadManager;
    public static FileManager fileManager;
    public static ConfigManager configManager;
    public static ServerManager serverManager;
    public static EventManager eventManager;
    public static TextManager textManager;
    public static SafetyManager safetyManager;
    public static TargetManager targetManager;
    @Mod.Instance
    public static OyVey INSTANCE;
    private static boolean unloaded;

    public static void load() {
        LOGGER.info("\n\nLoading ab-Hack by Steve_ab");
        unloaded = false;
        if (reloadManager != null) {
            reloadManager.unload();
            reloadManager = null;
        }
        textManager = new TextManager();
        commandManager = new CommandManager();
        friendManager = new FriendManager();
        moduleManager = new ModuleManager();
        rotationManager = new RotationManager();
        packetManager = new PacketManager();
        eventManager = new EventManager();
        speedManager = new SpeedManager();
        potionManager = new PotionManager();
        inventoryManager = new InventoryManager();
        serverManager = new ServerManager();
        fileManager = new FileManager();
        colorManager = new ColorManager();
        positionManager = new PositionManager();
        configManager = new ConfigManager();
        holeManager = new HoleManager();
        safetyManager = new SafetyManager();
        targetManager = new TargetManager();
        LOGGER.info("Managers loaded.");
        moduleManager.init();
        LOGGER.info("Modules loaded.");
        configManager.init();
        eventManager.init();
        LOGGER.info("EventManager loaded.");
        textManager.init(true);
        moduleManager.onLoad();
        LOGGER.info("abHack successfully loaded!\n");
    }

    public static void unload(boolean unload) {
        LOGGER.info("\n\nUnloading ab-Hack by Steve_ab");
        if (unload) {
            reloadManager = new ReloadManager();
            reloadManager.init(commandManager != null ? commandManager.getPrefix() : ".");
        }
        OyVey.onUnload();
        eventManager = null;
        friendManager = null;
        speedManager = null;
        holeManager = null;
        positionManager = null;
        rotationManager = null;
        configManager = null;
        commandManager = null;
        colorManager = null;
        serverManager = null;
        fileManager = null;
        potionManager = null;
        inventoryManager = null;
        moduleManager = null;
        textManager = null;
        safetyManager = null;
        targetManager = null;
        LOGGER.info("ab-Hack unloaded!\n");
    }

    public static void reload() {
        OyVey.unload(false);
        OyVey.load();
    }

    public static void onUnload() {
        if (!unloaded) {
            eventManager.onUnload();
            moduleManager.onUnload();
            configManager.saveConfig(OyVey.configManager.config.replaceFirst("ab-Hack/", ""));
            moduleManager.onUnloadPost();
            unloaded = true;
        }
    }

    public static void setWindowIcon() {
        if (Util.getOSType() != Util.EnumOS.OSX) {
            try (InputStream inputStream16x = Minecraft.class.getResourceAsStream("/assets/minecraft/textures/steve16x.png");
                 InputStream inputStream32x = Minecraft.class.getResourceAsStream("/assets/minecraft/textures/steve32x.png");){
                ByteBuffer[] icons = new ByteBuffer[]{IconUtil.INSTANCE.readImageToBuffer(inputStream16x), IconUtil.INSTANCE.readImageToBuffer(inputStream32x)};
                Display.setIcon((ByteBuffer[])icons);
            }
            catch (Exception e) {
                LOGGER.error("Couldn't set Windows Icon", (Throwable)e);
            }
        }
    }

    private void setWindowsIcon() {
        OyVey.setWindowIcon();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigurationHandler confHandler = ConfigurationHandler.getInstance();
        confHandler.load(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register((Object)confHandler);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        VerificationManager.hwidCheck();
        Display.setTitle((String)"ab-Hack 0.0.1");
        OyVey.load();
        this.setWindowsIcon();
        MinecraftForge.EVENT_BUS.register((Object)OpenTradeEventHandler.getInstance());
    }

    static {
        unloaded = false;
    }
}

