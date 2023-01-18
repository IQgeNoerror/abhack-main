package me.abHack.features.modules.player;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import me.abHack.event.events.PacketEvent;
import me.abHack.features.setting.Setting;
import me.abHack.features.modules.Module;

public class AntiHunger extends Module
{
    public Setting<Boolean> cancelSprint;
    public Setting<Boolean> ground;

    public AntiHunger() {
        super("AntiHunger", "Prevents you from getting Hungry.", Category.PLAYER, true, false, false);
        this.cancelSprint = (Setting<Boolean>)this.register(new Setting("CancelSprint", true));
        this.ground = (Setting<Boolean>)this.register(new Setting("Ground", true));
    }

    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (this.ground.getValue() && event.getPacket() instanceof CPacketPlayer) {
            final CPacketPlayer packet = event.getPacket();
            packet.onGround = (AntiHunger.mc.player.fallDistance >= 0.0f || AntiHunger.mc.playerController.isHittingBlock);
        }
        if (this.cancelSprint.getValue() && event.getPacket() instanceof CPacketEntityAction) {
            final CPacketEntityAction packet2 = event.getPacket();
            if (packet2.getAction() == CPacketEntityAction.Action.START_SPRINTING || packet2.getAction() == CPacketEntityAction.Action.STOP_SPRINTING) {
                event.setCanceled(true);
            }
        }
    }
}