package mokilop.paleolithic.block.custom;

import mokilop.paleolithic.Paleolithic;
import mokilop.paleolithic.block.entity.DryingRackBlockEntity;
import mokilop.paleolithic.block.entity.GrindstoneBlockEntity;
import mokilop.paleolithic.block.entity.ModBlockEntities;
import mokilop.paleolithic.util.ProgressActionResult;
import mokilop.paleolithic.util.SoundEffect;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.data.client.Model;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
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

import java.util.Arrays;
import java.util.Optional;

public class GrindstoneBlock extends BlockWithEntity {
    public static final Model MODEL = new Model(Optional.of(new Identifier(Paleolithic.MOD_ID, "block/grindstone")),
            Optional.empty());
    private static final VoxelShape SHAPE = VoxelShapes.combineAndSimplify(Block.createCuboidShape(1, 4, 1, 15, 7, 15), Block.createCuboidShape(0, 0, 0, 16, 3, 16), BooleanBiFunction.OR);
    private static final SoundEffect ITEM_PULLOUT_SOUND = new SoundEffect(SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, .5f, 1.5f);
    private static final SoundEffect GRINDING_SOUND = new SoundEffect(SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS, 1, .3f);
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;


    public GrindstoneBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GrindstoneBlockEntity(pos, state);
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, ModBlockEntities.DRYING_RACK, DryingRackBlockEntity::tick);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
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
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof GrindstoneBlockEntity entity) {
                ItemScatterer.spawn(world, pos, entity);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (hit.getSide() == Direction.DOWN ||
                !(world.getBlockEntity(pos) instanceof GrindstoneBlockEntity entity)) {
            return ActionResult.PASS;
        }
        if (hit.getSide().getHorizontal() >= 0) {
            return tryGrinding(world, entity);
        }
        int slot = getSlot(hit, pos, state.get(FACING));
        if (player.getStackInHand(hand).isEmpty() && hand == Hand.MAIN_HAND) {
            return pullOutStack(slot, world, entity, player);
        }
        return tryAddItem(slot, world, entity, player, hand);
    }

    private int getSlot(BlockHitResult hit, BlockPos pos, Direction facing) {
        int horSlots = 2;
        int verticalSlots = 2;
        int totalSlots = horSlots + verticalSlots;
        final int xSlot = (int) ((hit.getPos().x - pos.getX()) * horSlots);
        final int zSlot = (int) ((hit.getPos().z - pos.getZ()) * verticalSlots);
        return switch (facing.rotateCounterclockwise(Direction.Axis.Y)) {
            case NORTH -> totalSlots - (xSlot + horSlots * zSlot);
            case EAST -> 1 - zSlot + horSlots * xSlot;
            case WEST -> zSlot + horSlots * (1 - xSlot);
            default -> xSlot + horSlots * zSlot;
        };
    }

    private ActionResult tryAddItem(int slot, World world, GrindstoneBlockEntity entity, PlayerEntity player, Hand hand) {
        ItemStack itemHeld = player.getStackInHand(hand);
        if (entity.addItem(player.isCreative() ? itemHeld.copy() : itemHeld, slot)) {
            return ActionResult.SUCCESS;
        }
        return ActionResult.CONSUME;
    }

    private ActionResult pullOutStack(int slot, World world, GrindstoneBlockEntity entity, PlayerEntity player) {
        ItemStack stack = entity.removeStack(slot);
        BlockPos pos = entity.getPos();
        if (!player.isCreative() && !player.getInventory().insertStack(stack)) {
            ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        }
        ITEM_PULLOUT_SOUND.play(world, entity.getPos());
        return ActionResult.SUCCESS;
    }

    private ActionResult tryGrinding(World world, GrindstoneBlockEntity entity) {
        var results = entity.grind();
        ProgressActionResult result = Arrays.stream(results).anyMatch(r -> r == ProgressActionResult.COMPLETE) ? ProgressActionResult.COMPLETE :
                Arrays.stream(results).anyMatch(r -> r == ProgressActionResult.PROGRESS) ? ProgressActionResult.PROGRESS : ProgressActionResult.FAIL;
        if (result == ProgressActionResult.COMPLETE) {
            GRINDING_SOUND.play(world, entity.getPos());
            return ActionResult.SUCCESS;
        }
        if (result == ProgressActionResult.PROGRESS) {
            GRINDING_SOUND.play(world, entity.getPos());
            return ActionResult.SUCCESS;
        }
        return ActionResult.CONSUME;
    }
}
