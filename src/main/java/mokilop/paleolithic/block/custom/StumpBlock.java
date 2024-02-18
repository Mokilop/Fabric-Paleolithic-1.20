package mokilop.paleolithic.block.custom;

import mokilop.paleolithic.Paleolithic;
import mokilop.paleolithic.block.entity.StumpBlockEntity;
import mokilop.paleolithic.data.Constants;
import mokilop.paleolithic.sound.ModSounds;
import mokilop.paleolithic.util.ModTags;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class StumpBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final Model PARENT_MODEL = new Model(Optional.of(new Identifier(Paleolithic.MOD_ID, "block/stump")),
            Optional.empty());
    public static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 6, 16);
    private final WoodType woodType;
    private final boolean isStripped;
    private final TextureMap textureMap;

    public StumpBlock(Settings settings, WoodType woodType, boolean stripped) {
        super(settings.sounds(woodType.soundType()));
        this.woodType = woodType;
        isStripped = stripped;
        textureMap = new TextureMap().register(TextureKey.of("log"), TextureMap.getId(getLogBlock()))
                .register(TextureKey.of("log_top"), TextureMap.getSubId(getLogBlock(), "_top"));
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

    @SuppressWarnings("SameReturnValue")
    public Model getParentModel() {
        return PARENT_MODEL;
    }

    public Block getLogBlock() {
        return (isStripped ? Constants.STRIPPED_LOGS_MAP : Constants.LOGS_MAP).get(woodType);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new StumpBlockEntity(pos, state);
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
            if (blockEntity instanceof StumpBlockEntity) {
                ItemScatterer.spawn(world, pos, (StumpBlockEntity) blockEntity);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (hit.getSide() != Direction.UP) return ActionResult.PASS;
        if (!(world.getBlockEntity(pos) instanceof StumpBlockEntity entity)) {
            return ActionResult.PASS;
        }
        if (entity.isEmpty()) {
            return tryAddItemInHand(world, entity, player, hand);
        }
        ItemStack itemHeld = player.getStackInHand(hand);
        if (!itemHeld.isEmpty()) {
            return tryChoppingWithItemHeld(world, entity, player, hand);
        }
        if (hand.equals(Hand.MAIN_HAND)) {
            return pullOutItem(world, entity, player);
        }
        return ActionResult.CONSUME_PARTIAL;
    }

    private ActionResult tryChoppingWithItemHeld(World world, StumpBlockEntity entity, PlayerEntity player, Hand hand) {
        ItemStack itemHeld = player.getStackInHand(hand);
        int result = entity.chop(itemHeld);
        if (result < 0) return ActionResult.CONSUME;
        if (result == 0) {
            world.playSound(null, entity.getPos(), SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, .5f, 1.2f);
            return ActionResult.SUCCESS;
        }
        world.playSound(null, entity.getPos(), SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, 2.5f, 1f);
        itemHeld.damage(1, player, user -> user.sendToolBreakStatus(Hand.MAIN_HAND));
        return ActionResult.SUCCESS;
    }


    private ActionResult pullOutItem(World world, StumpBlockEntity entity, PlayerEntity player) {
        BlockPos pos = entity.getPos();

        if (player.isCreative()) {
            entity.removeItem();
        } else if (!player.getInventory().insertStack(entity.removeItem())) {
            ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), entity.removeItem());
        }
        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_WOOD_HIT, SoundCategory.BLOCKS, .25f, .5f);
        return ActionResult.SUCCESS;
    }

    private ActionResult tryAddItemInHand(World world, StumpBlockEntity entity, PlayerEntity player, Hand hand) {
        ItemStack itemHeld = player.getStackInHand(hand);
        ItemStack itemOffhand = player.getOffHandStack();

        boolean inMainWithBlockOrEqInOff = (hand.equals(Hand.MAIN_HAND) && !(itemOffhand.isIn(ModTags.Items.OFFHAND_EQUIPMENT) || (itemHeld.getItem() instanceof BlockItem)));
        boolean inOffhandWithEq = (hand.equals(Hand.OFF_HAND) && itemOffhand.isIn(ModTags.Items.OFFHAND_EQUIPMENT));
        if (!itemOffhand.isEmpty() && (inMainWithBlockOrEqInOff || inOffhandWithEq) || itemHeld.isEmpty()) {
            return ActionResult.PASS;
        }
        if (entity.addItem(player.isCreative() ? itemHeld.copy() : itemHeld)) {
            world.playSound(null, entity.getPos(), SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1, 0.2f);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}
