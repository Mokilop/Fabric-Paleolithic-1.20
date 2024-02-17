package mokilop.paleolithic.block.custom;

import mokilop.paleolithic.item.ModItems;
import mokilop.paleolithic.util.ModTags;
import mokilop.paleolithic.util.SoundEffect;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class RockBlock extends HorizontalFacingBlock implements Waterloggable {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final IntProperty STONES = IntProperty.of("stones", 1, 3);
    public static final BooleanProperty IS_ON_HARD_SURFACE = BooleanProperty.of("is_on_hard_surface");
    private static final VoxelShape[][] SHAPES = new VoxelShape[][]{
            {
                    Block.createCuboidShape(9, 0, 7, 13, 2, 12),
                    Block.createCuboidShape(4, 0, 9, 9, 2, 13),
                    Block.createCuboidShape(3, 0, 4, 7, 2, 9),
                    Block.createCuboidShape(7, 0, 3, 12, 2, 7)
            },
            {
                    Block.createCuboidShape(2, 0, 2, 13, 2, 13)
            },
            {
                    Block.createCuboidShape(1, 0, 1, 15, 2, 15)
            }
    };

    public static final ItemStack SHARPENING_REWARD = ModItems.FLAKED_ROCK.getDefaultStack();
    public static final SoundEffect TRY_SHARPENING_SOUND = new SoundEffect(SoundEvents.BLOCK_STONE_HIT, SoundCategory.BLOCKS, .5f, 2f);
    public static final SoundEffect SHARPENING_SUCCESS_SOUND = new SoundEffect(SoundEvents.BLOCK_STONE_HIT, SoundCategory.BLOCKS, 1F, 1f);

    public RockBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
                .with(Properties.HORIZONTAL_FACING, Direction.NORTH)
                .with(STONES, 1)
                .with(WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING, WATERLOGGED, STONES, IS_ON_HARD_SURFACE);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockState blockBellow = world.getBlockState(pos.down());
        return blockBellow.isSideSolidFullSquare(world, pos.down(), Direction.UP);
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        return (!context.shouldCancelInteraction() && context.getStack().isOf(this.asItem()) && state.get(STONES) < 3) || super.canReplace(state, context);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        int dirId = state.get(FACING).getHorizontal();
        int stonesId = state.get(STONES) - 1;
        return SHAPES[stonesId][stonesId == 0 ? dirId : 0];
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos());
        if (blockState.isOf(this)) {
            return blockState.with(STONES, Math.min(3, blockState.get(STONES) + 1));
        }
        return getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                .with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER)
                .with(IS_ON_HARD_SURFACE, ctx.getWorld().getBlockState(ctx.getBlockPos().down()).getHardness(ctx.getWorld(), ctx.getBlockPos().down()) >= Blocks.STONE.getHardness());
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        if (direction == Direction.DOWN && !this.canPlaceAt(state, world, pos)) {
            return Blocks.AIR.getDefaultState();
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemHeld = player.getStackInHand(hand);
        if (!itemHeld.isIn(ModTags.Items.HAMMERS) || !state.get(IS_ON_HARD_SURFACE)) {
            return ActionResult.PASS;
        }
        if (world.isClient) return ActionResult.SUCCESS;
        if (!(world.getRandom().nextBetween(0, 7) == 3)) {
            TRY_SHARPENING_SOUND.play(world, pos);
            return ActionResult.SUCCESS;
        }
        if (state.get(STONES).equals(1)) {
            world.removeBlock(pos, false);
        } else {
            state = state.with(STONES, state.get(STONES) - 1);
            world.setBlockState(pos, state);
        }
        itemHeld.damage(1, player, user -> user.sendToolBreakStatus(hand));
        world.spawnEntity(new ItemEntity(world, pos.getX() + .5f, pos.getY(), pos.getZ() + .5f, SHARPENING_REWARD));
        SHARPENING_SUCCESS_SOUND.play(world, pos);
        return ActionResult.SUCCESS;
    }
}


