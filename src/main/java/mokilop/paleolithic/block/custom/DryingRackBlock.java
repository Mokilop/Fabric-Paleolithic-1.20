package mokilop.paleolithic.block.custom;

import mokilop.paleolithic.Paleolithic;
import mokilop.paleolithic.block.entity.DryingRackBlockEntity;
import mokilop.paleolithic.block.entity.ModBlockEntities;
import mokilop.paleolithic.block.enums.ComplexAttachment;
import mokilop.paleolithic.data.Constants;
import net.minecraft.block.*;
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
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.Stream;

public class DryingRackBlock extends BlockWithEntity {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final EnumProperty<ComplexAttachment> ATTACHMENT =
            EnumProperty.of("drying_rack_attachment", ComplexAttachment.class);

    //region Models
    public static final Model PARENT_MODEL = new Model(Optional.of(
            new Identifier(Paleolithic.MOD_ID, "block/drying_rack")),
            Optional.empty());
    public static final Model PARENT_MODEL_FAR = new Model(Optional.of(
            new Identifier(Paleolithic.MOD_ID, "block/drying_rack_far")),
            Optional.of("_far"));
    public static final Model PARENT_MODEL_CLOSE = new Model(Optional.of(
            new Identifier(Paleolithic.MOD_ID, "block/drying_rack_close")),
            Optional.of("_close"));

    public static final Model PARENT_MODEL_ON_WALL = new Model(Optional.of(
            new Identifier(Paleolithic.MOD_ID, "block/drying_rack_on_wall")),
            Optional.of("_on_wall"));
    public static final Model PARENT_MODEL_ON_WALL_LEFT = new Model(Optional.of(
            new Identifier(Paleolithic.MOD_ID, "block/drying_rack_on_wall_left")),
            Optional.of("_on_wall_left"));
    public static final Model PARENT_MODEL_ON_WALL_RIGHT = new Model(Optional.of(
            new Identifier(Paleolithic.MOD_ID, "block/drying_rack_on_wall_right")),
            Optional.of("_on_wall_right"));
    public static final Model PARENT_MODEL_ON_WALL_UP = new Model(Optional.of(
            new Identifier(Paleolithic.MOD_ID, "block/drying_rack_on_wall_up")),
            Optional.of("_on_wall_up"));
    ;

