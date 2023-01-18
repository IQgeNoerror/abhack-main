package me.abHack.features.modules.client;

import net.minecraft.entity.player.EntityPlayer;
import me.abHack.event.events.ClientEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import me.abHack.util.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import me.abHack.util.MathUtil;
import me.abHack.util.EntityUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.multiplayer.ServerData;
import java.util.Collection;
import net.minecraft.potion.PotionEffect;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import me.abHack.OyVey;
import net.minecraft.client.gui.GuiChat;
import java.util.function.ToIntFunction;
import net.minecraft.init.Items;
import me.abHack.util.ColorUtil;
import me.abHack.event.events.Render2DEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.text.TextComponentString;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import java.util.HashMap;
import me.abHack.util.TextUtil;
import me.abHack.util.Timer;
import java.util.Map;
import me.abHack.features.setting.Setting;
import net.minecraft.item.ItemStack;
import me.abHack.features.modules.Module;

public class HUD extends Module
{
    private static final ItemStack totem;
    private static HUD INSTANCE;
    private final Setting<Boolean> grayNess;
    private final Setting<Boolean> renderingUp;
    private final Setting<Boolean> waterMark;
    private final Setting<Boolean> arrayList;
    private final Setting<Boolean> coords;
    private final Setting<Boolean> direction;
    private final Setting<Boolean> armor;
    private final Setting<Boolean> totems;
    private final Setting<Boolean> greeter;
    private final Setting<Boolean> speed;
    private final Setting<Boolean> potions;
    private final Setting<Boolean> server;
    private final Setting<Boolean> ping;
    private final Setting<Boolean> tps;
    private final Setting<Boolean> fps;
    private final Map<String, Integer> players;
    private final Setting<Boolean> lag;
    private final Timer timer;
    public Setting<Boolean> time;
    public Setting<Boolean> chattime;
    private final Setting<Boolean> hitMarkers;
    public Setting<Boolean> Info;
    private final Setting<Boolean> crystals;
    public Setting<Integer> crystalX;
    public Setting<Integer> crystalY;
    public Setting<Integer> expX;
    public Setting<Integer> expY;
    private final Setting<Boolean> exp;
    private final Setting<Boolean> gapples;
    public Setting<Integer> GapplesX;
    public Setting<Integer> GapplesY;
    public Setting<Boolean> FriendList;
    public Setting<Integer> FriendListY;
    public Setting<Boolean> playerViewer;
    public Setting<Integer> playerViewerX;
    public Setting<Integer> playerViewerY;
    public Setting<Float> playerScale;
    public Setting<TextUtil.Color> bracketColor;
    public Setting<TextUtil.Color> commandColor;
    public Setting<Boolean> notifyToggles;
    public Setting<Boolean> magenDavid;
    public Setting<RenderingMode> renderingMode;
    public Setting<Integer> waterMarkY;
    public Setting<Integer> lagTime;
    private int color;
    private boolean shouldIncrement;
    private int hitMarkerTimer;

