package mokilop.paleolithic.block.entity;

import mokilop.paleolithic.recipe.StumpChoppingRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class StumpBlockEntity extends BlockEntityWithDisplayableInventory {

    private static final int maxProgress = 6;
    private int progress = 0;

    public StumpBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STUMP, pos, state, 1);
    }

    public ItemStack getStoredItem() {
        return getStack(0);
    }

    private Optional<StumpChoppingRecipe> getMatchForChopping(ItemStack tool) {
        SimpleInventory inv = new SimpleInventory(size() + 1);
        inv.setStack(0, tool);
        inv.setStack(1, getStack(0));
        return getWorld().getRecipeManager()
                .getFirstMatch(StumpChoppingRecipe.Type.INSTANCE, inv, getWorld());
    }

    private void craftItemFromChopping(ItemStack tool) {
        Optional<StumpChoppingRecipe> match = getMatchForChopping(tool);
        if (match.isEmpty()) return;
        ItemStack outputItems = match.get().getOutput(getWorld().getRegistryManager());
        BlockPos pos = getPos();
        World world = getWorld();
        ItemEntity outputEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5, outputItems, 0, 0, 0);
        clear();
        world.spawnEntity(outputEntity);
    }

    public int chop(World world, ItemStack tool) {
        if (world == null) return -1;
        if (getMatchForChopping(tool).isEmpty()) {
            return -1;
        }
        if (++progress >= maxProgress) {
            progress = 0;
            craftItemFromChopping(tool);
            markDirty();
            return 1;
        }
        return 0;
    }

    public boolean addItem(ItemStack item) {
        if (!inventory.get(0).isEmpty() || item.isEmpty()) return false;
        inventory.set(0, item.split(1));
        progress = 0;
        markDirty();
        return true;
    }

    public ItemStack removeItem() {
        if (isEmpty()) return ItemStack.EMPTY;
        ItemStack item = getStoredItem().split(1);
        progress = 0;
        markDirty();
        return item;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("stump.progress", progress);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        progress = nbt.getInt("stump.progress");
    }

    public ItemStack getRenderStack() {
        return getStack(0);
    }
}
