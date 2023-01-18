/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.util.concurrent.AtomicDouble
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.tileentity.TileEntityBed
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.abHack.features.modules.combat;

import com.google.common.util.concurrent.AtomicDouble;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import me.abHack.OyVey;
import me.abHack.event.events.PacketEvent;
import me.abHack.event.events.UpdateWalkingPlayerEvent;
import me.abHack.features.command.Command;
import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import me.abHack.util.BlockUtil;
import me.abHack.util.DamageUtil;
import me.abHack.util.EntityUtil;
import me.abHack.util.InventoryUtil;
import me.abHack.util.MathUtil;
import me.abHack.util.RotationUtil;
import me.abHack.util.Timer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BedAura
extends Module {
    private final Setting<Boolean> place = this.register(new Setting<Boolean>("Place", true));
    private final Setting<Integer> placeDelay = this.register(new Setting<Object>("Placedelay", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(500), v -> this.place.getValue()));
    private final Setting<Float> placeRange = this.register(new Setting<Object>("PlaceRange", Float.valueOf(6.0f), Float.valueOf(1.0f), Float.valueOf(10.0f), v -> this.place.getValue()));
    private final Setting<Boolean> extraPacket = this.register(new Setting<Object>("InsanePacket", Boolean.valueOf(false), v -> this.place.getValue()));
    private final Setting<Boolean> packet = this.register(new Setting<Object>("Packet", Boolean.valueOf(false), v -> this.place.getValue()));
    private final Setting<Boolean> explode = this.register(new Setting<Boolean>("Break", true));
    private final Setting<BreakLogic> breakMode = this.register(new Setting<Object>("BreakMode", (Object)BreakLogic.ALL, v -> this.explode.getValue()));
    private final Setting<Integer> breakDelay = this.register(new Setting<Object>("Breakdelay", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(500), v -> this.explode.getValue()));
    private final Setting<Float> breakRange = this.register(new Setting<Object>("BreakRange", Float.valueOf(6.0f), Float.valueOf(1.0f), Float.valueOf(10.0f), v -> this.explode.getValue()));
    private final Setting<Float> minDamage = this.register(new Setting<Object>("MinDamage", Float.valueOf(5.0f), Float.valueOf(1.0f), Float.valueOf(36.0f), v -> this.explode.getValue()));
    private final Setting<Float> range = this.register(new Setting<Object>("Range", Float.valueOf(10.0f), Float.valueOf(1.0f), Float.valueOf(12.0f), v -> this.explode.getValue()));
    private final Setting<Boolean> suicide = this.register(new Setting<Object>("Suicide", Boolean.valueOf(false), v -> this.explode.getValue()));
    private final Setting<Boolean> removeTiles = this.register(new Setting<Boolean>("RemoveTiles", false));
    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", false));
    private final Setting<Boolean> oneDot15 = this.register(new Setting<Boolean>("1.15", false));
    private final Setting<Boolean> dimensionCheck = this.register(new Setting<Boolean>("Dimension Check", true));
    private final Setting<SwitchModes> switchMode = this.register(new Setting<SwitchModes>("Switch Mode", SwitchModes.SILENT));
    private final Setting<Logic> logic = this.register(new Setting<Object>("Logic", (Object)Logic.BREAKPLACE, v -> this.place.getValue() != false && this.explode.getValue() != false));
    private final Timer breakTimer = new Timer();
    private final Timer placeTimer = new Timer();
    private final AtomicDouble yaw = new AtomicDouble(-1.0);
    private final AtomicDouble pitch = new AtomicDouble(-1.0);
    private final AtomicBoolean shouldRotate = new AtomicBoolean(false);
    private EntityPlayer target = null;
    private boolean sendRotationPacket = false;
    private boolean one;
    private boolean two;
    private boolean three;
    private boolean four;
    private boolean five;
    private boolean six;
    private BlockPos maxPos = null;
    private int lastHotbarSlot = -1;
    private int bedSlot = -1;
    private BlockPos finalPos;
    private EnumFacing finalFacing;

    public BedAura() {
        super("BedAura", "AutoPlace and Break for beds", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onUpdate() {
        if (this.dimensionCheck.getValue().booleanValue() && BedAura.mc.player.dimension == 0) {
            Command.sendMessage(ChatFormatting.WHITE + "<WorldCheck> You are in the Overworld! Toggling Off!");
            this.disable();
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Send event) {
        if (this.shouldRotate.get() && event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            packet.yaw = (float)this.yaw.get();
            packet.pitch = (float)this.pitch.get();
            this.shouldRotate.set(false);
        }
    }

    private void switchToSlot(int slot) {
        BedAura.mc.player.inventory.currentItem = slot;
        BedAura.mc.playerController.updateController();
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0) {
            this.doBedAura();
        } else if (event.getStage() == 1 && this.finalPos != null) {
            Vec3d hitVec = new Vec3d((Vec3i)this.finalPos.down()).add(0.5, 0.5, 0.5).add(new Vec3d(this.finalFacing.getOpposite().getDirectionVec()).scale(0.5));
            BedAura.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BedAura.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            switch (this.switchMode.getValue()) {
                case NORMAL: {
                    InventoryUtil.switchToHotbarSlot(this.bedSlot, false);
                }
            }
            if (InventoryUtil.getItemHotbar(Items.BED) != -1) {
                if (this.switchMode.getValue() == SwitchModes.SILENT) {
                    int old = BedAura.mc.player.inventory.currentItem;
                    this.switchToSlot(this.bedSlot);
                    BlockUtil.rightClickBlock(this.finalPos.down(), hitVec, this.bedSlot == -2 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, EnumFacing.UP, this.packet.getValue());
                    BedAura.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BedAura.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                    this.switchToSlot(old);
                } else {
                    BlockUtil.rightClickBlock(this.finalPos.down(), hitVec, this.bedSlot == -2 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, EnumFacing.UP, this.packet.getValue());
                    BedAura.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BedAura.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                }
            }
            this.placeTimer.reset();
            this.finalPos = null;
        }
    }

    public void recheckBedSlots(int woolSlot, int woodSlot) {
        int i;
        for (i = 1; i <= 3; ++i) {
            if (BedAura.mc.player.openContainer.getInventory().get(i) != ItemStack.EMPTY) continue;
            BedAura.mc.playerController.windowClick(1, woolSlot, 0, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
            BedAura.mc.playerController.windowClick(1, i, 1, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
            BedAura.mc.playerController.windowClick(1, woolSlot, 0, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
        }
        for (i = 4; i <= 6; ++i) {
            if (BedAura.mc.player.openContainer.getInventory().get(i) != ItemStack.EMPTY) continue;
            BedAura.mc.playerController.windowClick(1, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
            BedAura.mc.playerController.windowClick(1, i, 1, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
            BedAura.mc.playerController.windowClick(1, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)BedAura.mc.player);
        }
    }

    private void doBedAura() {
        switch (this.logic.getValue()) {
            case BREAKPLACE: {
                this.mapBeds();
                this.breakBeds();
                this.placeBeds();
                break;
            }
            case PLACEBREAK: {
                this.mapBeds();
                this.placeBeds();
                this.breakBeds();
            }
        }
    }

    private void breakBeds() {
        if (this.explode.getValue().booleanValue() && this.breakTimer.passedMs(this.breakDelay.getValue().intValue())) {
            if (this.breakMode.getValue() == BreakLogic.CALC) {
                if (this.maxPos != null) {
                    RayTraceResult result;
                    Vec3d hitVec = new Vec3d((Vec3i)this.maxPos).add(0.5, 0.5, 0.5);
                    float[] rotations = RotationUtil.getLegitRotations(hitVec);
                    this.yaw.set((double)rotations[0]);
                    if (this.rotate.getValue().booleanValue()) {
                        this.shouldRotate.set(true);
                        this.pitch.set((double)rotations[1]);
                    }
                    EnumFacing facing = (result = BedAura.mc.world.rayTraceBlocks(new Vec3d(BedAura.mc.player.posX, BedAura.mc.player.posY + (double)BedAura.mc.player.getEyeHeight(), BedAura.mc.player.posZ), new Vec3d((double)this.maxPos.getX() + 0.5, (double)this.maxPos.getY() - 0.5, (double)this.maxPos.getZ() + 0.5))) == null || result.sideHit == null ? EnumFacing.UP : result.sideHit;
                    BlockUtil.rightClickBlock(this.maxPos, hitVec, EnumHand.MAIN_HAND, facing, true);
                    this.breakTimer.reset();
                }
            } else {
                for (TileEntity entityBed : BedAura.mc.world.loadedTileEntityList) {
                    RayTraceResult result;
                    if (!(entityBed instanceof TileEntityBed) || BedAura.mc.player.getDistanceSq(entityBed.getPos()) > MathUtil.square(this.breakRange.getValue().floatValue())) continue;
                    Vec3d hitVec = new Vec3d((Vec3i)entityBed.getPos()).add(0.5, 0.5, 0.5);
                    float[] rotations = RotationUtil.getLegitRotations(hitVec);
                    this.yaw.set((double)rotations[0]);
                    if (this.rotate.getValue().booleanValue()) {
                        this.shouldRotate.set(true);
                        this.pitch.set((double)rotations[1]);
                    }
                    EnumFacing facing = (result = BedAura.mc.world.rayTraceBlocks(new Vec3d(BedAura.mc.player.posX, BedAura.mc.player.posY + (double)BedAura.mc.player.getEyeHeight(), BedAura.mc.player.posZ), new Vec3d((double)entityBed.getPos().getX() + 0.5, (double)entityBed.getPos().getY() - 0.5, (double)entityBed.getPos().getZ() + 0.5))) == null || result.sideHit == null ? EnumFacing.UP : result.sideHit;
                    BlockUtil.rightClickBlock(entityBed.getPos(), hitVec, EnumHand.MAIN_HAND, facing, true);
                    this.breakTimer.reset();
                }
            }
        }
    }

    private void mapBeds() {
        this.maxPos = null;
        float maxDamage = 0.5f;
        if (this.removeTiles.getValue().booleanValue()) {
            ArrayList<BedData> removedBlocks = new ArrayList<BedData>();
            for (TileEntity tile : BedAura.mc.world.loadedTileEntityList) {
                if (!(tile instanceof TileEntityBed)) continue;
                TileEntityBed bed = (TileEntityBed)tile;
                BedData data = new BedData(tile.getPos(), BedAura.mc.world.getBlockState(tile.getPos()), bed, bed.isHeadPiece());
                removedBlocks.add(data);
            }
            for (BedData data : removedBlocks) {
                BedAura.mc.world.setBlockToAir(data.getPos());
            }
            for (BedData data : removedBlocks) {
                float f = 0;
                BlockPos pos;
                if (!data.isHeadPiece() || BedAura.mc.player.getDistanceSq(pos = data.getPos()) > MathUtil.square(this.breakRange.getValue().floatValue())) continue;
                float selfDamage = DamageUtil.calculateDamage(pos, (Entity)BedAura.mc.player);
                if ((double)f + 1.0 >= (double)EntityUtil.getHealth((Entity)BedAura.mc.player) && DamageUtil.canTakeDamage(this.suicide.getValue())) continue;
                for (EntityPlayer player : BedAura.mc.world.playerEntities) {
                    float f2 = 0;
                    if (player.getDistanceSq(pos) >= MathUtil.square(this.range.getValue().floatValue()) || !EntityUtil.isValid((Entity)player, this.range.getValue().floatValue() + this.breakRange.getValue().floatValue())) continue;
                    float damage = DamageUtil.calculateDamage(pos, (Entity)player);
                    if (f2 <= selfDamage && (damage <= this.minDamage.getValue().floatValue() || DamageUtil.canTakeDamage(this.suicide.getValue())) && damage <= EntityUtil.getHealth((Entity)player) || damage <= maxDamage) continue;
                    maxDamage = damage;
                    this.maxPos = pos;
                }
            }
            for (BedData data : removedBlocks) {
                BedAura.mc.world.setBlockState(data.getPos(), data.getState());
            }
        } else {
            for (TileEntity tile : BedAura.mc.world.loadedTileEntityList) {
                float f = 0;
                BlockPos pos;
                TileEntityBed bed;
                if (!(tile instanceof TileEntityBed) || !(bed = (TileEntityBed)tile).isHeadPiece() || BedAura.mc.player.getDistanceSq(pos = bed.getPos()) > MathUtil.square(this.breakRange.getValue().floatValue())) continue;
                float selfDamage = DamageUtil.calculateDamage(pos, (Entity)BedAura.mc.player);
                if ((double)f + 1.0 >= (double)EntityUtil.getHealth((Entity)BedAura.mc.player) && DamageUtil.canTakeDamage(this.suicide.getValue())) continue;
                for (EntityPlayer player : BedAura.mc.world.playerEntities) {
                    float f3 = 0;
                    if (player.getDistanceSq(pos) >= MathUtil.square(this.range.getValue().floatValue()) || !EntityUtil.isValid((Entity)player, this.range.getValue().floatValue() + this.breakRange.getValue().floatValue())) continue;
                    float damage = DamageUtil.calculateDamage(pos, (Entity)player);
                    if (f3 <= selfDamage && (damage <= this.minDamage.getValue().floatValue() || DamageUtil.canTakeDamage(this.suicide.getValue())) && damage <= EntityUtil.getHealth((Entity)player) || damage <= maxDamage) continue;
                    maxDamage = damage;
                    this.maxPos = pos;
                }
            }
        }
    }

    private void placeBeds() {
        if (this.place.getValue().booleanValue() && this.placeTimer.passedMs(this.placeDelay.getValue().intValue()) && this.maxPos == null) {
            this.bedSlot = this.findBedSlot();
            if (this.bedSlot == -1 && BedAura.mc.player.getHeldItemOffhand().getItem() == Items.BED) {
                this.bedSlot = -2;
            }
            this.lastHotbarSlot = BedAura.mc.player.inventory.currentItem;
            this.target = EntityUtil.getClosestEnemy(this.placeRange.getValue().floatValue());
            if (this.target != null) {
                BlockPos targetPos = new BlockPos(this.target.getPositionVector());
                this.placeBed(targetPos, true);
            }
        }
    }

    private void placeBed(BlockPos pos, boolean firstCheck) {
        if (BedAura.mc.world.getBlockState(pos).getBlock() == Blocks.BED) {
            return;
        }
        float damage = DamageUtil.calculateDamage(pos, (Entity)BedAura.mc.player);
        if ((double)damage > (double)EntityUtil.getHealth((Entity)BedAura.mc.player) + 0.5) {
            if (firstCheck && this.oneDot15.getValue().booleanValue()) {
                this.placeBed(pos.up(), false);
            }
            return;
        }
        if (!BedAura.mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
            if (firstCheck && this.oneDot15.getValue().booleanValue()) {
                this.placeBed(pos.up(), false);
            }
            return;
        }
        ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
        HashMap<BlockPos, EnumFacing> facings = new HashMap<BlockPos, EnumFacing>();
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos blockPos = null;
            if (facing == EnumFacing.DOWN || facing == EnumFacing.UP) continue;
            BlockPos position = pos.offset(facing);
            if (!(BedAura.mc.player.getDistanceSq(blockPos) <= MathUtil.square(this.placeRange.getValue().floatValue())) || !BedAura.mc.world.getBlockState(position).getMaterial().isReplaceable() || BedAura.mc.world.getBlockState(position.down()).getMaterial().isReplaceable()) continue;
            positions.add(position);
            facings.put(position, facing.getOpposite());
        }
        if (positions.isEmpty()) {
            if (firstCheck && this.oneDot15.getValue().booleanValue()) {
                this.placeBed(pos.up(), false);
            }
            return;
        }
        positions.sort(Comparator.comparingDouble(pos2 -> BedAura.mc.player.getDistanceSq(pos2)));
        this.finalPos = (BlockPos)positions.get(0);
        this.finalFacing = (EnumFacing)facings.get(this.finalPos);
        float[] rotation = RotationUtil.simpleFacing(this.finalFacing);
        if (!this.sendRotationPacket && this.extraPacket.getValue().booleanValue()) {
            RotationUtil.faceYawAndPitch(rotation[0], rotation[1]);
            this.sendRotationPacket = true;
        }
        this.yaw.set((double)rotation[0]);
        this.pitch.set((double)rotation[1]);
        this.shouldRotate.set(true);
        OyVey.rotationManager.setPlayerRotations(rotation[0], rotation[1]);
    }

    @Override
    public String getDisplayInfo() {
        if (this.target != null) {
            return this.target.getName();
        }
        return null;
    }

    @Override
    public void onToggle() {
        this.lastHotbarSlot = -1;
        this.bedSlot = -1;
        this.sendRotationPacket = false;
        this.target = null;
        this.yaw.set(-1.0);
        this.pitch.set(-1.0);
        this.shouldRotate.set(false);
    }

    private int findBedSlot() {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = BedAura.mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY || stack.getItem() != Items.BED) {
                continue;
            }
            return i;
        }
        return -1;
    }

    private int safety(BlockPos pos) {
        int safety = 0;
        for (EnumFacing facing : EnumFacing.values()) {
            if (BedAura.mc.world.getBlockState(pos.offset(facing)).getMaterial().isReplaceable()) continue;
            ++safety;
        }
        return safety;
    }

    public static class BedData {
        private final BlockPos pos;
        private final IBlockState state;
        private final boolean isHeadPiece;
        private final TileEntityBed entity;

        public BedData(BlockPos pos, IBlockState state, TileEntityBed bed, boolean isHeadPiece) {
            this.pos = pos;
            this.state = state;
            this.entity = bed;
            this.isHeadPiece = isHeadPiece;
        }

        public BlockPos getPos() {
            return this.pos;
        }

        public IBlockState getState() {
            return this.state;
        }

        public boolean isHeadPiece() {
            return this.isHeadPiece;
        }

        public TileEntityBed getEntity() {
            return this.entity;
        }
    }

    public static enum Logic {
        BREAKPLACE,
        PLACEBREAK;

    }

    public static enum BreakLogic {
        ALL,
        CALC;

    }

    public static enum SwitchModes {
        SILENT,
        NORMAL;

    }
}