    public HUD() {
        super("HUD", "HUD Elements rendered on your screen", Category.CLIENT, true, false, false);
        this.grayNess = (Setting<Boolean>)this.register(new Setting("Gray", true));
        this.renderingUp = (Setting<Boolean>)this.register(new Setting("RenderingUp", false, "Orientation of the HUD-Elements."));
        this.waterMark = (Setting<Boolean>)this.register(new Setting("Watermark", false, "displays watermark"));
        this.arrayList = (Setting<Boolean>)this.register(new Setting("ActiveModules", true, "Lists the active modules."));
        this.coords = (Setting<Boolean>)this.register(new Setting("Coords", false, "Your current coordinates"));
        this.direction = (Setting<Boolean>)this.register(new Setting("Direction", false, "The Direction you are facing."));
        this.armor = (Setting<Boolean>)this.register(new Setting("Armor", true, "ArmorHUD"));
        this.totems = (Setting<Boolean>)this.register(new Setting("Totems", true, "TotemHUD"));
        this.greeter = (Setting<Boolean>)this.register(new Setting("Welcomer", false, "The time"));
        this.speed = (Setting<Boolean>)this.register(new Setting("Speed", true, "Your Speed"));
        this.potions = (Setting<Boolean>)this.register(new Setting("Potions", true, "Your Speed"));
        this.server = (Setting<Boolean>)this.register(new Setting("IP", true, "Shows the server"));
        this.ping = (Setting<Boolean>)this.register(new Setting("Ping", true, "Your response time to the server."));
        this.tps = (Setting<Boolean>)this.register(new Setting("TPS", true, "Ticks per second of the server."));
        this.fps = (Setting<Boolean>)this.register(new Setting("FPS", true, "Your frames per second."));
        this.players = new HashMap<String, Integer>();
        this.lag = (Setting<Boolean>)this.register(new Setting("LagNotifier", true, "The time"));
        this.timer = new Timer();
        this.time = (Setting<Boolean>)this.register(new Setting("Time", true, "The time"));
        this.chattime = (Setting<Boolean>)this.register(new Setting("ChatTime", true, "Prefixes chat messages with the time"));
        this.hitMarkers = (Setting<Boolean>)this.register(new Setting("HitMarkers", false));
        this.Info = (Setting<Boolean>)this.register(new Setting("Info", false));
        this.crystals = (Setting<Boolean>)this.register(new Setting("Crystals", true, v -> this.Info.getValue()));
        this.crystalX = (Setting<Integer>)this.register(new Setting("CrystalX", 0, 0, 900, v -> this.Info.getValue() && this.crystals.getValue()));
        this.crystalY = (Setting<Integer>)this.register(new Setting("CrystalY", 122, 0, 530, v -> this.Info.getValue() && this.crystals.getValue()));
        this.expX = (Setting<Integer>)this.register(new Setting("ExpX", 0, 0, 900, v -> this.Info.getValue() && this.crystals.getValue()));
        this.expY = (Setting<Integer>)this.register(new Setting("ExpY", 128, 0, 530, v -> this.Info.getValue() && this.crystals.getValue()));
        this.exp = (Setting<Boolean>)this.register(new Setting("Exp", true, v -> this.Info.getValue()));
        this.gapples = (Setting<Boolean>)this.register(new Setting("Gapples", true, v -> this.Info.getValue()));
        this.GapplesX = (Setting<Integer>)this.register(new Setting("GapplesX", 0, 0, 900, v -> this.Info.getValue() && this.gapples.getValue()));
        this.GapplesY = (Setting<Integer>)this.register(new Setting("GapplesY", 135, 0, 530, v -> this.Info.getValue() && this.gapples.getValue()));
        this.FriendList = (Setting<Boolean>)this.register(new Setting("FriendList", Boolean.FALSE));
        this.FriendListY = (Setting<Integer>)this.register(new Setting("FriendListY", 173, 0, 530, v -> this.FriendList.getValue()));
        this.playerViewer = (Setting<Boolean>)this.register(new Setting("PlayerViewer", false));
        this.playerViewerX = (Setting<Integer>)this.register(new Setting("PlayerX", 150, 0, 700, v -> this.playerViewer.getValue()));
        this.playerViewerY = (Setting<Integer>)this.register(new Setting("PlayerY", 60, 0, 500, v -> this.playerViewer.getValue()));
        this.playerScale = (Setting<Float>)this.register(new Setting("PlayerScale", 0.7f, 0.1f, 2.0f, v -> this.playerViewer.getValue()));
        this.bracketColor = (Setting<TextUtil.Color>)this.register(new Setting("BracketColor", TextUtil.Color.BLUE));
        this.commandColor = (Setting<TextUtil.Color>)this.register(new Setting("NameColor", TextUtil.Color.BLUE));
        this.notifyToggles = (Setting<Boolean>)this.register(new Setting("ChatNotify", true, "notifys in chat"));
        this.magenDavid = (Setting<Boolean>)this.register(new Setting("FutureGui", true, "draws magen david"));
        this.renderingMode = (Setting<RenderingMode>)this.register(new Setting("Ordering", RenderingMode.Length));
        this.waterMarkY = (Setting<Integer>)this.register(new Setting("WatermarkPosY", 2, 0, 20, v -> this.waterMark.getValue()));
        this.lagTime = (Setting<Integer>)this.register(new Setting("LagTime", 1000, 0, 2000));
        this.setInstance();
    }

