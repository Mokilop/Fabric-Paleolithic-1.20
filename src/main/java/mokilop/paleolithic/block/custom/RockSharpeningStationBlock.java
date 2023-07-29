package mokilop.paleolithic.block.custom;

import mokilop.paleolithic.Paleolithic;
import mokilop.paleolithic.block.ModBlocks;
import mokilop.paleolithic.block.enums.StoneSharpeningStationBlockMode;
import mokilop.paleolithic.data.Constants;
import net.minecraft.block.*;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.text.Text;
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

public class RockSharpeningStationBlock extends HorizontalFacingBlock {
    private static VoxelShape SHAPE = Stream.of(
            Block.createCuboidShape(1, 10, 1, 15, 11, 15),
            Block.createCuboidShape(0, 2, 0, 16, 10, 16),
            Block.createCuboidShape(12, 0, 0, 16, 2, 4),
            Block.createCuboidShape(12, 0, 12, 16, 2, 16),
            Block.createCuboidShape(0, 0, 12, 4, 2, 16),
            Block.createCuboidShape(0, 0, 0, 4, 2, 4)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
    public static final Model PARENT_MODEL = new Model(Optional.of(new Identifier(Paleolithic.MOD_ID, "block/rock_sharpening_station")),
            Optional.empty());
    private int sharpeningCounter = 0;
    public static final EnumProperty<StoneSharpeningStationBlockMode> MODE = EnumProperty.of("mode", StoneSharpeningStationBlockMode.class);
    private WoodType woodType;
    private boolean isStripped;
    private TextureMap textureMap;
    public RockSharpeningStationBlock(Settings settings, WoodType woodType, boolean isStripped) {
        super(settings.sounds(woodType.soundType()));
        this.woodType = woodType;
        this.isStripped = isStripped;
        textureMap = new TextureMap().register(TextureKey.of("log"), TextureMap.getId(getLogBlock()))
                .register(TextureKey.of("log_top"), TextureMap.getSubId(getLogBlock(), "_top"));
    }
    public Block getLogBlock() {
        return (isStripped ? Constants.STRIPPED_LOGS_MAP : Constants.LOGS_MAP).get(woodType);
    }

    public WoodType getWoodType() {
        return woodType;
    }
    public boolean isStripped() {
        return isStripped;
    }


    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(FACING, ctx.getHorizontalPlayerFacing().getOpposite()).with(MODE, StoneSharpeningStationBlockMode.HATCHET_HEAD);
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
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!world.isClient() && hand == Hand.MAIN_HAND){
            if(player.getMainHandStack().isOf(ModBlocks.ROCK.asItem()) && hit.getSide() == Direction.UP){
                if(isCoolingDown(player)) return ActionResult.CONSUME;
                if(!isSharpeningSuccessful()){
                    return handleUnsuccessfulSharpening(world, pos, player);
                }
                return handleSuccessfulSharpening(state, world, pos, player);
            }
            return handleModeCycling(state, world, pos, player);
        }
        return ActionResult.SUCCESS;
    }

    private static boolean isCoolingDown(PlayerEntity player) {
        return player.getItemCooldownManager().isCoolingDown(player.getMainHandStack().getItem());
    }

    private ActionResult handleModeCycling(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        world.setBlockState(pos, state.cycle(MODE));
        sendSharpeningModeMessage(player, state.cycle(MODE));
        this.sharpeningCounter = 0;
        world.playSound(null, pos, SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.2f, 10f);
        return ActionResult.SUCCESS;
    }

    @NotNull
    private static ActionResult handleSuccessfulSharpening(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        player.sendMessage(Text.literal("Sharpening success"), true);
        if(!player.isCreative()){
            player.getMainHandStack().decrement(1);
        }
        world.spawnEntity(getCorrectSharpenedRockEntity(state, world, pos));
        player.getItemCooldownManager().set(player.getMainHandStack().getItem(), 20);
        world.playSound(null, pos, SoundEvents.BLOCK_STONE_HIT, SoundCategory.BLOCKS, 1f, 2f);
        return ActionResult.SUCCESS;
    }

    @NotNull
    private static ActionResult handleUnsuccessfulSharpening(World world, BlockPos pos, PlayerEntity player) {
        sendSharpeningMessage(player);
        player.getItemCooldownManager().set(player.getMainHandStack().getItem(), 3);
        world.playSound(null, pos, SoundEvents.BLOCK_STONE_HIT, SoundCategory.BLOCKS, 1f, 1.5f);
        return ActionResult.SUCCESS;
    }

    private static ItemEntity getCorrectSharpenedRockEntity(BlockState state, World world, BlockPos pos){
        Item sharpenedRock = state.get(MODE).getItem();
        ItemEntity entity =  new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, sharpenedRock.getDefaultStack().split(1));
        entity.setVelocity(0, 0, 0);
        return entity;
    }

    private boolean isSharpeningSuccessful(){
        boolean successfulSharpen = ++sharpeningCounter >= 5;
        sharpeningCounter = successfulSharpen ? 0 : sharpeningCounter;
        return successfulSharpen;
    }

    private static void sendSharpeningModeMessage(PlayerEntity player, BlockState state){
        player.sendMessage(state.get(MODE).asText(), true);
    }

    private static void sendSharpeningMessage(PlayerEntity player){
        player.sendMessage(Text.translatable("stone_sharpening_station.sharpening"), true);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(MODE);
    }


    public TextureMap getTextureMap() {
        return textureMap;
    }
}
