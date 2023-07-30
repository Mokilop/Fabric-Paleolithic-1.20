package mokilop.paleolithic.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class CraftingStumpBlockEntity extends BlockEntityWithDisplayableInventory {
    private final int maxProgress = 3;
    private int progress = 0;

    public CraftingStumpBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRAFTING_STUMP, pos, state, 9);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("crafting_stump.progress", progress);
    }
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        progress = nbt.getInt("crafting_stump.progress");
    }
}