    public static HUD getInstance() {
        if (HUD.INSTANCE == null) {
            HUD.INSTANCE = new HUD();
        }
        return HUD.INSTANCE;
    }

    private void setInstance() {
        HUD.INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.shouldIncrement) {
            ++this.hitMarkerTimer;
        }
        if (this.hitMarkerTimer == 10) {
            this.hitMarkerTimer = 0;
            this.shouldIncrement = false;
        }
    }

    @SubscribeEvent
    public void onClientChatReceived(final ClientChatReceivedEvent event) {
        if (this.chattime.getValue()) {
            final Date date = new Date();
            final SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm");
            final String strDate = dateFormatter.format(date);
            final TextComponentString time = new TextComponentString(ChatFormatting.LIGHT_PURPLE + "[" + ChatFormatting.DARK_PURPLE + strDate + ChatFormatting.LIGHT_PURPLE + "]" + ChatFormatting.RESET + " ");
            event.setMessage(time.appendSibling(event.getMessage()));
        }
    }

    @Override
    public void onRender2D(final Render2DEvent event) {
        if (fullNullCheck()) {
            return;
        }
        final int width = this.renderer.scaledWidth;
        final int height = this.renderer.scaledHeight;
        this.color = ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue());
        if (this.waterMark.getValue()) {
            final String string = "ab-Hack v0.0.1";
            if (ClickGui.getInstance().rainbow.getValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(string, 2.0f, this.waterMarkY.getValue(), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    final int[] arrayOfInt = { 1 };
                    final char[] stringToCharArray = string.toCharArray();
                    float f = 0.0f;
                    for (final char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0f + f, this.waterMarkY.getValue(), ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += this.renderer.getStringWidth(String.valueOf(c));
                        ++arrayOfInt[0];
                    }
                }
            }
            else {
                this.renderer.drawString(string, 2.0f, this.waterMarkY.getValue(), this.color, true);
            }
        }
        final String Crystals = "Crystals: " + HUD.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.END_CRYSTAL).mapToInt(ItemStack::getCount).sum();
        final String Exp = "Exp: " + HUD.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.EXPERIENCE_BOTTLE).mapToInt(ItemStack::getCount).sum();
        final String Gapples = "Gapples: " + HUD.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.GOLDEN_APPLE).mapToInt(ItemStack::getCount).sum();
        this.color = ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue());
        int y = 10;
        if (ClickGui.getInstance().rainbow.getValue()) {
            if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                if (this.Info.getValue() && this.crystals.getValue()) {
                    this.renderer.drawString(Crystals, this.crystalX.getValue(), this.crystalY.getValue(), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    y += 12;
                }
                if (this.Info.getValue() && this.exp.getValue()) {
                    this.renderer.drawString(Exp, this.expX.getValue(), this.expY.getValue(), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    y += 12;
                }
                if (this.Info.getValue() && this.gapples.getValue()) {
                    this.renderer.drawString(Gapples, this.GapplesX.getValue(), this.GapplesY.getValue(), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    y += 12;
                }
            }
        }
        else {
            if (this.Info.getValue() && this.crystals.getValue()) {
                this.renderer.drawString(Crystals, this.crystalX.getValue(), this.crystalY.getValue(), this.color, true);
                y += 12;
            }
            if (this.Info.getValue() && this.exp.getValue()) {
                this.renderer.drawString(Exp, this.expX.getValue(), this.expY.getValue(), this.color, true);
                y += 12;
            }
            if (this.Info.getValue() && this.gapples.getValue()) {
                this.renderer.drawString(Gapples, this.GapplesX.getValue(), this.GapplesY.getValue(), this.color, true);
                y += 12;
            }
        }
        this.color = ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue());
        if (this.FriendList.getValue()) {
            this.renderFriends();
        }
        final int[] counter1 = { 1 };
        int j = (HUD.mc.currentScreen instanceof GuiChat && !this.renderingUp.getValue()) ? 14 : 0;
        if (this.arrayList.getValue()) {
            if (this.renderingUp.getValue()) {
                if (this.renderingMode.getValue() == RenderingMode.ABC) {
                    for (int k = 0; k < OyVey.moduleManager.sortedModulesABC.size(); ++k) {
                        final String str = OyVey.moduleManager.sortedModulesABC.get(k);
                        this.renderer.drawString(str, (float)(width - 2 - this.renderer.getStringWidth(str)), (float)(2 + j * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                        ++j;
                        ++counter1[0];
                    }
                }
                else {
                    for (int k = 0; k < OyVey.moduleManager.sortedModules.size(); ++k) {
                        final Module module = OyVey.moduleManager.sortedModules.get(k);
                        final String str2 = module.getDisplayName() + ChatFormatting.GRAY + ((module.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
                        this.renderer.drawString(str2, (float)(width - 2 - this.renderer.getStringWidth(str2)), (float)(2 + j * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                        ++j;
                        ++counter1[0];
                    }
                }
            }
            else if (this.renderingMode.getValue() == RenderingMode.ABC) {
                for (int k = 0; k < OyVey.moduleManager.sortedModulesABC.size(); ++k) {
                    final String str = OyVey.moduleManager.sortedModulesABC.get(k);
                    j += 10;
                    this.renderer.drawString(str, (float)(width - 2 - this.renderer.getStringWidth(str)), (float)(height - j), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
            }
            else {
                for (int k = 0; k < OyVey.moduleManager.sortedModules.size(); ++k) {
                    final Module module = OyVey.moduleManager.sortedModules.get(k);
                    final String str2 = module.getDisplayName() + ChatFormatting.GRAY + ((module.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
                    j += 10;
                    this.renderer.drawString(str2, (float)(width - 2 - this.renderer.getStringWidth(str2)), (float)(height - j), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
            }
        }
        final String grayString = this.grayNess.getValue() ? String.valueOf(ChatFormatting.GRAY) : "";
        int i = (HUD.mc.currentScreen instanceof GuiChat && this.renderingUp.getValue()) ? 13 : (this.renderingUp.getValue() ? -2 : 0);
        if (this.renderingUp.getValue()) {
            if (this.potions.getValue()) {
                final List<PotionEffect> effects = new ArrayList<PotionEffect>(Minecraft.getMinecraft().player.getActivePotionEffects());
                for (final PotionEffect potionEffect : effects) {
                    final String str3 = OyVey.potionManager.getColoredPotionString(potionEffect);
                    i += 10;
                    this.renderer.drawString(str3, (float)(width - this.renderer.getStringWidth(str3) - 2), (float)(height - 2 - i), potionEffect.getPotion().getLiquidColor(), true);
                }
            }
            if (this.server.getValue()) {
                final String sText = grayString + "IP " + ChatFormatting.WHITE + (HUD.mc.isSingleplayer() ? "SinglePlayer" : Objects.requireNonNull(HUD.mc.getCurrentServerData()).serverIP);
                i += 10;
                this.renderer.drawString(sText, (float)(width - this.renderer.getStringWidth(sText) - 2), (float)(height - 2 - i), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            if (this.speed.getValue()) {
                final String str2 = grayString + "Speed " + ChatFormatting.WHITE + OyVey.speedManager.getSpeedKpH() + " km/h";
                i += 10;
                this.renderer.drawString(str2, (float)(width - this.renderer.getStringWidth(str2) - 2), (float)(height - 2 - i), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            if (this.time.getValue()) {
                final String str2 = grayString + "Time " + ChatFormatting.WHITE + new SimpleDateFormat("h:mm a").format(new Date());
                i += 10;
                this.renderer.drawString(str2, (float)(width - this.renderer.getStringWidth(str2) - 2), (float)(height - 2 - i), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            if (this.tps.getValue()) {
                final String str2 = grayString + "TPS " + ChatFormatting.WHITE + OyVey.serverManager.getTPS();
                i += 10;
                this.renderer.drawString(str2, (float)(width - this.renderer.getStringWidth(str2) - 2), (float)(height - 2 - i), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            final String fpsText = grayString + "FPS " + ChatFormatting.WHITE + Minecraft.debugFPS;
            final String sText2 = grayString + "Server " + ChatFormatting.WHITE + (HUD.mc.isSingleplayer() ? "SinglePlayer" : Objects.requireNonNull(HUD.mc.getCurrentServerData()).serverIP);
            final String str4 = grayString + "Ping " + ChatFormatting.WHITE + OyVey.serverManager.getPing();
            if (this.renderer.getStringWidth(str4) > this.renderer.getStringWidth(fpsText)) {
                if (this.ping.getValue()) {
                    i += 10;
                    this.renderer.drawString(str4, (float)(width - this.renderer.getStringWidth(str4) - 2), (float)(height - 2 - i), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
                if (this.fps.getValue()) {
                    i += 10;
                    this.renderer.drawString(fpsText, (float)(width - this.renderer.getStringWidth(fpsText) - 2), (float)(height - 2 - i), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
            }
            else {
                if (this.fps.getValue()) {
                    i += 10;
                    this.renderer.drawString(fpsText, (float)(width - this.renderer.getStringWidth(fpsText) - 2), (float)(height - 2 - i), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
                if (this.ping.getValue()) {
                    i += 10;
                    this.renderer.drawString(str4, (float)(width - this.renderer.getStringWidth(str4) - 2), (float)(height - 2 - i), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
            }
        }
        else {
            if (this.potions.getValue()) {
                final List<PotionEffect> effects = new ArrayList<PotionEffect>(Minecraft.getMinecraft().player.getActivePotionEffects());
                for (final PotionEffect potionEffect : effects) {
                    final String str3 = OyVey.potionManager.getColoredPotionString(potionEffect);
                    this.renderer.drawString(str3, (float)(width - this.renderer.getStringWidth(str3) - 2), (float)(2 + i++ * 10), potionEffect.getPotion().getLiquidColor(), true);
                }
            }
            if (this.server.getValue()) {
                final String sText = grayString + "IP " + ChatFormatting.WHITE + (HUD.mc.isSingleplayer() ? "SinglePlayer" : Objects.requireNonNull(HUD.mc.getCurrentServerData()).serverIP);
                this.renderer.drawString(sText, (float)(width - this.renderer.getStringWidth(sText) - 2), (float)(2 + i++ * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            if (this.speed.getValue()) {
                final String str2 = grayString + "Speed " + ChatFormatting.WHITE + OyVey.speedManager.getSpeedKpH() + " km/h";
                this.renderer.drawString(str2, (float)(width - this.renderer.getStringWidth(str2) - 2), (float)(2 + i++ * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            if (this.time.getValue()) {
                final String str2 = grayString + "Time " + ChatFormatting.WHITE + new SimpleDateFormat("h:mm a").format(new Date());
                this.renderer.drawString(str2, (float)(width - this.renderer.getStringWidth(str2) - 2), (float)(2 + i++ * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            if (this.tps.getValue()) {
                final String str2 = grayString + "TPS " + ChatFormatting.WHITE + OyVey.serverManager.getTPS();
                this.renderer.drawString(str2, (float)(width - this.renderer.getStringWidth(str2) - 2), (float)(2 + i++ * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            final String fpsText = grayString + "FPS " + ChatFormatting.WHITE + Minecraft.debugFPS;
            final String str5 = grayString + "Ping " + ChatFormatting.WHITE + OyVey.serverManager.getPing();
            if (this.renderer.getStringWidth(str5) > this.renderer.getStringWidth(fpsText)) {
                if (this.ping.getValue()) {
                    this.renderer.drawString(str5, (float)(width - this.renderer.getStringWidth(str5) - 2), (float)(2 + i++ * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
                if (this.fps.getValue()) {
                    this.renderer.drawString(fpsText, (float)(width - this.renderer.getStringWidth(fpsText) - 2), (float)(2 + i++ * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
            }
            else {
                if (this.fps.getValue()) {
                    this.renderer.drawString(fpsText, (float)(width - this.renderer.getStringWidth(fpsText) - 2), (float)(2 + i++ * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
                if (this.ping.getValue()) {
                    this.renderer.drawString(str5, (float)(width - this.renderer.getStringWidth(str5) - 2), (float)(2 + i++ * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
            }
        }
        final boolean inHell = HUD.mc.world.getBiome(HUD.mc.player.getPosition()).getBiomeName().equals("Hell");
        final int posX = (int)HUD.mc.player.posX;
        final int posY = (int)HUD.mc.player.posY;
        final int posZ = (int)HUD.mc.player.posZ;
        final float nether = inHell ? 8.0f : 0.125f;
        final int hposX = (int)(HUD.mc.player.posX * nether);
        final int hposZ = (int)(HUD.mc.player.posZ * nether);
        i = ((HUD.mc.currentScreen instanceof GuiChat) ? 14 : 0);
        final String coordinates = ChatFormatting.WHITE + "XYZ " + ChatFormatting.RESET + (inHell ? (posX + ", " + posY + ", " + posZ + ChatFormatting.WHITE + " [" + ChatFormatting.RESET + hposX + ", " + hposZ + ChatFormatting.WHITE + "]" + ChatFormatting.RESET) : (posX + ", " + posY + ", " + posZ + ChatFormatting.WHITE + " [" + ChatFormatting.RESET + hposX + ", " + hposZ + ChatFormatting.WHITE + "]"));
        final String direction = this.direction.getValue() ? OyVey.rotationManager.getDirection4D(false) : "";
        final String coords = this.coords.getValue() ? coordinates : "";
        i += 10;
        if (ClickGui.getInstance().rainbow.getValue()) {
            final String rainbowCoords = this.coords.getValue() ? ("XYZ " + posX + ", " + posY + ", " + posZ + " [" + hposX + ", " + hposZ + "]") : "";
            if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                this.renderer.drawString(direction, 2.0f, (float)(height - i - 11), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                this.renderer.drawString(rainbowCoords, 2.0f, (float)(height - i), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
            }
            else {
                final int[] counter2 = { 1 };
                final char[] stringToCharArray2 = direction.toCharArray();
                float s = 0.0f;
                for (final char c2 : stringToCharArray2) {
                    this.renderer.drawString(String.valueOf(c2), 2.0f + s, (float)(height - i - 11), ColorUtil.rainbow(counter2[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    s += this.renderer.getStringWidth(String.valueOf(c2));
                    ++counter2[0];
                }
                final int[] counter3 = { 1 };
                final char[] stringToCharArray3 = rainbowCoords.toCharArray();
                float u = 0.0f;
                for (final char c3 : stringToCharArray3) {
                    this.renderer.drawString(String.valueOf(c3), 2.0f + u, (float)(height - i), ColorUtil.rainbow(counter3[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    u += this.renderer.getStringWidth(String.valueOf(c3));
                    ++counter3[0];
                }
            }
        }
        else {
            this.renderer.drawString(direction, 2.0f, (float)(height - i - 11), this.color, true);
            this.renderer.drawString(coords, 2.0f, (float)(height - i), this.color, true);
        }
        if (this.armor.getValue()) {
            this.renderArmorHUD(true);
        }
        if (this.totems.getValue()) {
            this.renderTotemHUD();
        }
        if (this.greeter.getValue()) {
            this.renderGreeter();
        }
        if (this.lag.getValue()) {
            this.renderLag();
        }
        if (this.playerViewer.getValue()) {
            this.drawPlayer();
        }
        if (!this.hitMarkers.getValue()) {
            return;
        }
        if (this.hitMarkerTimer <= 0) {
            return;
        }
        this.drawHitMarkers();
    }

    public Map<String, Integer> getTextRadarPlayers() {
        return EntityUtil.getTextRadarPlayers();
    }

    public void renderGreeter() {
        final int width = this.renderer.scaledWidth;
        String text = "";
        if (this.greeter.getValue()) {
            text = text + MathUtil.getTimeOfDay() + HUD.mc.player.getDisplayNameString();
        }
        if (ClickGui.getInstance().rainbow.getValue()) {
            if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                this.renderer.drawString(text, width / 2.0f - this.renderer.getStringWidth(text) / 2.0f + 2.0f, 2.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
            }
            else {
                final int[] counter1 = { 1 };
                final char[] stringToCharArray = text.toCharArray();
                float i = 0.0f;
                for (final char c : stringToCharArray) {
                    this.renderer.drawString(String.valueOf(c), width / 2.0f - this.renderer.getStringWidth(text) / 2.0f + 2.0f + i, 2.0f, ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    i += this.renderer.getStringWidth(String.valueOf(c));
                    ++counter1[0];
                }
            }
        }
        else {
            this.renderer.drawString(text, width / 2.0f - this.renderer.getStringWidth(text) / 2.0f + 2.0f, 2.0f, this.color, true);
        }
    }

    public void renderLag() {
        final int width = this.renderer.scaledWidth;
        if (OyVey.serverManager.isServerNotResponding()) {
            final String text = ChatFormatting.RED + "Server not responding " + MathUtil.round(OyVey.serverManager.serverRespondingTime() / 1000.0f, 1) + "s.";
            this.renderer.drawString(text, width / 2.0f - this.renderer.getStringWidth(text) / 2.0f + 2.0f, 20.0f, this.color, true);
        }
    }

    public void drawHitMarkers() {
        final ScaledResolution resolution = new ScaledResolution(HUD.mc);
        RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f - 4.0f, resolution.getScaledHeight() / 2.0f - 4.0f, resolution.getScaledWidth() / 2.0f - 8.0f, resolution.getScaledHeight() / 2.0f - 8.0f, 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
        RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f + 4.0f, resolution.getScaledHeight() / 2.0f - 4.0f, resolution.getScaledWidth() / 2.0f + 8.0f, resolution.getScaledHeight() / 2.0f - 8.0f, 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
        RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f - 4.0f, resolution.getScaledHeight() / 2.0f + 4.0f, resolution.getScaledWidth() / 2.0f - 8.0f, resolution.getScaledHeight() / 2.0f + 8.0f, 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
        RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f + 4.0f, resolution.getScaledHeight() / 2.0f + 4.0f, resolution.getScaledWidth() / 2.0f + 8.0f, resolution.getScaledHeight() / 2.0f + 8.0f, 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
    }

    public void renderTotemHUD() {
        final int width = this.renderer.scaledWidth;
        final int height = this.renderer.scaledHeight;
        int totems = HUD.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (HUD.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            totems += HUD.mc.player.getHeldItemOffhand().getCount();
        }
        if (totems > 0) {
            GlStateManager.enableTexture2D();
            final int i = width / 2;
            final int iteration = 0;
            final int y = height - 55 - ((HUD.mc.player.isInWater() && HUD.mc.playerController.gameIsSurvivalOrAdventure()) ? 10 : 0);
            final int x = i - 189 + 180 + 2;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(HUD.totem, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRenderer, HUD.totem, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.renderer.drawStringWithShadow(totems + "", (float)(x + 19 - 2 - this.renderer.getStringWidth(totems + "")), (float)(y + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
    }

    public void renderArmorHUD(final boolean percent) {
        final int width = this.renderer.scaledWidth;
        final int height = this.renderer.scaledHeight;
        GlStateManager.enableTexture2D();
        final int i = width / 2;
        int iteration = 0;
        final int y = height - 55 - ((HUD.mc.player.isInWater() && HUD.mc.playerController.gameIsSurvivalOrAdventure()) ? 10 : 0);
        for (final ItemStack is : HUD.mc.player.inventory.armorInventory) {
            ++iteration;
            if (is.isEmpty()) {
                continue;
            }
            final int x = i - 90 + (9 - iteration) * 20 + 2;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(is, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRenderer, is, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            final String s = (is.getCount() > 1) ? (is.getCount() + "") : "";
            this.renderer.drawStringWithShadow(s, (float)(x + 19 - 2 - this.renderer.getStringWidth(s)), (float)(y + 9), 16777215);
            if (!percent) {
                continue;
            }
            final int itemDurability = is.getMaxDamage() - is.getItemDamage();
            final float green = (is.getMaxDamage() - (float)is.getItemDamage()) / is.getMaxDamage();
            final float red = 1.0f - green;
            int dmg = 100 - (int)(red * 100.0f);
            if (dmg == -2147483547) {
                dmg = 100;
            }
            this.renderer.drawStringWithShadow(dmg + "", (float)(x + 8 - this.renderer.getStringWidth(dmg + "") / 2), (float)(y - 11), ColorUtil.toRGBA((int)(red * 255.0f), (int)(green * 255.0f), 0));
        }
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }

    public void drawPlayer() {
        final EntityPlayerSP ent = HUD.mc.player;
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.rotate(0.0f, 0.0f, 5.0f, 0.0f);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(this.playerViewerX.getValue() + 25), (float)(this.playerViewerY.getValue() + 25), 50.0f);
        GlStateManager.scale(-50.0f * this.playerScale.getValue(), 50.0f * this.playerScale.getValue(), 50.0f * this.playerScale.getValue());
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-(float)Math.atan(this.playerViewerY.getValue() / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        final RenderManager rendermanager = HUD.mc.getRenderManager();
        rendermanager.setPlayerViewY(180.0f);
        rendermanager.setRenderShadow(false);
        try {
            rendermanager.renderEntity((Entity)ent, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
        }
        catch (Exception ex) {}
        rendermanager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.depthFunc(515);
        GlStateManager.resetColor();
        GlStateManager.disableDepth();
        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(final AttackEntityEvent event) {
        this.shouldIncrement = true;
    }

    @Override
    public void onLoad() {
        OyVey.commandManager.setClientMessage(this.getCommandMessage());
    }

    @SubscribeEvent
    public void onSettingChange(final ClientEvent event) {
        if (event.getStage() == 2 && this.equals(event.getSetting().getFeature())) {
            OyVey.commandManager.setClientMessage(this.getCommandMessage());
        }
    }

    public String getCommandMessage() {
        return TextUtil.coloredString("<", this.bracketColor.getPlannedValue()) + TextUtil.coloredString("ab-Hack", this.commandColor.getPlannedValue()) + TextUtil.coloredString(">", this.bracketColor.getPlannedValue());
    }

    public void drawTextRadar(final int yOffset) {
        if (!this.players.isEmpty()) {
            int y = this.renderer.getFontHeight() + 7 + yOffset;
            for (final Map.Entry<String, Integer> player : this.players.entrySet()) {
                final String text = player.getKey() + " ";
                final int textheight = this.renderer.getFontHeight() + 1;
                this.renderer.drawString(text, 2.0f, (float)y, this.color, true);
                y += textheight;
            }
        }
    }

    private void renderFriends() {
        final List<String> friends = new ArrayList<String>();
        for (final EntityPlayer player : HUD.mc.world.playerEntities) {
            if (OyVey.friendManager.isFriend(player.getName())) {
                friends.add(player.getName());
            }
        }
        if (ClickGui.getInstance().rainbow.getValue()) {
            if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                int y = this.FriendListY.getValue();
                if (friends.isEmpty()) {
                    this.renderer.drawString("No friends", 0.0f, (float)y, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    this.renderer.drawString("Friends:", 0.0f, (float)y, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    y += 12;
                    for (final String friend : friends) {
                        this.renderer.drawString(friend, 0.0f, (float)y, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        y += 12;
                    }
                }
            }
        }
        else {
            int y = this.FriendListY.getValue();
            if (friends.isEmpty()) {
                this.renderer.drawString("No Webstas online", 0.0f, (float)y, this.color, true);
            }
            else {
                this.renderer.drawString("Webstas near you:", 0.0f, (float)y, this.color, true);
                y += 12;
                for (final String friend : friends) {
                    this.renderer.drawString(friend, 0.0f, (float)y, this.color, true);
                    y += 12;
                }
            }
        }
    }

    static {
        totem = new ItemStack(Items.TOTEM_OF_UNDYING);
        HUD.INSTANCE = new HUD();
    }

    public enum RenderingMode
    {
        Length,
        ABC;
    }
}