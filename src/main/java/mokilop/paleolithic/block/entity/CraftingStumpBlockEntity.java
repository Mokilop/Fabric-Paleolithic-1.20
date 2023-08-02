package mokilop.paleolithic.block.entity;

import mokilop.paleolithic.item.custom.HammerItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class CraftingStumpBlockEntity extends BlockEntityWithDisplayableInventory {
    private static final int maxProgress = 8;
    private int progress = 0;
    private final int[] randomRotationAmounts = new int[9];
    private static final int maxRandomRotationAmount = 6;
    public boolean crafting = false;
    public int craftingTicks = 0;
    public static final int maxCraftingTicks = 1;
    public CraftingStumpBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRAFTING_STUMP, pos, state, 10);
        updateRandomRotationAmounts(this);
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
    private static Optional<CraftingRecipe> getMatch(CraftingStumpBlockEntity entity) {
        CraftingInventory inv = new CraftingInventory(null, 3,  3, entity.getItems());
        return entity.getWorld().getRecipeManager()
                .getFirstMatch(RecipeType.CRAFTING, inv, entity.getWorld());
    }
    public static void updateRandomRotationAmounts(CraftingStumpBlockEntity entity){
        if(!entity.hasWorld())return;
        for(int i = 0; i < 9;  i++){
            entity.randomRotationAmounts[i] = entity.getWorld().getRandom().nextBetween(-maxRandomRotationAmount, maxRandomRotationAmount);
        }
    }
    public static int[] getRandomRotationAmounts(CraftingStumpBlockEntity entity){
        return entity.randomRotationAmounts;
    }
    public static boolean attemptCraft(CraftingStumpBlockEntity entity, HammerItem hammer) {
        if(entity.crafting)return false;
        entity.crafting = true;
        if (getMatch(entity).isEmpty() || entity.getWorld().isClient()) return false;
        entity.progress += hammer.CRAFTING_EFFICIENCY;
        if (!entity.getWorld().isClient() && entity.progress >= maxProgress) {
            return craft(entity);
        }
        return false;
    }

    private static boolean craft(CraftingStumpBlockEntity entity) {
        entity.progress = 0;
        ItemEntity result = new ItemEntity(entity.getWorld(), entity.getPos().getX() + 0.5, entity.getPos().getY() + 1, entity.getPos().getZ() + 0.5,
                getMatch(entity).get().getOutput(null), 0, 0, 0);
        entity.getWorld().spawnEntity(result);
        ItemStack extraItem = entity.getStack(9);
        entity.clear();
        entity.setStack(9, extraItem);
        entity.markDirty();
        return true;
    }

    public boolean addStack(int slot, ItemStack stack){
        if(getStack(slot).isEmpty()){
            setStack(slot, stack.copyWithCount(1));
            markDirty();
            return true;
        }
        return false;
    }
    public static void tick(World world, BlockPos blockPos, BlockState blockState,
                            CraftingStumpBlockEntity entity){
        if(entity.crafting){
            entity.craftingTicks++;
        }
        if(entity.craftingTicks > maxCraftingTicks){
            entity.craftingTicks = 0;
            entity.crafting = false;
            updateRandomRotationAmounts(entity);
        }
    }
}
