package mokilop.paleolithic.block.custom;

import mokilop.paleolithic.block.entity.PrimitiveCraftingBenchBlockEntity;
import mokilop.paleolithic.block.enums.StoneSharpeningStationBlockMode;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class CraftingBenchBlock extends HorizontalFacingBlock {
    public CraftingBenchBlock(Settings settings) {
        super(settings);
    }

    private static VoxelShape SHAPE = Stream.of(
            Block.createCuboidShape(1, 0, 1, 15, 3, 15),
            Block.createCuboidShape(1, 15, 15, 15, 16, 16),
            Block.createCuboidShape(0, 13, 0, 16, 15, 16),
            Block.createCuboidShape(15, 15, 0, 16, 16, 16),
            Block.createCuboidShape(0, 15, 0, 1, 16, 16),
            Block.createCuboidShape(1, 15, 0, 15, 16, 1),
            Block.createCuboidShape(3, 3, 3, 13, 13, 13)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    // BLOCK ENTITY


    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof PrimitiveCraftingBenchBlockEntity) {
                ItemScatterer.spawn(world, pos, (PrimitiveCraftingBenchBlockEntity)blockEntity);
                world.updateComparators(pos,this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
}
