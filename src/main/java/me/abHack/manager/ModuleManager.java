/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.EventBus
 *  org.lwjgl.input.Keyboard
 */
package me.abHack.manager;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import me.abHack.OyVey;
import me.abHack.event.events.Render2DEvent;
import me.abHack.event.events.Render3DEvent;
import me.abHack.features.Feature;
import me.abHack.features.gui.OyVeyGui;
import me.abHack.features.modules.Module;
import me.abHack.features.modules.client.Capes;
import me.abHack.features.modules.client.ClickGui;
import me.abHack.features.modules.client.FontMod;
import me.abHack.features.modules.client.HUD;
import me.abHack.features.modules.client.WaterMark;
import me.abHack.features.modules.combat.AntiBurrow;
import me.abHack.features.modules.combat.AntiCev;
import me.abHack.features.modules.combat.AntiCity;
import me.abHack.features.modules.combat.AntiPiston;
import me.abHack.features.modules.combat.AutoArmor;
import me.abHack.features.modules.combat.AutoCev;
import me.abHack.features.modules.combat.AutoCity;
import me.abHack.features.modules.combat.AutoCrystal;
import me.abHack.features.modules.combat.AutoTotem;
import me.abHack.features.modules.combat.AutoTrap;
import me.abHack.features.modules.combat.AutoWeb;
import me.abHack.features.modules.combat.BedAura;
import me.abHack.features.modules.combat.BowAim;
import me.abHack.features.modules.combat.Criticals;
import me.abHack.features.modules.combat.FastBow;
import me.abHack.features.modules.combat.Flatten;
import me.abHack.features.modules.combat.HoleFiller;
import me.abHack.features.modules.combat.Killaura;
import me.abHack.features.modules.combat.Offhand;
import me.abHack.features.modules.combat.PistonCrystal;
import me.abHack.features.modules.combat.Quiver;
import me.abHack.features.modules.combat.Selftrap;
import me.abHack.features.modules.combat.Surround;
import me.abHack.features.modules.combat.TrapHead;
import me.abHack.features.modules.combat.TrapSelf;
import me.abHack.features.modules.misc.AntiCrash;
import me.abHack.features.modules.misc.AntiPackets;
import me.abHack.features.modules.misc.AntiWeb;
import me.abHack.features.modules.misc.AutoReconnect;
import me.abHack.features.modules.misc.AutoRespawn;
import me.abHack.features.modules.misc.BowGod;
import me.abHack.features.modules.misc.ChatSuffix;
import me.abHack.features.modules.misc.ChestStealer;
import me.abHack.features.modules.misc.CrashSeries;
import me.abHack.features.modules.misc.Gamemode;
import me.abHack.features.modules.misc.Interact;
import me.abHack.features.modules.misc.Message;
import me.abHack.features.modules.misc.MiddleFriend;
import me.abHack.features.modules.misc.NoEntityTrace;
import me.abHack.features.modules.misc.NoRotate;
import me.abHack.features.modules.misc.NoteBot;
import me.abHack.features.modules.misc.OffhandCrash;
import me.abHack.features.modules.misc.PopCounter;
import me.abHack.features.modules.misc.ShulkerViewer;
import me.abHack.features.modules.misc.SkinFlicker;
import me.abHack.features.modules.misc.Spammer;
import me.abHack.features.modules.misc.Timer;
import me.abHack.features.modules.misc.VillagerNotifier;
import me.abHack.features.modules.movement.AirJump;
import me.abHack.features.modules.movement.AntiLevitate;
import me.abHack.features.modules.movement.AutoWalk;
import me.abHack.features.modules.movement.BoatFly;
import me.abHack.features.modules.movement.ElytraFlight;
import me.abHack.features.modules.movement.EntityControl;
import me.abHack.features.modules.movement.Flight;
import me.abHack.features.modules.movement.HoleTP;
import me.abHack.features.modules.movement.NoFall;
import me.abHack.features.modules.movement.Phase;
import me.abHack.features.modules.movement.PlayerTweaks;
import me.abHack.features.modules.movement.ReverseStep;
import me.abHack.features.modules.movement.Scaffold;
import me.abHack.features.modules.movement.Sprint;
import me.abHack.features.modules.movement.Step;
import me.abHack.features.modules.movement.Strafe;
import me.abHack.features.modules.player.Anti32k;
import me.abHack.features.modules.player.AntiContainer;
import me.abHack.features.modules.player.AntiHunger;
import me.abHack.features.modules.player.AntiShulkerBox;
import me.abHack.features.modules.player.AutoBuilder;
import me.abHack.features.modules.player.AutoDupe;
import me.abHack.features.modules.player.AutoEat;
import me.abHack.features.modules.player.AutoXP;
import me.abHack.features.modules.player.Blink;
import me.abHack.features.modules.player.BlockTweaks;
import me.abHack.features.modules.player.Burrow;
import me.abHack.features.modules.player.FakePlayer;
import me.abHack.features.modules.player.FastPlace;
import me.abHack.features.modules.player.InstantMine;
import me.abHack.features.modules.player.MultiTask;
import me.abHack.features.modules.player.PacketEat;
import me.abHack.features.modules.player.PortalGodMode;
import me.abHack.features.modules.player.Reach;
import me.abHack.features.modules.player.Replenish;
import me.abHack.features.modules.player.Speedmine;
import me.abHack.features.modules.player.Swing;
import me.abHack.features.modules.player.TickShift;
import me.abHack.features.modules.player.TpsSync;
import me.abHack.features.modules.player.XCarry;
import me.abHack.features.modules.player.tp;
import me.abHack.features.modules.render.BlockHighlight;
import me.abHack.features.modules.render.BreadCrumbs;
import me.abHack.features.modules.render.BreakingESP;
import me.abHack.features.modules.render.BurrowESP;
import me.abHack.features.modules.render.CameraClip;
import me.abHack.features.modules.render.Chams;
import me.abHack.features.modules.render.ChestESP;
import me.abHack.features.modules.render.CityRender;
import me.abHack.features.modules.render.CrystalScale;
import me.abHack.features.modules.render.ESP;
import me.abHack.features.modules.render.ExplosionChams;
import me.abHack.features.modules.render.Fullbright;
import me.abHack.features.modules.render.HoleESP;
import me.abHack.features.modules.render.ItemPhysics;
import me.abHack.features.modules.render.LogoutSpots;
import me.abHack.features.modules.render.NameTags;
import me.abHack.features.modules.render.NoRender;
import me.abHack.features.modules.render.Particles;
import me.abHack.features.modules.render.PopChams;
import me.abHack.features.modules.render.PortalESP;
import me.abHack.features.modules.render.Ranges;
import me.abHack.features.modules.render.Skeleton;
import me.abHack.features.modules.render.SkyColor;
import me.abHack.features.modules.render.Target;
import me.abHack.features.modules.render.Trajectories;
import me.abHack.features.modules.render.ViewModel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.lwjgl.input.Keyboard;