    public static final Model PARENT_MODEL_ON_CEILING = new Model(Optional.of(
            new Identifier(Paleolithic.MOD_ID, "block/drying_rack_on_ceiling")),
            Optional.of("_on_ceiling"));
    public static final Model PARENT_MODEL_ON_CEILING_CLOSE = new Model(Optional.of(
            new Identifier(Paleolithic.MOD_ID, "block/drying_rack_on_ceiling_close")),
            Optional.of("_on_ceiling_close"));
    public static final Model PARENT_MODEL_ON_CEILING_FAR = new Model(Optional.of(
            new Identifier(Paleolithic.MOD_ID, "block/drying_rack_on_ceiling_far")),
            Optional.of("_on_ceiling_far"));
    //endregion
    // region Shapes
    private static VoxelShape FLOOR_NS_SHAPE = Stream.of(
            Block.createCuboidShape(0, 0, 6, 3, 16, 10),
            Block.createCuboidShape(13, 0, 6, 16, 16, 10),
            Block.createCuboidShape(3, 13, 7, 13, 15, 9)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
    private static VoxelShape FLOOR_EW_SHAPE = Stream.of(
            Block.createCuboidShape(6, 0, 0, 10, 16, 3),
            Block.createCuboidShape(6, 0, 13, 10, 16, 16),
            Block.createCuboidShape(7, 13, 3, 9, 15, 13)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
    private static VoxelShape CEILING_NS_SHAPE = Stream.of(
            Block.createCuboidShape(13, 12, 6, 16, 16, 10),
            Block.createCuboidShape(0, 12, 6, 3, 16, 10),
            Block.createCuboidShape(3, 13, 7, 13, 15, 9)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
    private static VoxelShape CEILING_EW_SHAPE = Stream.of(
            Block.createCuboidShape(6, 12, 0, 10, 16, 3),
            Block.createCuboidShape(6, 12, 13, 10, 16, 16),
            Block.createCuboidShape(7, 13, 3, 9, 15, 13)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
    private static VoxelShape WALL_N_SHAPE = VoxelShapes.combineAndSimplify(Block.createCuboidShape(6, 4, 14, 10, 16, 16), Block.createCuboidShape(7, 13, 4, 9, 15, 14), BooleanBiFunction.OR);
    private static VoxelShape WALL_E_SHAPE = VoxelShapes.combineAndSimplify(Block.createCuboidShape(0, 4, 6, 2, 16, 10), Block.createCuboidShape(2, 13, 7, 12, 15, 9), BooleanBiFunction.OR);
    private static VoxelShape WALL_S_SHAPE = VoxelShapes.combineAndSimplify(Block.createCuboidShape(6, 4, 0, 10, 16, 2), Block.createCuboidShape(7, 13, 2, 9, 15, 12), BooleanBiFunction.OR);
    private static VoxelShape WALL_W_SHAPE = VoxelShapes.combineAndSimplify(Block.createCuboidShape(14, 4, 6, 16, 16, 10), Block.createCuboidShape(4, 13, 7, 14, 15, 9), BooleanBiFunction.OR);
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
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getSide();
        BlockPos blockPos = ctx.getBlockPos();
        BlockPos hitBlockPos = blockPos.offset(direction);
        Vec3d hitPos = ctx.getHitPos();
        Vec3d hitOffset = new Vec3d(hitPos.x - hitBlockPos.getX(),
                hitPos.y - hitBlockPos.getY(),
                hitPos.z - hitBlockPos.getZ());
        World world = ctx.getWorld();
        Direction.Axis axis = direction.getAxis();
        Direction playerFacing = ctx.getHorizontalPlayerFacing();

        if (axis == Direction.Axis.Y) {
            BlockState state = getDefaultState().with(FACING, playerFacing.getOpposite());
            double offset = isNS(playerFacing) ? hitOffset.z : hitOffset.x;
            offset = (!isSE(playerFacing)) ? (1 - offset) : offset;
            ComplexAttachment att = getAttForYAxis(direction, offset);
            state = state.with(ATTACHMENT, att);
            return state.canPlaceAt(world, blockPos) ? state : null;
        }

        BlockState state = getDefaultState().with(FACING, direction);
        if (hitOffset.y >= 0.85) {
            state = state.with(ATTACHMENT, ComplexAttachment.WALL_UP);
            return state.canPlaceAt(world, blockPos) ? state : null;
        }
        double offset = isNS(direction) ? hitOffset.x : hitOffset.z;
        offset = (isNE(direction)) ? (1 - offset) : offset;
        ComplexAttachment att = offset <= 0.25 ? ComplexAttachment.WALL_LEFT :
                offset >= 0.75 ? ComplexAttachment.WALL_RIGHT : ComplexAttachment.WALL;
        state = state.with(ATTACHMENT, att);
        return state.canPlaceAt(world, blockPos) ? state : null;
    }

    boolean isNS(Direction dir) {
        return dir.getHorizontal() % 2 == 0;
    }

    boolean isSE(Direction dir) {
        return dir.getId() % 2 == 1 && dir.getId() > 1;
    }

    boolean isNE(Direction dir) {
        return dir.getHorizontal() >= 2;
    }

    @NotNull
    private static ComplexAttachment getAttForYAxis(Direction direction, double offset) {
        return direction == Direction.UP ?
                (offset <= 0.25 ? ComplexAttachment.FLOOR_CLOSE : offset >= 0.75 ? ComplexAttachment.FLOOR_FAR : ComplexAttachment.FLOOR) :
                (offset <= 0.25 ? ComplexAttachment.CEILING_CLOSE : offset >= 0.75 ? ComplexAttachment.CEILING_FAR : ComplexAttachment.CEILING);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        Attachment attachment = state.get(ATTACHMENT).getSimpleAttachment();
        Direction direction2 = DryingRackBlock.getPlacementSide(state).getOpposite();
        if (direction2 == direction && !state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Direction direction = DryingRackBlock.getPlacementSide(state).getOpposite();
        if (direction == Direction.UP) {
            return Block.sideCoversSmallSquare(world, pos.up(), Direction.DOWN);
        }
        if (direction == Direction.DOWN) {
            BlockState stateBelow = world.getBlockState(pos.down());
            if (!stateBelow.isOf(this)) {
                return Block.sideCoversSmallSquare(world, pos.down(), Direction.UP);
            }
            ComplexAttachment attBelow = stateBelow.get(ATTACHMENT);
            if (isNS(stateBelow.get(FACING)) == isNS(state.get(FACING))
                    && (attBelow == ComplexAttachment.WALL_UP || attBelow.getSimpleAttachment() == Attachment.FLOOR)) {
                return true;
            }
        }
        return WallMountedBlock.canPlaceAt(world, pos, direction);
    }

    private static Direction getPlacementSide(BlockState state) {
        switch (state.get(ATTACHMENT).getSimpleAttachment()) {
            case CEILING -> {
                return Direction.DOWN;
            }
            case FLOOR -> {
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
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (getPlacementSide(state).getAxis() == Direction.Axis.Y
                || state.get(ATTACHMENT) == ComplexAttachment.WALL_UP) {
            return getShapeFloorAndCeil(state);
        }
        return getShapeWall(state);
    }

    private VoxelShape getShapeWall(BlockState state) {
        Direction facing = state.get(FACING);
        ComplexAttachment att = state.get(ATTACHMENT);
        double offset = 0.375 * att.getOffset() * (isNE(facing) ? -1 : 1);
        return switch (facing) {
            case EAST -> WALL_E_SHAPE.offset(0, 0, offset);
            case WEST -> WALL_W_SHAPE.offset(0, 0, offset);
            case NORTH -> WALL_N_SHAPE.offset(offset, 0, 0);
            case SOUTH -> WALL_S_SHAPE.offset(offset, 0, 0);
            default -> VoxelShapes.fullCube();
        };
    }

    private VoxelShape getShapeFloorAndCeil(BlockState state) {
        Direction facing = state.get(FACING);
        ComplexAttachment att = state.get(ATTACHMENT);
        double offset = 0.375 * att.getOffset() * (isSE(facing) ? -1 : 1);
        if (isNS(facing)) {
            VoxelShape base = att.getSimpleAttachment() == Attachment.FLOOR ? FLOOR_NS_SHAPE : CEILING_NS_SHAPE;
            return base.offset(0, 0, offset);
        }
        VoxelShape base = att.getSimpleAttachment() == Attachment.FLOOR ? FLOOR_EW_SHAPE : CEILING_EW_SHAPE;
        return base.offset(offset, 0, 0);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DryingRackBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.DRYING_RACK, DryingRackBlockEntity::tick);
    }

}
