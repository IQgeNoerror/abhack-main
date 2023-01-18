/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.init.MobEffects
 *  net.minecraft.network.play.server.SPacketExplosion
 *  net.minecraftforge.client.event.RenderBlockOverlayEvent
 *  net.minecraftforge.client.event.RenderBlockOverlayEvent$OverlayType
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.abHack.features.modules.render;

import me.abHack.event.events.NoRenderEvent;
import me.abHack.event.events.PacketEvent;
import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoRender
extends Module {
    private static NoRender INSTANCE = new NoRender();
    public Setting<Boolean> armor = this.register(new Setting<Boolean>("Armor", true));
    public Setting<Boolean> fire = this.register(new Setting<Boolean>("Frie", true));
    public Setting<Boolean> blind = this.register(new Setting<Boolean>("Blind", true));
    public Setting<Boolean> nausea = this.register(new Setting<Boolean>("Nausea", true));
    public Setting<Boolean> hurtCam = this.register(new Setting<Boolean>("HurtCam", true));
    public Setting<Boolean> explosions = this.register(new Setting<Boolean>("Explosions", false));
    public Setting<Boolean> items = this.register(new Setting<Boolean>("Items", Boolean.valueOf(false), "Removes items on the ground."));
    public Setting<Boolean> noWeather = this.register(new Setting<Boolean>("Weather", Boolean.valueOf(false), "AntiWeather"));
    public Setting<Boolean> skyLightUpdate = this.register(new Setting<Boolean>("SkyLightUpdate", true));

    public NoRender() {
        super("NoRender", "Shield some particle effects", Module.Category.RENDER, true, false, false);
        this.setInstance();
    }

    public static NoRender getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NoRender();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (this.explosions.getValue().booleanValue() && event.getPacket() instanceof SPacketExplosion) {
            event.setCanceled(true);
        }
    }

    @Override
    public void onUpdate() {
        if (this.items.getValue().booleanValue()) {
            NoRender.mc.world.loadedEntityList.stream().filter(EntityItem.class::isInstance).map(EntityItem.class::cast).forEach(Entity::setDead);
        }
        if (this.blind.getValue().booleanValue() && NoRender.mc.player.isPotionActive(MobEffects.BLINDNESS)) {
            NoRender.mc.player.removePotionEffect(MobEffects.BLINDNESS);
        }
        if (this.nausea.getValue().booleanValue() && NoRender.mc.player.isPotionActive(MobEffects.NAUSEA)) {
            NoRender.mc.player.removePotionEffect(MobEffects.NAUSEA);
        }
        if (this.noWeather.getValue().booleanValue() && NoRender.mc.world.isRaining()) {
            NoRender.mc.world.setRainStrength(0.0f);
        }
    }

    @SubscribeEvent
    public void NoRenderEventListener(NoRenderEvent event) {
        if (event.getStage() == 0 && this.armor.getValue().booleanValue()) {
            event.setCanceled(true);
        } else if (event.getStage() == 1 && this.hurtCam.getValue().booleanValue()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void blockOverlayEventListener(RenderBlockOverlayEvent event) {
        if (this.fire.getValue().booleanValue() && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.FIRE) {
            event.setCanceled(true);
        }
    }
}

