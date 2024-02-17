package mokilop.paleolithic.block.entity;

import mokilop.paleolithic.inventory.CraftingStumpCraftingInventory;
import mokilop.paleolithic.item.custom.HammerItem;
import mokilop.paleolithic.util.ModTags;
import mokilop.paleolithic.util.ProgressActionResult;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class CraftingStumpBlockEntity extends BlockEntityWithDisplayableInventory {
    private static final int maxProgress = 8;
    public static final int width = 3;
    public static final int height = 3;
    public static final int numberOfCraftingSlots = width * height;
    private int progress = 0;
    private final int[] randomRotationAmounts = new int[numberOfCraftingSlots];
    private static final int maxRandomRotationAmount = 6;
    public boolean crafting = false;
    public int craftingTicks = 0;
    public static final int maxCraftingTicks = 2;
    public final CraftingStumpCraftingInventory craftingInventory = new CraftingStumpCraftingInventory(this);
    public final CraftingResultInventory craftingResultInventory = new CraftingResultInventory();

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

    @Override
    public ItemStack getRenderStack() {
        return getStack(9);
    }

    public static void updateRandomRotationAmounts(CraftingStumpBlockEntity entity) {
        if (!entity.hasWorld()) return;
        for (int i = 0; i < entity.randomRotationAmounts.length; i++) {
            entity.randomRotationAmounts[i] = entity.getWorld().getRandom().nextBetween(-maxRandomRotationAmount, maxRandomRotationAmount);
        }
    }

    public static int[] getRandomRotationAmounts(CraftingStumpBlockEntity entity) {
        return entity.randomRotationAmounts;
    }

    private Optional<CraftingRecipe> getMatch() {
        assert (getWorld() != null);
        return getWorld().getRecipeManager()
                .getFirstMatch(RecipeType.CRAFTING, craftingInventory, getWorld());
    }

    public ProgressActionResult attemptCraft(ItemStack itemHeld, PlayerEntity player) {
        if (!itemHeld.isIn(ModTags.Items.HAMMERS) || !(itemHeld.getItem() instanceof HammerItem hammer) || crafting) {
            return ProgressActionResult.FAIL;
        }
        World world = getWorld();
        assert world != null;
        crafting = true;
        if (world.isClient) return ProgressActionResult.PROGRESS_PARTIAL;
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        Optional<CraftingRecipe> match = getMatch();
        CraftingRecipe recipe;
        ItemStack result = ItemStack.EMPTY;
        boolean shouldCraft = match.isPresent() && craftingResultInventory.shouldCraftRecipe(world, serverPlayer, recipe = match.get())
                && (result = recipe.craft(craftingInventory, world.getRegistryManager())).isItemEnabled(world.getEnabledFeatures());
        if (!shouldCraft) return ProgressActionResult.PROGRESS_PARTIAL;

        progress += hammer.CRAFTING_EFFICIENCY;
        if (progress >= maxProgress) {
            progress = 0;
            ItemEntity resultE = new ItemEntity(getWorld(), getPos().getX() + 0.5,
                    getPos().getY() + 1, getPos().getZ() + 0.5, result);
            getWorld().spawnEntity(resultE);
            clearCraftingGrid();
            markDirty();
            return ProgressActionResult.COMPLETE;
        }
        return ProgressActionResult.PROGRESS;
    }

    public void clearCraftingGrid() {
        for (int i = 0; i < numberOfCraftingSlots; i++) {
            ItemStack current = getStack(i);
            ItemStack residualItem = current.getRecipeRemainder();
            setStack(i, residualItem);
        }
    }

    public boolean addStack(int slot, ItemStack stack) {
        if (getStack(slot).isEmpty()) {
            setStack(slot, stack.split(1));
            markDirty();
            return true;
        }
        return false;
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState,
                            CraftingStumpBlockEntity entity) {
        if (entity.crafting) {
            entity.craftingTicks++;
        }
        if (entity.craftingTicks > maxCraftingTicks) {
            entity.craftingTicks = 0;
            entity.crafting = false;
            updateRandomRotationAmounts(entity);
        }
    }
}
