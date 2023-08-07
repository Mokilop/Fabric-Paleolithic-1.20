package mokilop.paleolithic.block.custom;

import mokilop.paleolithic.Paleolithic;
import mokilop.paleolithic.block.entity.DryingRackBlockEntity;
import mokilop.paleolithic.block.entity.ModBlockEntities;
import mokilop.paleolithic.block.entity.PrimitiveCampfireBlockEntity;
import mokilop.paleolithic.data.Constants;
import mokilop.paleolithic.util.ModTags;
import net.minecraft.block.*;
import net.minecraft.block.entity.BellBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.Attachment;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.awt.event.InvocationEvent;
import java.util.Optional;
import java.util.stream.Stream;

public class DryingRackBlock extends BlockWithEntity {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final EnumProperty<Attachment> ATTACHMENT = Properties.ATTACHMENT;

    //region Models and Shapes
    public static final Model PARENT_MODEL = new Model(Optional.of(
            new Identifier(Paleolithic.MOD_ID, "block/drying_rack")),
            Optional.empty());
    public static final Model PARENT_MODEL_ON_WALL = new Model(Optional.of(
            new Identifier(Paleolithic.MOD_ID, "block/drying_rack_on_wall")),
            Optional.of("_on_wall"));
    public static final Model PARENT_MODEL_ON_CEILING = new Model(Optional.of(
            new Identifier(Paleolithic.MOD_ID, "block/drying_rack_on_ceiling")),
            Optional.of("_on_ceiling"));
    private static final VoxelShape NS_SHAPE = Stream.of(
            Block.createCuboidShape(13, 0, 6, 16, 16, 10),
            Block.createCuboidShape(0, 0, 6, 3, 16, 10),
            Block.createCuboidShape(3, 13, 7, 13, 15, 9)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
    private static final VoxelShape EW_SHAPE = Stream.of(
            Block.createCuboidShape(6, 0, 0, 10, 16, 3),
            Block.createCuboidShape(6, 0, 13, 10, 16, 16),
            Block.createCuboidShape(7, 13, 3, 9, 15, 13)
            ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
    private static final VoxelShape N_SHAPE_ON_WALL = VoxelShapes.combineAndSimplify(
            Block.createCuboidShape(6, 4, 14, 10, 16, 16),
            Block.createCuboidShape(7, 13, 4, 9, 15, 14), BooleanBiFunction.OR);
    private static final VoxelShape W_SHAPE_ON_WALL = VoxelShapes.combineAndSimplify(
            Block.createCuboidShape(14, 4, 6, 16, 16, 10),
            Block.createCuboidShape(4, 13, 7, 14, 15, 9), BooleanBiFunction.OR);
    private static final VoxelShape S_SHAPE_ON_WALL = VoxelShapes.combineAndSimplify(
            Block.createCuboidShape(6, 4, 0, 10, 16, 2),
            Block.createCuboidShape(7, 13, 2, 9, 15, 12), BooleanBiFunction.OR);
    private static final VoxelShape E_SHAPE_ON_WALL = VoxelShapes.combineAndSimplify(
            Block.createCuboidShape(0, 4, 6, 2, 16, 10),
            Block.createCuboidShape(2, 13, 7, 12, 15, 9), BooleanBiFunction.OR);
    private static final VoxelShape NS_SHAPE_ON_CEILING = Stream.of(
            Block.createCuboidShape(13, 12, 6, 16, 16, 10),
            Block.createCuboidShape(0, 12, 6, 3, 16, 10),
            Block.createCuboidShape(3, 13, 7, 13, 15, 9)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
    private static final VoxelShape EW_SHAPE_ON_CEILING = Stream.of(
            Block.createCuboidShape(6, 12, 0, 10, 16, 3),
            Block.createCuboidShape(6, 12, 13, 10, 16, 16),
            Block.createCuboidShape(7, 13, 3, 9, 15, 13)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
    //endregion

    private final WoodType woodType;
    private final TextureMap textureMap;
    public DryingRackBlock(Settings settings, WoodType woodType) {
        super(settings);
        this.woodType = woodType;
        textureMap = new TextureMap().register(TextureKey.of("plank"), TextureMap.getId(Constants.PLANKS_MAP.get(woodType)));
    }

    public TextureMap getTextureMap() {
        return textureMap;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getSide();
        BlockPos blockPos = ctx.getBlockPos();
        World world = ctx.getWorld();
        Direction.Axis axis = direction.getAxis();
        if (axis == Direction.Axis.Y) {
            Attachment att = direction == Direction.DOWN ? Attachment.CEILING : Attachment.FLOOR;
            BlockState blockState = this.getDefaultState().with(ATTACHMENT, att).with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
            return blockState.canPlaceAt(ctx.getWorld(), blockPos) ? blockState : null;
        }
        BlockState state = getDefaultState().with(FACING, direction).with(ATTACHMENT, Attachment.SINGLE_WALL);
        return state.canPlaceAt(world, blockPos) ? state : null;
    }
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        Attachment attachment = state.get(ATTACHMENT);
        Direction direction2 = DryingRackBlock.getPlacementSide(state).getOpposite();
        if (direction2 == direction && !state.canPlaceAt(world, pos) && attachment != Attachment.DOUBLE_WALL) {
            return Blocks.AIR.getDefaultState();
        }
        if (direction.getAxis() == state.get(FACING).getAxis()) {
            if (attachment == Attachment.DOUBLE_WALL && !neighborState.isSideSolidFullSquare(world, neighborPos, direction)) {
                return state.with(ATTACHMENT, Attachment.SINGLE_WALL).with(FACING, direction.getOpposite());
            }
            if (attachment == Attachment.SINGLE_WALL && direction2.getOpposite() == direction && neighborState.isSideSolidFullSquare(world, neighborPos, state.get(FACING))) {
                return state.with(ATTACHMENT, Attachment.DOUBLE_WALL);
            }
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Direction direction = DryingRackBlock.getPlacementSide(state).getOpposite();
        if (direction == Direction.UP) {
            return Block.sideCoversSmallSquare(world, pos.up(), Direction.DOWN);
        }
        if(direction == Direction.DOWN){
            return Block.sideCoversSmallSquare(world, pos.down(), Direction.UP);
        }
        return WallMountedBlock.canPlaceAt(world, pos, direction);
    }

    private static Direction getPlacementSide(BlockState state) {
        switch (state.get(ATTACHMENT)) {
            case CEILING: {
                return Direction.DOWN;
            }
            case FLOOR: {
                return Direction.UP;
            }
        }
        return state.get(FACING);
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, ATTACHMENT);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.DRYING_RACK, DryingRackBlockEntity::tick);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    private VoxelShape getShape(BlockState state) {
        Direction direction = state.get(FACING);
        Attachment attachment = state.get(ATTACHMENT);
        switch (attachment) {
            case FLOOR -> {
                return isNS(direction) ? NS_SHAPE : EW_SHAPE;
            }
            case CEILING -> {
                return isNS(direction) ? NS_SHAPE_ON_CEILING : EW_SHAPE_ON_CEILING;
            }
            case DOUBLE_WALL -> {
                return isNS(direction) ? EW_SHAPE_ON_CEILING : NS_SHAPE_ON_CEILING;
            }
        }
        if (direction == Direction.NORTH) {
            return N_SHAPE_ON_WALL;
        }
        if (direction == Direction.SOUTH) {
            return S_SHAPE_ON_WALL;
        }
        if (direction == Direction.EAST) {
            return E_SHAPE_ON_WALL;
        }
        return W_SHAPE_ON_WALL;
    }

    boolean isNS(Direction dir){
        return dir == Direction.NORTH || dir == Direction.SOUTH;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.getShape(state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.getShape(state);
    }
}
