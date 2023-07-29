package mokilop.paleolithic.block.custom;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class RockBlock extends HorizontalFacingBlock implements Waterloggable {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final IntProperty STONES = IntProperty.of("stones", 1, 3);
    private static VoxelShape SHAPE_NORTH_1 = Block.createCuboidShape(3, 0, 4, 7, 2, 9);
    private static VoxelShape SHAPE_WEST_1 = Block.createCuboidShape(4, 0, 9, 9, 2, 13);
    private static VoxelShape SHAPE_SOUTH_1 = Block.createCuboidShape(9, 0, 7, 13, 2, 12);
    private static VoxelShape SHAPE_EAST_1 = Block.createCuboidShape(7, 0, 3, 12, 2, 7);
    private static VoxelShape SHAPE_2 = Block.createCuboidShape(2, 0, 2, 13, 2, 13);
    private static VoxelShape SHAPE_3 = Block.createCuboidShape(1, 0, 1, 15, 2, 15);
    public RockBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
                .with(Properties.HORIZONTAL_FACING, Direction.NORTH)
                .with(STONES, 1)
                .with(WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING, WATERLOGGED, STONES);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockState blockBellow = world.getBlockState(pos.down());
        return blockBellow.isSideSolidFullSquare(world, pos.down(), Direction.UP);
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        if (!context.shouldCancelInteraction() && context.getStack().isOf(this.asItem()) && state.get(STONES) < 3) {
            return true;
        }
        return super.canReplace(state, context);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        int stones = state.get(STONES);
        if (stones == 2) return SHAPE_2;
        if (stones == 3) return SHAPE_3;
        switch (dir) {
            case SOUTH:
                return SHAPE_SOUTH_1;
            case EAST:
                return SHAPE_EAST_1;
            case WEST:
                return SHAPE_WEST_1;
            default:
                return SHAPE_NORTH_1;
        }
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos());
        if (blockState.isOf(this)) {
            return (BlockState) blockState.with(STONES, Math.min(3, blockState.get(STONES) + 1));
        }
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                .with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            // This is for 1.17 and below: world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        if (direction == Direction.DOWN && !this.canPlaceAt(state, world, pos)) {
            return Blocks.AIR.getDefaultState();
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
}


