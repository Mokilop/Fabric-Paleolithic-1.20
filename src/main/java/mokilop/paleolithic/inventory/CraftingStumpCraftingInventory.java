package mokilop.paleolithic.inventory;

import mokilop.paleolithic.block.entity.CraftingStumpBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeMatcher;

import java.util.List;

public class CraftingStumpCraftingInventory implements RecipeInputInventory {
    private final CraftingStumpBlockEntity entity;

    public CraftingStumpCraftingInventory(CraftingStumpBlockEntity entity) {
        this.entity = entity;
    }

    @Override
    public int getWidth() {
        return CraftingStumpBlockEntity.width;
    }

    @Override
    public int getHeight() {
        return CraftingStumpBlockEntity.height;
    }

    @Override
    public List<ItemStack> getInputStacks() {
        int i = 0;
        return entity.getItems().stream().limit(9).toList();
    }

    @Override
    public int size() {
        return CraftingStumpBlockEntity.numberOfCraftingSlots;
    }

    @Override
    public boolean isEmpty() {
        return entity.getItems().stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getStack(int slot) {
        return entity.getStack(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return entity.removeStack(slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return entity.removeStack(slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        entity.setStack(slot, stack);
    }

    @Override
    public void markDirty() {
        entity.markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void provideRecipeInputs(RecipeMatcher finder) {
        for (ItemStack itemStack : this.getInputStacks()) {
            finder.addUnenchantedInput(itemStack);
        }
    }

    @Override
    public void clear() {
        entity.clearCraftingGrid();
    }
}
