package mokilop.paleolithic.block.entity;

import mokilop.paleolithic.recipe.GrindingRecipe;
import mokilop.paleolithic.util.ProgressActionResult;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class GrindstoneBlockEntity extends BlockEntityWithDisplayableInventory {
    private static final int MAX_GRINDING_TICKS = 10;
    private int grindingTicks = MAX_GRINDING_TICKS;
    public boolean isGrinding = false;
    private static final int N_INPUT_SLOTS = 2;
    private static final int N_OUTPUT_SLOTS = 2;
    private int progress = 0;


    public GrindstoneBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GRINDSTONE, pos, state, N_INPUT_SLOTS + N_OUTPUT_SLOTS);
    }

    @Override
    public ItemStack getRenderStack() {
        return getStack(0);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("grindstone.progress", progress);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        progress = nbt.getInt("grindstone.progress");
    }

    private Optional<GrindingRecipe> getMatch(int slot) {
        SimpleInventory inv = new SimpleInventory(getStack(slot));
        return world.getRecipeManager().getFirstMatch(GrindingRecipe.Type.INSTANCE, inv, world);
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState,
                            GrindstoneBlockEntity entity) {
        if (entity.isGrinding && entity.grindingTicks > 0) {
            --entity.grindingTicks;
            return;
        }
        entity.grindingTicks = MAX_GRINDING_TICKS;
        entity.isGrinding = false;
    }

    private ProgressActionResult grindInSlot(int slot) {
        assert (slot < N_INPUT_SLOTS);
        if (isGrinding || world == null) return ProgressActionResult.FAIL;
        var match = getMatch(slot);
        if (match.isEmpty()) return ProgressActionResult.FAIL;
        GrindingRecipe recipe = match.get();
        if (++progress >= recipe.grindingTimes) {
            progress = 0;
            ItemStack result = recipe.getOutput(null);
            setStack(N_INPUT_SLOTS + slot, result);
            markDirty();
            return ProgressActionResult.COMPLETE;
        }
        return ProgressActionResult.PROGRESS;
    }

    public ProgressActionResult[] grind() {
        ProgressActionResult[] results = new ProgressActionResult[N_INPUT_SLOTS];
        for (int i = 0; i < N_INPUT_SLOTS; i++) {
            results[i] = grindInSlot(i);
        }
        return results;
    }

    public boolean addItem(ItemStack stack, int slot) {
        assert (slot < N_INPUT_SLOTS);
        progress = 0;
        if (getStack(slot).isEmpty()) {
            setStack(slot, stack.split(1));
            return true;
        }
        if (stack.isOf(getStack(slot).getItem()) && stack.getCount() < stack.getMaxCount()) {
            stack.decrement(1);
            getStack(slot).increment(1);
            return true;
        }
        return false;
    }
}
