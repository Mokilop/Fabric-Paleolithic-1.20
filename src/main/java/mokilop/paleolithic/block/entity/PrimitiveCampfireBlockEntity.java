package mokilop.paleolithic.block.entity;

import mokilop.paleolithic.block.custom.PrimitiveCampfireBlock;
import mokilop.paleolithic.block.custom.StumpBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Optional;

import static net.minecraft.recipe.BrewingRecipeRegistry.hasRecipe;

public class PrimitiveCampfireBlockEntity extends BlockEntityWithDisplayableInventory {
    private static final int multiplier = 2;
    private static final int maxProgress = 200 * multiplier;
    private static final int maxBurnTime = 200 * 128 * multiplier;
    public static final int acceptFuelBelowMax = 200 * multiplier;
    public final RecipeManager.MatchGetter<Inventory, CampfireCookingRecipe> matchGetter = RecipeManager.createCachedMatchGetter(RecipeType.CAMPFIRE_COOKING);
    private int progress = 0;
    private int burnTime = 0;
    private int fireStrength = 0;

    public PrimitiveCampfireBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PRIMITIVE_CAMPFIRE, pos, state, 1);
    }
    private boolean isBurning(){
        return burnTime > 0;
    }

    public static void tick(World world, BlockPos pos, BlockState state,
                            PrimitiveCampfireBlockEntity entity) {
        if (world.isClient) return;
        boolean wasBurning = entity.isBurning();
        int oldFireStrength = entity.fireStrength;
        if(!entity.isBurning()){
            entity.progress = Math.max(entity.progress - 1, 0);
            return;
        }
        entity.burnTime--;
        entity.fireStrength = entity.burnTime >= 1200 ? 4 : entity.burnTime / 300;
        Optional<CampfireCookingRecipe> match = getMatch(entity, world);
        if(match.isPresent()){
            if(++entity.progress >= match.get().getCookTime()){
                craftItem(entity, world, pos, match);
            }
        }
        if(entity.fireStrength != oldFireStrength || entity.isBurning() != wasBurning){
            state = state.with(PrimitiveCampfireBlock.LIT, entity.isBurning())
                    .with(PrimitiveCampfireBlock.FIRE_STRENGTH, entity.fireStrength);
            world.setBlockState(pos, state, Block.NOTIFY_ALL);
        }
    }

    private static void craftItem(PrimitiveCampfireBlockEntity entity, World world, BlockPos pos, Optional<CampfireCookingRecipe> match) {
        assert match.isPresent();
        ItemStack result = match.get().getOutput(world.getRegistryManager());
        entity.clear();
        entity.progress = 0;
        world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY() + 1, pos.getZ(), result, 0, 1, 0));
        entity.markDirty();
    }

    public static Optional<CampfireCookingRecipe> getMatch(PrimitiveCampfireBlockEntity e, World world){
        return world.getRecipeManager()
                .getFirstMatch(RecipeType.CAMPFIRE_COOKING, new SimpleInventory(e.getStack(0)), world);
    }

    public ItemStack getRenderStack() {
        return getStack(0);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("primitive_campfire.progress", progress);
        nbt.putInt("primitive_campfire.burn_time", burnTime);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        progress = nbt.getInt("primitive_campfire.progress");
        burnTime = nbt.getInt("primitive_campfire.burn_time");
    }

    public boolean addItem(ItemStack itemStack) {
        if (!inventory.get(0).isEmpty()) return false;
        inventory.set(0, itemStack.copyWithCount(1));
        progress = 0;
        markDirty();
        return true;
    }

    public boolean addFuel(ItemStack fuel) {
        int fuelTime = AbstractFurnaceBlockEntity
                .createFuelTimeMap()
                .getOrDefault(fuel.getItem(), 0) * multiplier;
        if (burnTime + fuelTime > maxBurnTime - acceptFuelBelowMax) return false;
        burnTime += fuelTime;
        markDirty();
        return true;
    }
}
