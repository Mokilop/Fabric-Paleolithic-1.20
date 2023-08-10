package mokilop.paleolithic.block.entity;

import mokilop.paleolithic.recipe.DryingRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class DryingRackBlockEntity extends BlockEntityWithDisplayableInventory {
    private static final int maxProgress = 3600;
    private int progress = 0;
    public DryingRackBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DRYING_RACK, pos, state, 1);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("drying_rack.progress", progress);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        progress = nbt.getInt("drying_rack.progress");
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState,
                            DryingRackBlockEntity entity) {
        Optional<DryingRecipe> match = getMatch(entity);
        if(world.isClient || match.isEmpty())return;
        entity.progress++;
        if(entity.progress >= maxProgress){
            entity.progress = 0;
            entity.setStack(0, match.get().getOutput(null));
            entity.markDirty();
        }
    }

    private static Optional<DryingRecipe> getMatch(DryingRackBlockEntity entity) {
        SimpleInventory inv = new SimpleInventory(entity.getStack(0));
        World w = entity.getWorld();
        return w.getRecipeManager().getFirstMatch(DryingRecipe.Type.INSTANCE, inv, w);
    }
    public static void resetProgress(DryingRackBlockEntity entity){
        entity.progress = 0;
    }

    public ItemStack getRenderStack() {
        return getStack(0);
    }
}