public class ModuleManager
extends Feature {
    public ArrayList<Module> modules = new ArrayList();
    public List<Module> sortedModules = new ArrayList<Module>();
    public List<String> sortedModulesABC = new ArrayList<String>();
    public Animation animationThread;

    public void init() {
        this.modules.add(new ClickGui());
        this.modules.add(new FontMod());
        this.modules.add(new HUD());
        this.modules.add(new Capes());
        this.modules.add(new WaterMark());
        this.modules.add(new Offhand());
        this.modules.add(new Surround());
        this.modules.add(new AutoTrap());
        this.modules.add(new AutoCity());
        this.modules.add(new AutoCev());
        this.modules.add(new AutoTotem());
        this.modules.add(new AutoWeb());
        this.modules.add(new AntiBurrow());
        this.modules.add(new AntiPiston());
        this.modules.add(new AntiCity());
        this.modules.add(new AutoCrystal());
        this.modules.add(new BedAura());
        this.modules.add(new Killaura());
        this.modules.add(new Criticals());
        this.modules.add(new HoleFiller());
        this.modules.add(new AutoArmor());
        this.modules.add(new Selftrap());
        this.modules.add(new FastBow());
        this.modules.add(new Flatten());
        this.modules.add(new BowAim());
        this.modules.add(new Quiver());
        this.modules.add(new AntiCev());
        this.modules.add(new TrapHead());
        this.modules.add(new TrapSelf());
        this.modules.add(new PistonCrystal());
        this.modules.add(new Timer());
        this.modules.add(new ShulkerViewer());
        this.modules.add(new Interact());
        this.modules.add(new MiddleFriend());
        this.modules.add(new PopCounter());
        this.modules.add(new OffhandCrash());
        this.modules.add(new Message());
        this.modules.add(new AntiPackets());
        this.modules.add(new NoEntityTrace());
        this.modules.add(new ChatSuffix());
        this.modules.add(new AutoRespawn());
        this.modules.add(new BowGod());
        this.modules.add(new AntiWeb());
        this.modules.add(new ChestStealer());
        this.modules.add(new CrashSeries());
        this.modules.add(new NoRotate());
        this.modules.add(new Gamemode());
        this.modules.add(new AntiCrash());
        this.modules.add(new Spammer());
        this.modules.add(new NoteBot());
        this.modules.add(new SkinFlicker());
        this.modules.add(new AutoReconnect());
        this.modules.add(new VillagerNotifier());
        this.modules.add(new BlockHighlight());
        this.modules.add(new BreakingESP());
        this.modules.add(new HoleESP());
        this.modules.add(new Skeleton());
        this.modules.add(new Trajectories());
        this.modules.add(new NoRender());
        this.modules.add(new NameTags());
        this.modules.add(new Particles());
        this.modules.add(new ESP());
        this.modules.add(new ItemPhysics());
        this.modules.add(new Fullbright());
        this.modules.add(new CameraClip());
        this.modules.add(new LogoutSpots());
        this.modules.add(new PopChams());
        this.modules.add(new BreadCrumbs());
        this.modules.add(new SkyColor());
        this.modules.add(new ViewModel());
        this.modules.add(new ChestESP());
        this.modules.add(new Chams());
        this.modules.add(new PortalESP());
        this.modules.add(new BurrowESP());
        this.modules.add(new CityRender());
        this.modules.add(new Target());
        this.modules.add(new Ranges());
        this.modules.add(new ExplosionChams());
        this.modules.add(new CrystalScale());
        this.modules.add(new Replenish());
        this.modules.add(new FakePlayer());
        this.modules.add(new TpsSync());
        this.modules.add(new MultiTask());
        this.modules.add(new Speedmine());
        this.modules.add(new FastPlace());
        this.modules.add(new InstantMine());
        this.modules.add(new Phase());
        this.modules.add(new Reach());
        this.modules.add(new PortalGodMode());
        this.modules.add(new AntiHunger());
        this.modules.add(new XCarry());
        this.modules.add(new Anti32k());
        this.modules.add(new AutoXP());
        this.modules.add(new AutoEat());
        this.modules.add(new Blink());
        this.modules.add(new Burrow());
        this.modules.add(new PacketEat());
        this.modules.add(new TickShift());
        this.modules.add(new Swing());
        this.modules.add(new tp());
        this.modules.add(new BlockTweaks());
        this.modules.add(new AntiContainer());
        this.modules.add(new AntiShulkerBox());
        this.modules.add(new AutoDupe());
        this.modules.add(new AutoBuilder());
        this.modules.add(new Strafe());
        this.modules.add(new Step());
        this.modules.add(new Flight());
        this.modules.add(new Scaffold());
        this.modules.add(new ReverseStep());
        this.modules.add(new AntiLevitate());
        this.modules.add(new AutoWalk());
        this.modules.add(new BoatFly());
        this.modules.add(new ElytraFlight());
        this.modules.add(new EntityControl());
        this.modules.add(new NoFall());
        this.modules.add(new PlayerTweaks());
        this.modules.add(new Sprint());
        this.modules.add(new HoleTP());
        this.modules.add(new AirJump());
    }

    public Module getModuleByName(String name) {
        for (Module module : this.modules) {
            if (!module.getName().equalsIgnoreCase(name)) continue;
            return module;
        }
        return null;
    }

    public <T extends Module> T getModuleByClass(Class<T> clazz) {
        for (Module module : this.modules) {
            if (!clazz.isInstance(module)) continue;
            return (T)module;
        }
        return null;
    }

    public void enableModule(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.disable();
        }
    }

    public void enableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.disable();
        }
    }

    public boolean isModuleEnabled(String name) {
        Module module = this.getModuleByName(name);
        return module != null && module.isOn();
    }

    public boolean isModuleEnabled(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        return module != null && module.isOn();
    }

    public Module getModuleByDisplayName(String displayName) {
        for (Module module : this.modules) {
            if (!module.getDisplayName().equalsIgnoreCase(displayName)) continue;
            return module;
        }
        return null;
    }

    public ArrayList<Module> getEnabledModules() {
        ArrayList<Module> enabledModules = new ArrayList<Module>();
        for (Module module : this.modules) {
            if (!module.isEnabled()) continue;
            enabledModules.add(module);
        }
        return enabledModules;
    }

    public ArrayList<String> getEnabledModulesName() {
        ArrayList<String> enabledModules = new ArrayList<String>();
        for (Module module : this.modules) {
            if (!module.isEnabled() || !module.isDrawn()) continue;
            enabledModules.add(module.getFullArrayString());
        }
        return enabledModules;
    }

    public ArrayList<Module> getModulesByCategory(Module.Category category) {
        ArrayList<Module> modulesCategory = new ArrayList<Module>();
        this.modules.forEach(module -> {
            if (module.getCategory() == category) {
                modulesCategory.add((Module)module);
            }
        });
        return modulesCategory;
    }

    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }

    public void onLoad() {
        this.modules.stream().filter(Module::listening).forEach(arg_0 -> ((EventBus)MinecraftForge.EVENT_BUS).register(arg_0));
        this.modules.forEach(Module::onLoad);
    }

    public void onUpdate() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onUpdate);
    }

    public void onTick() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onTick);
    }

    public void onRender2D(Render2DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender2D(event));
    }

    public void onRender3D(Render3DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender3D(event));
    }

    public void sortModules(boolean reverse) {
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(module -> this.renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1))).collect(Collectors.toList());
    }

    public void sortModulesABC() {
        this.sortedModulesABC = new ArrayList<String>(this.getEnabledModulesName());
        this.sortedModulesABC.sort(String.CASE_INSENSITIVE_ORDER);
    }

    public void onLogout() {
        this.modules.forEach(Module::onLogout);
    }

    public void onLogin() {
        this.modules.forEach(Module::onLogin);
    }

    public void onUnload() {
        this.modules.forEach(arg_0 -> ((EventBus)MinecraftForge.EVENT_BUS).unregister(arg_0));
        this.modules.forEach(Module::onUnload);
    }

    public void onUnloadPost() {
        for (Module module : this.modules) {
            module.enabled.setValue(false);
        }
    }

    public void onKeyPressed(int eventKey) {
        if (eventKey == 0 || !Keyboard.getEventKeyState() || ModuleManager.mc.currentScreen instanceof OyVeyGui) {
            return;
        }
        this.modules.forEach(module -> {
            if (module.getBind().getKey() == eventKey) {
                module.toggle();
            }
        });
    }

    private class Animation
    extends Thread {
        public Module module;
        public float offset;
        public float vOffset;
        ScheduledExecutorService service;

        public Animation() {
            super("Animation");
            this.service = Executors.newSingleThreadScheduledExecutor();
        }

        @Override
        public void run() {
            if (HUD.getInstance().renderingMode.getValue() == HUD.RenderingMode.Length) {
                for (Module module : ModuleManager.this.sortedModules) {
                    String text = module.getDisplayName() + ChatFormatting.GRAY + (module.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]" : "");
                    module.offset = (float)ModuleManager.this.renderer.getStringWidth(text) / 1.0f;
                    module.vOffset = (float)ModuleManager.this.renderer.getFontHeight() / 1.0f;
                    if (module.isEnabled()) {
                        // empty if block
                    }
                    if (!module.isDisabled()) continue;
                }
            } else {
                for (String e : ModuleManager.this.sortedModulesABC) {
                    Module module = OyVey.moduleManager.getModuleByName(e);
                    String text = module.getDisplayName() + ChatFormatting.GRAY + (module.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]" : "");
                    module.offset = (float)ModuleManager.this.renderer.getStringWidth(text) / 1.0f;
                    module.vOffset = (float)ModuleManager.this.renderer.getFontHeight() / 1.0f;
                    if (module.isEnabled()) {
                        // empty if block
                    }
                    if (!module.isDisabled()) continue;
                }
            }
        }

        @Override
        public void start() {
            System.out.println("Starting animation thread.");
            this.service.scheduleAtFixedRate(this, 0L, 1L, TimeUnit.MILLISECONDS);
        }
    }
}

