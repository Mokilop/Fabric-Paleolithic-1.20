package mokilop.paleolithic.block.custom;

import mokilop.paleolithic.Paleolithic;
import mokilop.paleolithic.block.entity.CraftingStumpBlockEntity;
import mokilop.paleolithic.data.Constants;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.Stream;

public class CraftingStumpBlock extends BlockWithEntity {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final Model PARENT_MODEL = new Model(Optional.of(new Identifier(Paleolithic.MOD_ID, "block/crafting_stump")),
            Optional.empty());
    private static final VoxelShape SHAPE = Stream.of(
            Block.createCuboidShape(1, 0, 1, 15, 3, 15),
            Block.createCuboidShape(1, 15, 15, 15, 16, 16),
            Block.createCuboidShape(0, 13, 0, 16, 15, 16),
            Block.createCuboidShape(15, 15, 0, 16, 16, 16),
            Block.createCuboidShape(0, 15, 0, 1, 16, 16),
            Block.createCuboidShape(1, 15, 0, 15, 16, 1),
            Block.createCuboidShape(3, 3, 3, 13, 13, 13)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
    private final WoodType woodType;
    private final boolean isStripped;
    private final TextureMap textureMap;

    public CraftingStumpBlock(Settings settings, WoodType woodType, boolean isStripped) {
        super(settings.sounds(woodType.soundType()));
        this.woodType = woodType;
        this.isStripped = isStripped;
        textureMap = new TextureMap().register(TextureKey.of("log"), TextureMap.getId(getLogBlock()))
                .register(TextureKey.of("log_top"), TextureMap.getSubId(getLogBlock(), "_top"))
                .register(TextureKey.of("stripped_log"), TextureMap.getId(Constants.STRIPPED_LOGS_MAP.get(woodType)));
    }

    public WoodType getWoodType() {
        return woodType;
    }

    public boolean getIsStripped() {
        return isStripped;
    }

    public TextureMap getTextureMap() {
        return textureMap;
    }

    public Model getParentModel() {
        return PARENT_MODEL;
    }

    public Block getLogBlock() {
        return (isStripped ? Constants.STRIPPED_LOGS_MAP : Constants.LOGS_MAP).get(woodType);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
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

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CraftingStumpBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CraftingStumpBlockEntity) {
                ItemScatterer.spawn(world, pos, (CraftingStumpBlockEntity) blockEntity);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

}
