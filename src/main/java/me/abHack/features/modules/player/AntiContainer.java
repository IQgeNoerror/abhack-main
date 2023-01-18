/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockShulkerBox
 *  net.minecraft.client.Minecraft
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.abHack.features.modules.player;

import me.abHack.event.events.PacketEvent;
import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiContainer
extends Module {
    public Setting<Boolean> Chest = this.register(new Setting<Boolean>("Chest", true));
    public Setting<Boolean> EnderChest = this.register(new Setting<Boolean>("EnderChest", true));
    public Setting<Boolean> Trapped_Chest = this.register(new Setting<Boolean>("Trapped_Chest", true));
    public Setting<Boolean> Hopper = this.register(new Setting<Boolean>("Hopper", true));
    public Setting<Boolean> Dispenser = this.register(new Setting<Boolean>("Dispenser", true));
    public Setting<Boolean> Furnace = this.register(new Setting<Boolean>("Furnace", true));
    public Setting<Boolean> Beacon = this.register(new Setting<Boolean>("Beacon", true));
    public Setting<Boolean> Crafting_Table = this.register(new Setting<Boolean>("Crafting_Table", true));
    public Setting<Boolean> Anvil = this.register(new Setting<Boolean>("Anvil", true));
    public Setting<Boolean> Enchanting_table = this.register(new Setting<Boolean>("Enchanting_table", true));
    public Setting<Boolean> Brewing_Stand = this.register(new Setting<Boolean>("Brewing_Stand", true));
    public Setting<Boolean> ShulkerBox = this.register(new Setting<Boolean>("ShulkerBox", true));

    public AntiContainer() {
        super("AntiContainer", "Do not display containers", Module.Category.PLAYER, true, false, false);
    }

    @SubscribeEvent
    public void onCheck(PacketEvent.Send packet) {
        BlockPos pos;
        if (packet.packet instanceof CPacketPlayerTryUseItemOnBlock && this.check(pos = ((CPacketPlayerTryUseItemOnBlock)packet.packet).getPos())) {
            packet.setCanceled(true);
        }
    }

    public boolean check(BlockPos pos) {
        return Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.CHEST && this.Chest.getValue() != false || Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.ENDER_CHEST && this.EnderChest.getValue() != false || Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.TRAPPED_CHEST && this.Trapped_Chest.getValue() != false || Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.HOPPER && this.Hopper.getValue() != false || Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.DISPENSER && this.Dispenser.getValue() != false || Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.FURNACE && this.Furnace.getValue() != false || Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.BEACON && this.Beacon.getValue() != false || Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.CRAFTING_TABLE && this.Crafting_Table.getValue() != false || Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.ANVIL && this.Anvil.getValue() != false || Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.ENCHANTING_TABLE && this.Enchanting_table.getValue() != false || Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.BREWING_STAND && this.Brewing_Stand.getValue() != false || Minecraft.getMinecraft().world.getBlockState(pos).getBlock() instanceof BlockShulkerBox && this.ShulkerBox.getValue() != false;
    }
}

