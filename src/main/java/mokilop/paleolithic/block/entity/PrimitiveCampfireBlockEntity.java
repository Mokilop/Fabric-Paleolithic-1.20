package mokilop.paleolithic.block.entity;

import mokilop.paleolithic.block.custom.PrimitiveCampfireBlock;
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

public class PrimitiveCampfireBlockEntity extends BlockEntityWithDisplayableInventory {
    public final RecipeManager.MatchGetter<Inventory, CampfireCookingRecipe> matchGetter = RecipeManager.createCachedMatchGetter(RecipeType.CAMPFIRE_COOKING);

    private static int multiplier = 2;
    private static final int maxProgress = 200 * multiplier;
    private static final int maxBurnTime = 200 * 128 * multiplier;
    public static final int acceptFuelBelowMax = 200 * multiplier;
    private int progress = 0;
    private int burnTime = 0;
    private int fireStrength = 0;

    public PrimitiveCampfireBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PRIMITIVE_CAMPFIRE, pos, state, 1);
    }

    public ItemStack getRenderStack(){
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


    public static void tick(World world, BlockPos blockPos, BlockState blockState,
                            PrimitiveCampfireBlockEntity entity) {
        if(world.isClient)return;
        if(entity.burnTime <= 0) {
            if(world.setBlockState(blockPos, blockState.with(PrimitiveCampfireBlock.LIT, false))) {
                entity.markDirty();
                markDirty(world, blockPos, blockState);
            }
            if(entity.progress > 0)entity.progress--;
            return;
        }
        entity.burnTime--;
        int newFireStrength = Math.min(4, entity.burnTime * 128 / maxBurnTime);
        if(newFireStrength != entity.fireStrength){
            entity.fireStrength = newFireStrength;
            world.setBlockState(blockPos, blockState.with(PrimitiveCampfireBlock.FIRE_STRENGTH, entity.fireStrength));
        }
        ItemStack itemInside = entity.inventory.get(0);
        SimpleInventory inv;
        boolean isSmeltable = entity.matchGetter.getFirstMatch(inv = new SimpleInventory(itemInside), world).isPresent();
        if(!isSmeltable || maxProgress > ++entity.progress){
            entity.markDirty();
            markDirty(world, blockPos, blockState);
            return;
        }
        ItemStack smeltedItem = entity.matchGetter.getFirstMatch(inv, world).map(recipe -> recipe.craft(inv, world.getRegistryManager())).orElse(itemInside);
        entity.inventory.set(0, ItemStack.EMPTY);
        ItemEntity cookedItemEntity =  new ItemEntity(world, blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, smeltedItem);
        cookedItemEntity.setVelocity(0, 0, 0);
        world.spawnEntity(cookedItemEntity);
        world.updateListeners(blockPos, blockState, blockState, Block.NOTIFY_ALL);
        world.emitGameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Emitter.of(blockState));
        entity.markDirty();
        markDirty(world, blockPos, blockState);
    }

    public boolean addItem(ItemStack itemStack) {
        if(!inventory.get(0).isEmpty())return false;
        inventory.set(0, itemStack.copyWithCount(1));
        progress = 0;
        markDirty();
        return true;
    }
    public boolean addFuel(ItemStack fuel) {
        int fuelTime = AbstractFurnaceBlockEntity
                .createFuelTimeMap()
                .getOrDefault(fuel.getItem(), 0) * multiplier;
        if(burnTime + fuelTime > maxBurnTime - acceptFuelBelowMax) return false;
        burnTime += fuelTime;
        markDirty();
        return true;
    }
}
