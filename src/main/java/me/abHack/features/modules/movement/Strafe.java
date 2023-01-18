/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.MobEffects
 *  net.minecraft.util.MovementInput
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.abHack.features.modules.movement;

import java.util.Objects;
import me.abHack.OyVey;
import me.abHack.event.events.ClientEvent;
import me.abHack.event.events.MoveEvent;
import me.abHack.event.events.UpdateWalkingPlayerEvent;
import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import me.abHack.util.EntityUtil;
import me.abHack.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import net.minecraft.util.MovementInput;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Strafe
extends Module {
    private static Strafe INSTANCE;
    private final Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.NCP));
    private final Setting<Boolean> limiter = this.register(new Setting<Boolean>("SetGround", Boolean.valueOf(true), v -> this.mode.getValue() == Mode.NCP));
    private final Setting<Float> speed = this.register(new Setting<Float>("Speed", Float.valueOf(2.0f), Float.valueOf(1.0f), Float.valueOf(5.0f), v -> this.mode.getValue() == Mode.NCP));
    private final Timer timer = new Timer();
    public Setting<Boolean> strafeJump = this.register(new Setting<Object>("Jump", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.INSTANT));
    private int stage = 1;
    private double moveSpeed;
    private double lastDist;

    public Strafe() {
        super("Strafe", "AirControl etc.", Module.Category.MOVEMENT, true, false, false);
        INSTANCE = this;
    }

    public static Strafe getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Strafe();
        }
        return INSTANCE;
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.272;
        if (Strafe.mc.player.isPotionActive(MobEffects.SPEED)) {
            int amplifier = Objects.requireNonNull(Strafe.mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)amplifier;
        }
        return baseSpeed;
    }

    @Override
    public void onEnable() {
        this.timer.reset();
        this.moveSpeed = Strafe.getBaseMoveSpeed();
    }

    @Override
    public void onDisable() {
        this.moveSpeed = 0.0;
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0) {
            this.lastDist = Math.sqrt((Strafe.mc.player.posX - Strafe.mc.player.prevPosX) * (Strafe.mc.player.posX - Strafe.mc.player.prevPosX) + (Strafe.mc.player.posZ - Strafe.mc.player.prevPosZ) * (Strafe.mc.player.posZ - Strafe.mc.player.prevPosZ));
        }
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (this.mode.getValue() == Mode.NCP) {
            this.doNCP(event);
        }
    }

    private void doNCP(MoveEvent event) {
        if (this.shouldReturn()) {
            return;
        }
        if (!this.limiter.getValue().booleanValue() && Strafe.mc.player.onGround) {
            this.stage = 2;
        }
        switch (this.stage) {
            case 0: {
                ++this.stage;
                this.lastDist = 0.0;
                break;
            }
            case 2: {
                double motionY = 0.40123128;
                if (Strafe.mc.player.moveForward == 0.0f && Strafe.mc.player.moveStrafing == 0.0f || !Strafe.mc.player.onGround) break;
                if (Strafe.mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                    motionY += (double)((float)(Objects.requireNonNull(Strafe.mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST)).getAmplifier() + 1) * 0.1f);
                }
                Strafe.mc.player.motionY = motionY;
                event.setY(Strafe.mc.player.motionY);
                this.moveSpeed *= (double)this.speed.getValue().floatValue();
                break;
            }
            case 3: {
                this.moveSpeed = this.lastDist - 0.76 * (this.lastDist - Strafe.getBaseMoveSpeed());
                break;
            }
            default: {
                if (Strafe.mc.world.getCollisionBoxes((Entity)Strafe.mc.player, Strafe.mc.player.getEntityBoundingBox().offset(0.0, Strafe.mc.player.motionY, 0.0)).size() > 0 || Strafe.mc.player.collidedVertically && this.stage > 0) {
                    this.stage = Strafe.mc.player.moveForward != 0.0f || Strafe.mc.player.moveStrafing != 0.0f ? 1 : 0;
                }
                this.moveSpeed = this.lastDist - this.lastDist / 159.0;
            }
        }
        this.moveSpeed = Math.max(this.moveSpeed, Strafe.getBaseMoveSpeed());
        double forward = Strafe.mc.player.movementInput.moveForward;
        double strafe = Strafe.mc.player.movementInput.moveStrafe;
        double yaw = Strafe.mc.player.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        } else if (forward != 0.0 && strafe != 0.0) {
            forward *= Math.sin(0.7853981633974483);
            strafe *= Math.cos(0.7853981633974483);
        }
        event.setX((forward * this.moveSpeed * -Math.sin(Math.toRadians(yaw)) + strafe * this.moveSpeed * Math.cos(Math.toRadians(yaw))) * 0.99);
        event.setZ((forward * this.moveSpeed * Math.cos(Math.toRadians(yaw)) - strafe * this.moveSpeed * -Math.sin(Math.toRadians(yaw))) * 0.99);
        ++this.stage;
    }

    @SubscribeEvent
    public void onMode(MoveEvent event) {
        if (!(this.shouldReturn() || event.getStage() != 0 || this.mode.getValue() != Mode.INSTANT || Strafe.nullCheck() || Strafe.mc.player.isSneaking() || Strafe.mc.player.isInWater() || Strafe.mc.player.isInLava() || Strafe.mc.player.movementInput.moveForward == 0.0f && Strafe.mc.player.movementInput.moveStrafe == 0.0f)) {
            if (Strafe.mc.player.onGround && this.strafeJump.getValue().booleanValue()) {
                Strafe.mc.player.motionY = 0.4;
                event.setY(0.4);
            }
            MovementInput movementInput = Strafe.mc.player.movementInput;
            float moveForward = movementInput.moveForward;
            float moveStrafe = movementInput.moveStrafe;
            float rotationYaw = Strafe.mc.player.rotationYaw;
            if ((double)moveForward == 0.0 && (double)moveStrafe == 0.0) {
                event.setX(0.0);
                event.setZ(0.0);
            } else {
                if ((double)moveForward != 0.0) {
                    float f;
                    if ((double)moveStrafe > 0.0) {
                        rotationYaw += (float)((double)moveForward > 0.0 ? -45 : 45);
                    } else if ((double)moveStrafe < 0.0) {
                        rotationYaw += (float)((double)moveForward > 0.0 ? 45 : -45);
                    }
                    moveStrafe = 0.0f;
                    float f2 = moveForward == 0.0f ? moveForward : (f = (moveForward = (double)moveForward > 0.0 ? 1.0f : -1.0f));
                }
                moveStrafe = moveStrafe == 0.0f ? moveStrafe : ((double)moveStrafe > 0.0 ? 1.0f : -1.0f);
                event.setX((double)moveForward * EntityUtil.getMaxSpeed() * Math.cos(Math.toRadians(rotationYaw + 90.0f)) + (double)moveStrafe * EntityUtil.getMaxSpeed() * Math.sin(Math.toRadians(rotationYaw + 90.0f)));
                event.setZ((double)moveForward * EntityUtil.getMaxSpeed() * Math.sin(Math.toRadians(rotationYaw + 90.0f)) - (double)moveStrafe * EntityUtil.getMaxSpeed() * Math.cos(Math.toRadians(rotationYaw + 90.0f)));
            }
        }
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().equals(this.mode) && this.mode.getPlannedValue() == Mode.INSTANT) {
            Strafe.mc.player.motionY = -0.1;
        }
    }

    private boolean shouldReturn() {
        return OyVey.moduleManager.isModuleEnabled("Freecam") || OyVey.moduleManager.isModuleEnabled("ElytraFlight") || OyVey.moduleManager.isModuleEnabled("Flight");
    }

    public static enum Mode {
        NCP,
        INSTANT;

    }
}

