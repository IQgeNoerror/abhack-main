/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockSlab
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.IBlockAccess
 */
package me.abHack.features.modules.movement;

import java.util.List;
import me.abHack.OyVey;
import me.abHack.features.modules.Module;
import me.abHack.features.setting.Setting;
import me.abHack.util.MathUtil;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class HoleTP
extends Module {
    private static HoleTP INSTANCE = new HoleTP();
    private final Setting<Float> range = this.register(new Setting<Float>("Range", Float.valueOf(5.0f), Float.valueOf(0.0f), Float.valueOf(5.0f)));
    private final Setting<Boolean> setY = this.register(new Setting<Boolean>("SetY", false));
    private final Setting<Float> xOffset = this.register(new Setting<Float>("XOffset", Float.valueOf(0.5f), Float.valueOf(0.0f), Float.valueOf(1.0f)));
    private final Setting<Float> yOffset = this.register(new Setting<Float>("YOffset", Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f)));
    private final Setting<Float> zOffset = this.register(new Setting<Float>("ZOffset", Float.valueOf(0.5f), Float.valueOf(0.0f), Float.valueOf(1.0f)));
    private final double[] oneblockPositions = new double[]{0.42, 0.75};
    private final boolean jumped = false;
    private int packets;

    public HoleTP() {
        super("HoleTP", "Teleports you in a hole.", Module.Category.MOVEMENT, true, false, false);
        this.setInstance();
    }

    public static HoleTP getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HoleTP();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        BlockPos pos;
        OyVey.holeManager.update();
        List<BlockPos> holes = OyVey.holeManager.getSortedHoles();
        if (!holes.isEmpty() && HoleTP.mc.player.getDistanceSq(pos = holes.get(0)) <= MathUtil.square(this.range.getValue().floatValue())) {
            OyVey.positionManager.setPositionPacket((float)pos.getX() + this.xOffset.getValue().floatValue(), (this.setY.getValue() != false ? (double)pos.getY() : HoleTP.mc.player.posY) + (double)this.yOffset.getValue().floatValue(), (float)pos.getZ() + this.zOffset.getValue().floatValue(), this.setY.getValue() != false && this.yOffset.getValue().floatValue() == 0.0f, true, true);
        }
        this.disable();
    }

    private boolean isInHole() {
        BlockPos blockPos = new BlockPos(HoleTP.mc.player.posX, HoleTP.mc.player.posY, HoleTP.mc.player.posZ);
        IBlockState blockState = HoleTP.mc.world.getBlockState(blockPos);
        return this.isBlockValid(blockState, blockPos);
    }

    private double getNearestBlockBelow() {
        for (double y = HoleTP.mc.player.posY; y > 0.0; y -= 0.001) {
            if (HoleTP.mc.world.getBlockState(new BlockPos(HoleTP.mc.player.posX, y, HoleTP.mc.player.posZ)).getBlock() instanceof BlockSlab || HoleTP.mc.world.getBlockState(new BlockPos(HoleTP.mc.player.posX, y, HoleTP.mc.player.posZ)).getBlock().getDefaultState().getCollisionBoundingBox((IBlockAccess)HoleTP.mc.world, new BlockPos(0, 0, 0)) == null) continue;
            return y;
        }
        return -1.0;
    }

    private boolean isBlockValid(IBlockState blockState, BlockPos blockPos) {
        if (blockState.getBlock() != Blocks.AIR) {
            return false;
        }
        if (HoleTP.mc.player.getDistanceSq(blockPos) < 1.0) {
            return false;
        }
        if (HoleTP.mc.world.getBlockState(blockPos.up()).getBlock() != Blocks.AIR) {
            return false;
        }
        if (HoleTP.mc.world.getBlockState(blockPos.up(2)).getBlock() != Blocks.AIR) {
            return false;
        }
        return this.isBedrockHole(blockPos) || this.isObbyHole(blockPos) || this.isBothHole(blockPos) || this.isElseHole(blockPos);
    }

    private boolean isObbyHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks;
        for (BlockPos pos : touchingBlocks = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()}) {
            IBlockState touchingState = HoleTP.mc.world.getBlockState(pos);
            if (touchingState.getBlock() != Blocks.AIR && touchingState.getBlock() == Blocks.OBSIDIAN) continue;
            return false;
        }
        return true;
    }

    private boolean isBedrockHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks;
        for (BlockPos pos : touchingBlocks = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()}) {
            IBlockState touchingState = HoleTP.mc.world.getBlockState(pos);
            if (touchingState.getBlock() != Blocks.AIR && touchingState.getBlock() == Blocks.BEDROCK) continue;
            return false;
        }
        return true;
    }

    private boolean isBothHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks;
        for (BlockPos pos : touchingBlocks = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()}) {
            IBlockState touchingState = HoleTP.mc.world.getBlockState(pos);
            if (touchingState.getBlock() != Blocks.AIR && (touchingState.getBlock() == Blocks.BEDROCK || touchingState.getBlock() == Blocks.OBSIDIAN)) continue;
            return false;
        }
        return true;
    }

    private boolean isElseHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks;
        for (BlockPos pos : touchingBlocks = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()}) {
            IBlockState touchingState = HoleTP.mc.world.getBlockState(pos);
            if (touchingState.getBlock() != Blocks.AIR && touchingState.isFullBlock()) continue;
            return false;
        }
        return true;
    }
}

