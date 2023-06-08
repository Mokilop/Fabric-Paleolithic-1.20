package mokilop.paleolithic.block.custom;

import mokilop.paleolithic.block.ModBlocks;
import mokilop.paleolithic.block.enums.StoneSharpeningStationBlockMode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
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

import java.util.stream.Stream;

public class RockSharpeningStationBlock extends HorizontalFacingBlock {
    public RockSharpeningStationBlock(Settings settings) {
        super(settings);
    }

    private int sharpeningCounter = 0;

    public static final EnumProperty<StoneSharpeningStationBlockMode> MODE = EnumProperty.of("mode", StoneSharpeningStationBlockMode.class);

    private static VoxelShape SHAPE = Stream.of(
            Block.createCuboidShape(1, 12, 1, 15, 13, 15),
            Block.createCuboidShape(0, 2, 0, 16, 12, 16),
            Block.createCuboidShape(13, 0, 13, 16, 2, 16),
            Block.createCuboidShape(0, 0, 13, 3, 2, 16),
            Block.createCuboidShape(13, 0, 0, 16, 2, 3),
            Block.createCuboidShape(0, 0, 0, 3, 2, 3)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();


    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite()).with(MODE, StoneSharpeningStationBlockMode.HATCHET_HEAD);
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
        handleSound(world, pos, player, hit);
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

    private static void handleSound(World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if(player.getMainHandStack().isOf(ModBlocks.ROCK.asItem()) && hit.getSide() == Direction.UP){
            if(isCoolingDown(player)) return;
            world.playSound(player, pos, SoundEvents.BLOCK_STONE_HIT, SoundCategory.BLOCKS, 1f, 2f);
        }
        else{
            world.playSound(player, pos, SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.2f, 10f);
        }
    }

    private ActionResult handleModeCycling(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        world.setBlockState(pos, state.cycle(MODE));
        sendSharpeningModeMessage(player, state.cycle(MODE));
        this.sharpeningCounter = 0;
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
        return ActionResult.SUCCESS;
    }

    @NotNull
    private static ActionResult handleUnsuccessfulSharpening(World world, BlockPos pos, PlayerEntity player) {
        sendSharpeningMessage(player);
        player.getItemCooldownManager().set(player.getMainHandStack().getItem(), 3);
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
}
