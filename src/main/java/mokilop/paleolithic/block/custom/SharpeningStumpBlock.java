package mokilop.paleolithic.block.custom;

import mokilop.paleolithic.Paleolithic;
import mokilop.paleolithic.block.ModBlocks;
import mokilop.paleolithic.block.entity.SharpeningStumpBlockEntity;
import mokilop.paleolithic.data.Constants;
import mokilop.paleolithic.item.custom.HammerItem;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.RecipeType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.*;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.Stream;

public class SharpeningStumpBlock extends BlockWithEntity {
    public static final Model PARENT_MODEL = new Model(Optional.of(new Identifier(Paleolithic.MOD_ID, "block/sharpening_stump")),
            Optional.empty());
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    private static final VoxelShape SHAPE = Stream.of(
            Block.createCuboidShape(1, 10, 1, 15, 11, 15),
            Block.createCuboidShape(0, 2, 0, 16, 10, 16),
            Block.createCuboidShape(12, 0, 0, 16, 2, 4),
            Block.createCuboidShape(12, 0, 12, 16, 2, 16),
            Block.createCuboidShape(0, 0, 12, 4, 2, 16),
            Block.createCuboidShape(0, 0, 0, 4, 2, 4)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
    private final WoodType woodType;
    private final boolean isStripped;
    private final TextureMap textureMap;

    public SharpeningStumpBlock(Settings settings, WoodType woodType, boolean isStripped) {
        super(settings.sounds(woodType.soundType()));
        this.woodType = woodType;
        this.isStripped = isStripped;
        textureMap = new TextureMap().register(TextureKey.of("log"), TextureMap.getId(getLogBlock()))
                .register(TextureKey.of("log_top"), TextureMap.getSubId(getLogBlock(), "_top"));
    }

    public Block getLogBlock() {
        return (isStripped ? Constants.STRIPPED_LOGS_MAP : Constants.LOGS_MAP).get(woodType);
    }

    public TextureMap getTextureMap() {
        return textureMap;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
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

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SharpeningStumpBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(world.isClient || !(world.getBlockEntity(pos) instanceof SharpeningStumpBlockEntity entity)){
            return hit.getSide() == Direction.UP ? ActionResult.SUCCESS : ActionResult.PASS;
        }
        if(hit.getSide() != Direction.UP) return ActionResult.PASS;
        ItemStack mhs = player.getMainHandStack();
        return mhs.isEmpty() ? takeStack(entity, world, pos, player) : addStack(entity, world, pos, player, mhs);
    }

    private ActionResult takeStack(SharpeningStumpBlockEntity entity, World world, BlockPos pos, PlayerEntity player) {
        if(entity.isEmpty())return ActionResult.FAIL;
        world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, .5f, 2);
        ItemStack removed = entity.removeStack(0);
        entity.markDirty();
        player.giveItemStack(player.isCreative() && player.getInventory().contains(removed) ?
                ItemStack.EMPTY : removed);
        return ActionResult.SUCCESS;
    }

    private ActionResult addStack(SharpeningStumpBlockEntity entity, World world, BlockPos pos, PlayerEntity player, ItemStack stack) {
        if(!entity.isEmpty())return ActionResult.FAIL;
        world.playSound(null, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, .5f, .5f);
        entity.setStack(0, stack.copyWithCount(1));
        stack.decrement(player.isCreative() ? 0 : 1);
        entity.markDirty();
        return ActionResult.SUCCESS;
    }

    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        if(world.isClient)return;
        ItemStack mhs = player.getMainHandStack();
        if(!(mhs.getItem() instanceof HammerItem)
            || !(world.getBlockEntity(pos) instanceof SharpeningStumpBlockEntity entity)) return;
        entity.sharpen(mhs, world, (ServerPlayerEntity) player);
        world.playSound(null, pos, SoundEvents.BLOCK_STONE_HIT, SoundCategory.BLOCKS, 1, 3);
        ServerWorld serverWorld = (ServerWorld) world;
        serverWorld.spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.STONE.getDefaultState()),
                pos.getX() + .5f, pos.getY() + .6f, pos.getZ() + .5f, 4,
                .1f, 0, .1f, 5);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
