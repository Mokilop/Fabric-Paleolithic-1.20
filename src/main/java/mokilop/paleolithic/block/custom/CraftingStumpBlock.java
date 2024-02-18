package mokilop.paleolithic.block.custom;

import mokilop.paleolithic.Paleolithic;
import mokilop.paleolithic.block.entity.CraftingStumpBlockEntity;
import mokilop.paleolithic.block.entity.ModBlockEntities;
import mokilop.paleolithic.block.entity.StumpBlockEntity;
import mokilop.paleolithic.data.Constants;
import mokilop.paleolithic.item.custom.HammerItem;
import mokilop.paleolithic.util.ModTags;
import mokilop.paleolithic.util.ProgressActionResult;
import mokilop.paleolithic.util.SoundEffect;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.Stream;

public class CraftingStumpBlock extends BlockWithEntity {
    private static final SoundEffect ITEM_PULLOUT = new SoundEffect(SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, .5f, 1.5f);
    private static final SoundEffect ITEM_HANG = new SoundEffect(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.BLOCKS, .5f, .8f);
    private static final SoundEffect ITEM_PLACE = new SoundEffect(SoundEvents.ENTITY_ITEM_FRAME_ADD_ITEM, SoundCategory.BLOCKS, .5f, 1f);
    private static final SoundEffect PROGRESS_UP = new SoundEffect(SoundEvents.BLOCK_STONE_HIT, SoundCategory.BLOCKS, .5f, 1f);
    private static final SoundEffect CRAFTING_COMPLETE = new SoundEffect(SoundEvents.ENTITY_GLOW_ITEM_FRAME_REMOVE_ITEM, SoundCategory.BLOCKS, .5f, 1f);

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

    public TextureMap getTextureMap() {
        return textureMap;
    }

    public Block getLogBlock() {
        return (isStripped ? Constants.STRIPPED_LOGS_MAP : Constants.LOGS_MAP).get(woodType);
    }

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

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!(world.getBlockEntity(pos) instanceof CraftingStumpBlockEntity entity)) {
            return ActionResult.PASS;
        }
        final float relativeY = (float) (hit.getPos().getY() - pos.getY());
        if (hit.getSide() == state.get(FACING) && relativeY < .75f) {
            return useOnFacingSide(world, entity, player, hand);
        }
        if (hit.getSide() != Direction.UP && (relativeY < .9375f || !hit.isInsideBlock()))
            return ActionResult.PASS;

        int slot = getSlot(hit, pos, state.get(FACING));
        ItemStack itemHeld = player.getStackInHand(hand);
        boolean shouldTryAdd = !itemHeld.isIn(ModTags.Items.HAMMERS);
        if (entity.getStack(slot).isEmpty() && shouldTryAdd) {
            return tryAddItemInHand(world, entity, player, hand, slot);
        }
        if (!itemHeld.isEmpty()) {
            return tryCrafingWithItemHeld(world, entity, player, hand, slot);
        }
        if (hand.equals(Hand.MAIN_HAND)) {
            return pullOutItem(world, entity, player, slot);
        }
        return ActionResult.CONSUME_PARTIAL;
    }

    private ActionResult useOnFacingSide(World world, CraftingStumpBlockEntity entity, PlayerEntity player, Hand hand) {
        if (entity.isEmpty()) {
            return tryHangItem(world, entity, player, hand);
        }
        if (player.getMainHandStack().isEmpty()) {
            return pullOutItem(world, entity, player, 9);
        }
        return ActionResult.PASS;
    }

    private ActionResult tryHangItem(World world, CraftingStumpBlockEntity entity, PlayerEntity player, Hand hand) {
        ItemStack itemHeld = player.getStackInHand(hand);
        if (!entity.getStack(9).isEmpty() || !itemHeld.isIn(ModTags.Items.HAMMERS)) {
            return ActionResult.PASS;
        }
        entity.setStack(9, player.isCreative() ? itemHeld.copy() : itemHeld);
        ITEM_HANG.play(world, entity.getPos());
        return ActionResult.SUCCESS;
    }

    private ActionResult pullOutItem(World world, CraftingStumpBlockEntity entity, PlayerEntity player, int slot) {
        BlockPos pos = entity.getPos();

        if (player.isCreative()) {
            entity.removeStack(slot);
        } else if (!player.getInventory().insertStack(entity.removeStack(slot))) {
            ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), entity.removeStack(slot));
        }
        ITEM_PULLOUT.play(world, pos);
        return ActionResult.SUCCESS;
    }

    private ActionResult tryAddItemInHand(World world, CraftingStumpBlockEntity entity, PlayerEntity player, Hand hand, int slot) {
        assert (slot < 9);
        ItemStack itemHeld = player.getStackInHand(hand);
        ItemStack offhandItem = player.getOffHandStack();
        if (!entity.getStack(slot).isEmpty() || itemHeld.isEmpty()) {
            return ActionResult.PASS;
        }
        entity.addStack(slot, player.isCreative() ? itemHeld.copy() : itemHeld);
        ITEM_PLACE.play(world, entity.getPos());
        return ActionResult.SUCCESS;
    }

    private ActionResult tryCrafingWithItemHeld(World world, CraftingStumpBlockEntity entity, PlayerEntity player, Hand hand, int targetedSlot) {
        ItemStack itemHeld = player.getStackInHand(hand);
        ItemStack offhandItem = player.getOffHandStack();
        if (entity.getStack(targetedSlot).isEmpty() && !offhandItem.isEmpty() && !offhandItem.isIn(ModTags.Items.OFFHAND_EQUIPMENT))
            return ActionResult.PASS;
        ProgressActionResult result = entity.attemptCraft(itemHeld, player);
        if (result == ProgressActionResult.FAIL) return ActionResult.CONSUME_PARTIAL;
        if (result == ProgressActionResult.PROGRESS || result == ProgressActionResult.PROGRESS_PARTIAL) {
            PROGRESS_UP.play(world, entity.getPos());
            return ActionResult.SUCCESS;
        }
        CRAFTING_COMPLETE.play(world, entity.getPos());
        itemHeld.damage(1, player, user -> user.sendToolBreakStatus(hand));
        return ActionResult.SUCCESS;
    }

    private int getSlot(BlockHitResult hit, BlockPos pos, Direction facing) {
        final int xSlot = (int) ((hit.getPos().x - pos.getX()) * 3);
        final int zSlot = (int) ((hit.getPos().z - pos.getZ()) * 3);
        return switch (facing) {
            case NORTH -> 8 - (xSlot + 3 * zSlot);
            case EAST -> 2 - zSlot + 3 * xSlot;
            case WEST -> zSlot + 3 * (2 - xSlot);
            default -> xSlot + 3 * zSlot;
        };
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

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.CRAFTING_STUMP, CraftingStumpBlockEntity::tick);
    }
}
