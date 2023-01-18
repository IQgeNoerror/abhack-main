/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.network.play.server.SPacketExplosion
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.abHack.features.modules.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import me.abHack.event.events.PacketEvent;
import me.abHack.event.events.Render3DEvent;
import me.abHack.features.modules.Module;
import me.abHack.features.modules.client.ClickGui;
import me.abHack.features.setting.Setting;
import me.abHack.util.ColorUtil;
import me.abHack.util.RenderUtil;
import me.abHack.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ExplosionChams
extends Module {
    public final Setting<Integer> red = this.register(new Setting<Integer>("Red", 30, 0, 255));
    public final Setting<Integer> green = this.register(new Setting<Integer>("Green", 167, 0, 255));
    public final Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 255, 0, 255));
    public final Setting<Integer> alpha = this.register(new Setting<Integer>("Alpha", 150, 0, 255));
    public final Setting<Float> increase = this.register(new Setting<Float>("Increase Size", Float.valueOf(0.5f), Float.valueOf(0.1f), Float.valueOf(5.0f)));
    public final Setting<Integer> riseSpeed = this.register(new Setting<Integer>("Rise Time", 5, 1, 50));
    public final Setting<Boolean> rainbow = this.register(new Setting<Boolean>("Rainbow", true));
    public Map<EntityEnderCrystal, BlockPos> explodedCrystals = new HashMap<EntityEnderCrystal, BlockPos>();
    public BlockPos crystalPos = null;
    public int aliveTicks = 0;
    public double speed = 0.0;
    public Timer timer = new Timer();

    public ExplosionChams() {
        super("ExplosionChams", "Draws a cham when a crystal explodes", Module.Category.RENDER, true, false, false);
    }

    @Override
    public void onEnable() {
        this.explodedCrystals.clear();
    }

    @Override
    public void onUpdate() {
        if (ExplosionChams.fullNullCheck()) {
            return;
        }
        ++this.aliveTicks;
        if (this.timer.passedMs(5L)) {
            this.speed += 0.5;
            this.timer.reset();
        }
        if (this.speed > (double)this.riseSpeed.getValue().intValue()) {
            this.speed = 0.0;
            this.explodedCrystals.clear();
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        try {
            for (Entity crystal : ExplosionChams.mc.world.loadedEntityList) {
                if (!(crystal instanceof EntityEnderCrystal) || !(event.getPacket() instanceof SPacketExplosion)) continue;
                this.crystalPos = new BlockPos(crystal.posX, crystal.posY, crystal.posZ);
                this.explodedCrystals.put((EntityEnderCrystal)crystal, this.crystalPos);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @Override
    @SubscribeEvent
    public void onRender3D(Render3DEvent event) {
        if (!this.explodedCrystals.isEmpty()) {
            RenderUtil.drawCircle(this.crystalPos.getX(), (float)this.crystalPos.getY() + (float)this.speed / 3.0f + 0.7f, this.crystalPos.getZ(), 0.6f + this.increase.getValue().floatValue(), this.rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()));
            RenderUtil.drawCircle(this.crystalPos.getX(), (float)this.crystalPos.getY() + (float)this.speed / 3.0f + 0.6f, this.crystalPos.getZ(), 0.5f + this.increase.getValue().floatValue(), this.rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()));
            RenderUtil.drawCircle(this.crystalPos.getX(), (float)this.crystalPos.getY() + (float)this.speed / 3.0f + 0.5f, this.crystalPos.getZ(), 0.4f + this.increase.getValue().floatValue(), this.rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()));
            RenderUtil.drawCircle(this.crystalPos.getX(), (float)this.crystalPos.getY() + (float)this.speed / 3.0f + 0.4f, this.crystalPos.getZ(), 0.3f + this.increase.getValue().floatValue(), this.rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()));
            RenderUtil.drawCircle(this.crystalPos.getX(), (float)this.crystalPos.getY() + (float)this.speed / 3.0f + 0.3f, this.crystalPos.getZ(), 0.2f + this.increase.getValue().floatValue(), this.rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()));
            RenderUtil.drawCircle(this.crystalPos.getX(), (float)this.crystalPos.getY() + (float)this.speed / 3.0f + 0.2f, this.crystalPos.getZ(), 0.1f + this.increase.getValue().floatValue(), this.rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()));
            RenderUtil.drawCircle(this.crystalPos.getX(), (float)this.crystalPos.getY() + (float)this.speed / 3.0f + 0.1f, this.crystalPos.getZ(), 0.0f + this.increase.getValue().floatValue(), this.rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()));
        }
    }
}

