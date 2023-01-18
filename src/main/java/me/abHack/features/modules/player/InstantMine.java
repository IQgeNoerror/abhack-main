/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Enchantments
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.World
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.abHack.features.modules.player;

import java.awt.Color;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import me.abHack.event.events.PacketEvent;
import me.abHack.event.events.PlayerDamageBlockEvent;
import me.abHack.event.events.Render3DEvent;
import me.abHack.features.modules.Module;
import me.abHack.features.modules.client.ClickGui;
import me.abHack.features.modules.combat.AntiBurrow;
import me.abHack.features.setting.Bind;
import me.abHack.features.setting.Setting;
import me.abHack.util.BlockUtil;
import me.abHack.util.ColorUtil;
import me.abHack.util.InventoryUtil;
import me.abHack.util.MathUtil;
import me.abHack.util.RenderUtil;
import me.abHack.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class InstantMine
extends Module {
    public static BlockPos breakPos2;
    public static BlockPos breakPos;
    private static InstantMine INSTANCE;
    private final Timer breakSuccess = new Timer();
    public final Timer Rendertimer = new Timer();
    private final Setting<Boolean> creativeMode = this.register(new Setting<Boolean>("CreativeMode", true));
    private final Setting<Float> range = this.register(new Setting<Float>("Range", Float.valueOf(256.0f), Float.valueOf(1.0f), Float.valueOf(256.0f)));
    private final Setting<Boolean> ghostHand = this.register(new Setting<Object>("GhostHand", Boolean.valueOf(true), v -> this.creativeMode.getValue()));
    private final Setting<Boolean> superghost = this.register(new Setting<Object>("Super GhostHand", Boolean.FALSE, v -> this.ghostHand.getValue()));
    private final Setting<Boolean> doubleBreak = this.register(new Setting<Object>("Double Break", Boolean.FALSE, v -> this.ghostHand.getValue()));
    private final Setting<Boolean> crystal = this.register(new Setting<Boolean>("Crystal", Boolean.FALSE));
    public final Setting<Boolean> attackcrystal = this.register(new Setting<Object>("Attack Crystal", Boolean.FALSE, v -> this.crystal.getValue()));
    public final Setting<Bind> bind = this.register(new Setting<Object>("ObsidianBind", new Bind(-1), v -> this.crystal.getValue()));
    private final Setting<Boolean> instant = this.register(new Setting<Boolean>("Instant", true));
    private final Setting<Boolean> render = this.register(new Setting<Boolean>("Render", true));
    public Setting<Mode> rendermode = this.register(new Setting<Mode>("Render Mode", Mode.Fill, v -> this.render.getValue()));
    private final Setting<Integer> fillAlpha = this.register(new Setting<Object>("Fill Alpha", Integer.valueOf(80), Integer.valueOf(0), Integer.valueOf(255), v -> this.render.getValue() != false && this.rendermode.getValue() == Mode.Fill));
    private final Setting<Integer> boxAlpha = this.register(new Setting<Object>("Box Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.render.getValue() != false && this.rendermode.getValue() == Mode.Box));
    private final Setting<Boolean> rainbow = this.register(new Setting<Object>("Rainbow", Boolean.valueOf(true), v -> this.render.getValue()));
    private final List<Block> godBlocks = Arrays.asList(Blocks.AIR, Blocks.FLOWING_LAVA, Blocks.LAVA, Blocks.FLOWING_WATER, Blocks.WATER, Blocks.BEDROCK);
    public Block block;
    private boolean cancelStart = false;
    private boolean empty = false;
    private EnumFacing facing;
    double manxi;

    public InstantMine() {
        super("InstantMine", "Instant Mine", Module.Category.PLAYER, true, false, false);
        this.setInstance();
    }

    public static InstantMine getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InstantMine();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (InstantMine.fullNullCheck()) {
            return;
        }
        if (InstantMine.mc.player.capabilities.isCreativeMode) {
            return;
        }
        if (!this.cancelStart) {
            return;
        }
        if (this.crystal.getValue().booleanValue() && this.attackcrystal.getValue().booleanValue() && InstantMine.mc.world.getBlockState(breakPos).getBlock() == Blocks.AIR) {
            InstantMine.attackcrystal();
        }
        if (this.bind.getValue().isDown() && this.crystal.getValue().booleanValue() && InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1 && InstantMine.mc.world.getBlockState(breakPos).getBlock() == Blocks.AIR) {
            int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            int old = InstantMine.mc.player.inventory.currentItem;
            this.switchToSlot(obbySlot);
            BlockUtil.placeBlock(breakPos, EnumHand.MAIN_HAND, false, true, false);
            this.switchToSlot(old);
        }
        if (breakPos != null && InstantMine.mc.player != null && InstantMine.mc.player.getDistanceSq(breakPos) > MathUtil.square(this.range.getValue().floatValue())) {
            breakPos = null;
            breakPos2 = null;
            this.cancelStart = false;
            return;
        }
        if (InstantMine.mc.world.getBlockState(breakPos).getBlock() == Blocks.AIR && !this.instant.getValue().booleanValue()) {
            breakPos = null;
            breakPos2 = null;
            this.cancelStart = false;
            return;
        }
        if (this.godBlocks.contains(InstantMine.mc.world.getBlockState(breakPos).getBlock())) {
            return;
        }
        if (InventoryUtil.getItemHotbar(Items.END_CRYSTAL) != -1 && this.crystal.getValue().booleanValue() && InstantMine.mc.world.getBlockState(breakPos).getBlock() == Blocks.OBSIDIAN && !this.check() && !breakPos.equals((Object)AntiBurrow.pos)) {
            BlockUtil.placeCrystalOnBlock(breakPos, EnumHand.MAIN_HAND, true, false, true);
        }
        if (this.ghostHand.getValue().booleanValue() || this.ghostHand.getValue().booleanValue() && this.superghost.getValue().booleanValue()) {
            float breakTime = InstantMine.mc.world.getBlockState(breakPos).getBlockHardness((World)InstantMine.mc.world, breakPos);
            int slotMain = InstantMine.mc.player.inventory.currentItem;
            if (!this.breakSuccess.passedMs((int)breakTime)) {
                return;
            }
            if (this.superghost.getValue().booleanValue() && InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) == -1) {
                for (int i = 9; i < 36; ++i) {
                    if (InstantMine.mc.player.inventory.getStackInSlot(i).getItem() != Items.DIAMOND_PICKAXE) continue;
                    InstantMine.mc.playerController.windowClick(InstantMine.mc.player.inventoryContainer.windowId, i, InstantMine.mc.player.inventory.currentItem, ClickType.SWAP, (EntityPlayer)InstantMine.mc.player);
                    InstantMine.mc.playerController.updateController();
                    InstantMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, breakPos, this.facing));
                    InstantMine.mc.playerController.windowClick(InstantMine.mc.player.inventoryContainer.windowId, i, InstantMine.mc.player.inventory.currentItem, ClickType.SWAP, (EntityPlayer)InstantMine.mc.player);
                    InstantMine.mc.playerController.updateController();
                    return;
                }
            }
            try {
                this.block = InstantMine.mc.world.getBlockState(breakPos).getBlock();
            }
            catch (Exception i) {
                // empty catch block
            }
            int toolSlot = this.getBestAvailableToolSlot(this.block.getBlockState().getBaseState());
            if (InstantMine.mc.player.inventory.currentItem != toolSlot && toolSlot != -1) {
                InstantMine.mc.player.inventory.currentItem = toolSlot;
                InstantMine.mc.playerController.updateController();
                InstantMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, breakPos, this.facing));
                InstantMine.mc.player.inventory.currentItem = slotMain;
                InstantMine.mc.playerController.updateController();
                return;
            }
        }
        InstantMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, breakPos, this.facing));
    }

    @Override
    public void onRender3D(final Render3DEvent event) {
        if (fullNullCheck()) {
            return;
        }
        if (!this.cancelStart) {
            return;
        }
        if ((InstantMine.breakPos != null || (this.instant.getValue() && InstantMine.mc.world.getBlockState(InstantMine.breakPos).getBlock() == Blocks.AIR)) && InstantMine.mc.player != null && InstantMine.mc.player.getDistanceSq(InstantMine.breakPos) > MathUtil.square(this.range.getValue())) {
            InstantMine.breakPos = null;
            InstantMine.breakPos2 = null;
            this.cancelStart = false;
            return;
        }
        if (this.doubleBreak.getValue() && this.ghostHand.getValue() && InstantMine.breakPos2 != null) {
            final int slotMains = InstantMine.mc.player.inventory.currentItem;
            if (InstantMine.mc.world.getBlockState(InstantMine.breakPos2).getBlock() != Blocks.AIR) {
                if (InstantMine.mc.world.getBlockState(InstantMine.breakPos2).getBlock() == Blocks.OBSIDIAN && !this.breakSuccess.passedMs(1234L)) {
                    return;
                }
                InstantMine.breakPos2 = InstantMine.breakPos;
                try {
                    this.block = InstantMine.mc.world.getBlockState(InstantMine.breakPos2).getBlock();
                }
                catch (Exception ex) {}
                final int toolSlot = this.getBestAvailableToolSlot(this.block.getBlockState().getBaseState());
                if (InstantMine.mc.player.inventory.currentItem != toolSlot && toolSlot != -1) {
                    InstantMine.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(toolSlot));
                    InstantMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, InstantMine.breakPos2, this.facing));
                }
            }
            if (InstantMine.mc.world.getBlockState(InstantMine.breakPos2).getBlock() == Blocks.AIR) {
                InstantMine.breakPos2 = null;
                InstantMine.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(slotMains));
            }
        }
    }

    public int getBestAvailableToolSlot(IBlockState blockState) {
        int toolSlot = -1;
        double max = 0.0;
        for (int i = 0; i < 9; ++i) {
            float f = 0;
            float f2 = 0;
            ItemStack stack = InstantMine.mc.player.inventory.getStackInSlot(i);
            if (stack.isEmpty) continue;
            float speed = stack.getDestroySpeed(blockState);
            if (!(f2 > 1.0f)) continue;
            int eff = EnchantmentHelper.getEnchantmentLevel((Enchantment)Enchantments.EFFICIENCY, (ItemStack)stack);
            speed = (float)((double)speed + (eff > 0 ? Math.pow(eff, 2.0) + 1.0 : 0.0));
            if (!((double)f > max)) continue;
            max = speed;
            toolSlot = i;
        }
        return toolSlot;
    }

    public static void attackcrystal() {
        for (Entity crystal : InstantMine.mc.world.loadedEntityList.stream().filter(e -> e instanceof EntityEnderCrystal && !e.isDead).sorted(Comparator.comparing(e -> Float.valueOf(InstantMine.mc.player.getDistance(e)))).collect(Collectors.toList())) {
            if (!(crystal instanceof EntityEnderCrystal) || !(crystal.getDistanceSq(breakPos) <= 2.0)) continue;
            InstantMine.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(crystal));
            InstantMine.mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.OFF_HAND));
        }
    }

    public boolean check() {
        return breakPos.equals((Object)new BlockPos(InstantMine.mc.player.posX, InstantMine.mc.player.posY + 2.0, InstantMine.mc.player.posZ)) || breakPos.equals((Object)new BlockPos(InstantMine.mc.player.posX, InstantMine.mc.player.posY + 3.0, InstantMine.mc.player.posZ)) || breakPos.equals((Object)new BlockPos(InstantMine.mc.player.posX, InstantMine.mc.player.posY - 1.0, InstantMine.mc.player.posZ)) || breakPos.equals((Object)new BlockPos(InstantMine.mc.player.posX + 1.0, InstantMine.mc.player.posY, InstantMine.mc.player.posZ)) || breakPos.equals((Object)new BlockPos(InstantMine.mc.player.posX - 1.0, InstantMine.mc.player.posY, InstantMine.mc.player.posZ)) || breakPos.equals((Object)new BlockPos(InstantMine.mc.player.posX, InstantMine.mc.player.posY, InstantMine.mc.player.posZ + 1.0)) || breakPos.equals((Object)new BlockPos(InstantMine.mc.player.posX, InstantMine.mc.player.posY, InstantMine.mc.player.posZ - 1.0)) || breakPos.equals((Object)new BlockPos(InstantMine.mc.player.posX + 1.0, InstantMine.mc.player.posY + 1.0, InstantMine.mc.player.posZ)) || breakPos.equals((Object)new BlockPos(InstantMine.mc.player.posX - 1.0, InstantMine.mc.player.posY + 1.0, InstantMine.mc.player.posZ)) || breakPos.equals((Object)new BlockPos(InstantMine.mc.player.posX, InstantMine.mc.player.posY + 1.0, InstantMine.mc.player.posZ + 1.0)) || breakPos.equals((Object)new BlockPos(InstantMine.mc.player.posX, InstantMine.mc.player.posY + 1.0, InstantMine.mc.player.posZ - 1.0));
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        CPacketPlayerDigging packet;
        if (InstantMine.fullNullCheck()) {
            return;
        }
        if (InstantMine.mc.player.capabilities.isCreativeMode) {
            return;
        }
        if (event.getPacket() instanceof CPacketPlayerDigging && (packet = (CPacketPlayerDigging)event.getPacket()).getAction() == CPacketPlayerDigging.Action.START_DESTROY_BLOCK) {
            event.setCanceled(this.cancelStart);
        }
    }

    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent e) {
        if (fullNullCheck()) {
            return;
        }
        if (this.render.getValue() && this.creativeMode.getValue() && this.cancelStart) {
            if (this.godBlocks.contains(InstantMine.mc.world.getBlockState(InstantMine.breakPos).getBlock())) {
                this.empty = true;
            }
            float RenderTime = InstantMine.mc.world.getBlockState(InstantMine.breakPos).getBlockHardness((World)InstantMine.mc.world, InstantMine.breakPos) / 5.0f;
            if (InstantMine.mc.world.getBlockState(InstantMine.breakPos).getBlock() == Blocks.OBSIDIAN) {
                RenderTime = 11.0f;
            }
            if (this.Rendertimer.passedMs((int)RenderTime)) {
                if (this.manxi <= 10.0) {
                    this.manxi += 0.11;
                }
                this.Rendertimer.reset();
            }
            final AxisAlignedBB axisAlignedBB = InstantMine.mc.world.getBlockState(InstantMine.breakPos).getSelectedBoundingBox((World)InstantMine.mc.world, InstantMine.breakPos);
            final double centerX = axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) / 2.0;
            final double centerY = axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) / 2.0;
            final double centerZ = axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) / 2.0;
            final double progressValX = this.manxi * (axisAlignedBB.maxX - centerX) / 10.0;
            final double progressValY = this.manxi * (axisAlignedBB.maxY - centerY) / 10.0;
            final double progressValZ = this.manxi * (axisAlignedBB.maxZ - centerZ) / 10.0;
            final AxisAlignedBB axisAlignedBB2 = new AxisAlignedBB(centerX - progressValX, centerY - progressValY, centerZ - progressValZ, centerX + progressValX, centerY + progressValY, centerZ + progressValZ);
            final Color color = this.rainbow.getValue() ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.empty ? 0 : 255, this.empty ? 255 : 0, 0, 255);
            if (this.rendermode.getValue() == Mode.Fill) {
                RenderUtil.drawBBFill(axisAlignedBB2, color, this.fillAlpha.getValue());
            }
            else {
                RenderUtil.drawBBBox(axisAlignedBB2, color, this.boxAlpha.getValue());
            }
        }
    }

    @SubscribeEvent
    public void onBlockEvent(PlayerDamageBlockEvent event) {
        if (InstantMine.fullNullCheck()) {
            return;
        }
        if (breakPos != null && breakPos.getX() == event.getPos().getX() && breakPos.getY() == event.getPos().getY() && breakPos.getZ() == event.getPos().getZ()) {
            return;
        }
        if (breakPos2 != null && breakPos2.getX() == event.getPos().getX() && breakPos2.getY() == event.getPos().getY() && breakPos2.getZ() == event.getPos().getZ() && this.doubleBreak.getValue().booleanValue()) {
            return;
        }
        if (InstantMine.mc.player.capabilities.isCreativeMode) {
            return;
        }
        if (BlockUtil.canBreak(event.pos)) {
            this.manxi = 0.0;
            breakPos2 = breakPos;
            this.empty = false;
            this.cancelStart = false;
            breakPos = event.pos;
            this.breakSuccess.reset();
            this.facing = event.facing;
            if (breakPos != null) {
                InstantMine.mc.player.swingArm(EnumHand.MAIN_HAND);
                InstantMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, breakPos, this.facing));
                this.cancelStart = true;
                InstantMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, breakPos, this.facing));
                event.setCanceled(true);
            }
        }
    }

    private void switchToSlot(int slot) {
        InstantMine.mc.player.inventory.currentItem = slot;
        InstantMine.mc.playerController.updateController();
    }

    @Override
    public String getDisplayInfo() {
        return this.ghostHand.getValue() != false ? "Ghost" : "Normal";
    }

    static {
        INSTANCE = new InstantMine();
    }

    public static enum Mode {
        Fill,
        Box;

    }
}

