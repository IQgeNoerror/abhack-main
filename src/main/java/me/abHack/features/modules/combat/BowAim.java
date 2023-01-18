/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemBow
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 */
package me.abHack.features.modules.combat;

import me.abHack.OyVey;
import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class BowAim
extends Module {
    public final Setting<Boolean> packet = this.register(new Setting<Boolean>("PacketRotate", true));
    public final Setting<Boolean> fastbow = this.register(new Setting<Boolean>("FastBow", true));

    public BowAim() {
        super("BowAim", "Automatically aims your bow at your opponent", Module.Category.COMBAT, true, false, false);
    }

    public static Vec3d interpolateEntity(Entity entity, float time) {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)time, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)time, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)time);
    }

    public static float[] calcAngle(Vec3d from, Vec3d to) {
        double difX = to.x - from.x;
        double difY = (to.y - from.y) * -1.0;
        double difZ = to.z - from.z;
        double dist = MathHelper.sqrt((double)(difX * difX + difZ * difZ));
        return new float[]{(float)MathHelper.wrapDegrees((double)(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0)), (float)MathHelper.wrapDegrees((double)Math.toDegrees(Math.atan2(difY, dist)))};
    }

    @Override
    public void onUpdate() {
        if (BowAim.mc.player.getHeldItemMainhand().getItem() instanceof ItemBow && BowAim.mc.player.isHandActive() && BowAim.mc.player.getItemInUseMaxCount() >= 3) {
            EntityPlayer player = null;
            float tickDis = 100.0f;
            for (EntityPlayer p : BowAim.mc.world.playerEntities) {
                float f = 0;
                if (p instanceof EntityPlayerSP || OyVey.friendManager.isFriend(p.getName())) continue;
                float dis = p.getDistance((Entity)BowAim.mc.player);
                if (f >= tickDis) continue;
                tickDis = dis;
                player = p;
            }
            if (player != null) {
                Vec3d pos = BowAim.interpolateEntity(player, mc.getRenderPartialTicks());
                float[] angels = BowAim.calcAngle(BowAim.interpolateEntity((Entity)BowAim.mc.player, mc.getRenderPartialTicks()), pos);
                if (this.packet.getValue().booleanValue()) {
                    BowAim.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(angels[0], angels[1], BowAim.mc.player.onGround));
                } else {
                    BowAim.mc.player.rotationYaw = angels[0];
                    BowAim.mc.player.rotationPitch = angels[1];
                }
            }
        }
        if (BowAim.mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && BowAim.mc.player.isHandActive() && BowAim.mc.player.getItemInUseMaxCount() >= 3 && this.fastbow.getValue().booleanValue()) {
            BowAim.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, BowAim.mc.player.getHorizontalFacing()));
            BowAim.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(BowAim.mc.player.getActiveHand()));
            BowAim.mc.player.stopActiveHand();
        }
    }
}

