package mokilop.paleolithic.block.custom;

import mokilop.paleolithic.Paleolithic;
import mokilop.paleolithic.block.entity.StumpBlockEntity;
import mokilop.paleolithic.data.Constants;
import mokilop.paleolithic.sound.ModSounds;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class StumpBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final Model PARENT_MODEL = new Model(Optional.of(new Identifier(Paleolithic.MOD_ID, "block/stump")),
            Optional.empty());
    private WoodType woodType;
    private boolean isStripped;
    private TextureMap textureMap;
    public static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 6, 16);

    public StumpBlock(Settings settings, WoodType woodType, boolean stripped)
    {
        super(settings.sounds(woodType.soundType()));
        this.woodType = woodType;
        isStripped = stripped;
        textureMap = new TextureMap().register(TextureKey.of("log"), TextureMap.getId(getLogBlock()))
                .register(TextureKey.of("log_top"), TextureMap.getSubId(getLogBlock(), "_top"));
    }
    public WoodType getWoodType() {
        return woodType;
    }
    public boolean getIsStripped(){
        return isStripped;
    }
    public TextureMap getTextureMap(){
        return textureMap;
    }
    public Model getParentModel(){
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
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof StumpBlockEntity) {
                ItemScatterer.spawn(world, pos, (StumpBlockEntity)blockEntity);
                world.updateComparators(pos,this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(hit.getSide() != Direction.UP)return ActionResult.PASS;
        if(world.isClient())return ActionResult.CONSUME;
        if(world.getBlockEntity(pos) instanceof StumpBlockEntity entity){
            ItemStack hs = player.getMainHandStack();
            if(hs.getItem() instanceof ToolItem)hs = player.getOffHandStack();
            if(hs.isEmpty()) {
                if(entity.removeItem(player)) return ActionResult.CONSUME_PARTIAL;
            }
            if(entity.addItem(hs.copyWithCount(1))){
                hs.decrement(player.isCreative()?0:1);
            }
            return ActionResult.CONSUME_PARTIAL;
        }
        return ActionResult.PASS;
    }

    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        if(world.isClient())return;
        if(world.getBlockEntity(pos) instanceof StumpBlockEntity entity){
            ItemStack mhs = player.getMainHandStack();
            if(mhs.getItem() instanceof MiningToolItem toolItem){
                boolean fullyCharged = player.getAttackCooldownProgress(0) == 1;
                boolean highDamage = toolItem.getAttackDamage() >= 8;
                boolean successful = StumpBlockEntity.chop(world, pos, state, entity, mhs, fullyCharged, highDamage);
                mhs.damage(successful ? 1 : 0, world.getRandom(), (ServerPlayerEntity)player);
                if(successful && world.getRandom().nextInt(256) == 0){
                    world.playSound(null, pos, ModSounds.STUMP_SHATTER, SoundCategory.BLOCKS, 1, 1);
                    world.removeBlock(pos, false);
                }
            }
        }
    }
}
