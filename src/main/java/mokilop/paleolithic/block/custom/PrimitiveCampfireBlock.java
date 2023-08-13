package mokilop.paleolithic.block.custom;

import mokilop.paleolithic.block.entity.ModBlockEntities;
import mokilop.paleolithic.block.entity.PrimitiveCampfireBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PrimitiveCampfireBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BooleanProperty.of("lit");
    public static final BooleanProperty USED = BooleanProperty.of("used");
    public static final IntProperty FIRE_STRENGTH = IntProperty.of("fire_strength", 0, 4);
    private static VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 2, 16);

    public PrimitiveCampfireBlock(Settings settings) {
        super(settings);
    }

    @NotNull
    private static ActionResult handleAddingItem(PlayerEntity player, PrimitiveCampfireBlockEntity entity, ItemStack itemToAdd) {
        boolean success = entity.addItem(itemToAdd);
        if (!success) return ActionResult.SUCCESS;
        itemToAdd.decrement(player.isCreative() ? 0 : 1);
        return ActionResult.SUCCESS;
    }

    @NotNull
    private static ActionResult handleFuelAdding(BlockState state, World world, BlockPos pos, PlayerEntity player, PrimitiveCampfireBlockEntity entity, ItemStack mhs) {
        boolean success = entity.addFuel(mhs);
        if (!success) return ActionResult.SUCCESS;
        world.setBlockState(pos, state.with(LIT, true).with(USED, true));
        if (!player.isCreative()) mhs.decrement(1);
        world.playSound(null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 0.2f, 1f);
        return ActionResult.SUCCESS;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockState blockBellow = world.getBlockState(pos.down());
        return blockBellow.isSideSolidFullSquare(world, pos.down(), Direction.UP);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.DOWN && !this.canPlaceAt(state, world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx)
                .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                .with(LIT, false)
                .with(USED, false)
                .with(FIRE_STRENGTH, 0);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    // Block Entity Below

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!state.get(LIT)) {
            return;
        }
        if (random.nextInt(10) == 0) {
            world.playSound((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, SoundEvents.BLOCK_CAMPFIRE_CRACKLE, SoundCategory.BLOCKS, 0.5f + random.nextFloat(), random.nextFloat() * 0.7f + 0.6f, false);
        }
        if (random.nextInt(5) == 0) {
            for (int i = 0; i < random.nextInt(1) + 1; ++i) {
                world.addParticle(ParticleTypes.LAVA, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, random.nextFloat() / 2.0f, 5.0E-5, random.nextFloat() / 2.0f);
            }
        }
        if (random.nextBoolean()) {
            DefaultParticleType defaultParticleType = ParticleTypes.CAMPFIRE_COSY_SMOKE;
            world.addImportantParticle(defaultParticleType, true, (double) pos.getX() + 0.5 + random.nextDouble() / 3.0 * (double) (random.nextBoolean() ? 1 : -1), (double) pos.getY() + random.nextDouble() + random.nextDouble(), (double) pos.getZ() + 0.5 + random.nextDouble() / 3.0 * (double) (random.nextBoolean() ? 1 : -1), 0.0, 0.07, 0.0);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(LIT).add(USED).add(FIRE_STRENGTH);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof PrimitiveCampfireBlockEntity) {
                ItemScatterer.spawn(world, pos, (PrimitiveCampfireBlockEntity) blockEntity);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient || hand != Hand.MAIN_HAND) return ActionResult.SUCCESS;
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof PrimitiveCampfireBlockEntity entity)) {
            return ActionResult.SUCCESS;
        }
        ItemStack mhs = player.getMainHandStack();
        if(mhs.isEmpty()){
            return handleItemRemove(world, pos, player, entity);
        }
        if (AbstractFurnaceBlockEntity.canUseAsFuel(mhs)) {
            return handleFuelAdding(state, world, pos, player, entity, mhs);
        }
        return handleAddingItem(player, entity, mhs);
    }

    @NotNull
    private static ActionResult handleItemRemove(World world, BlockPos pos, PlayerEntity player, PrimitiveCampfireBlockEntity entity) {
        ItemStack removed = entity.removeStack(0);
        if(!player.isCreative()) player.giveItemStack(removed);
        if(!removed.isEmpty()) world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.5f, 2);
        entity.markDirty();
        return ActionResult.SUCCESS;
    }

    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        super.onBlockBreakStart(state, world, pos, player);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PrimitiveCampfireBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.PRIMITIVE_CAMPFIRE, PrimitiveCampfireBlockEntity::tick);
    }
}