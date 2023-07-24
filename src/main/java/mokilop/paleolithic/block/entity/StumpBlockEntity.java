package mokilop.paleolithic.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class StumpBlockEntity extends BlockEntity implements ImplementedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public StumpBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STUMP, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return null;
    }
}
