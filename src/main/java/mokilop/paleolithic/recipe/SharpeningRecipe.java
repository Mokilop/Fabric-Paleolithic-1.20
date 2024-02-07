package mokilop.paleolithic.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class SharpeningRecipe implements Recipe<SimpleInventory> {
    private final Identifier id;
    private final ItemStack output;
    private final DefaultedList<Ingredient> recipeItems;
    public final int sharpenTimes;

    public SharpeningRecipe(Identifier id, ItemStack output, DefaultedList<Ingredient> recipeItems, int sharpenTimes) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
        this.sharpenTimes = sharpenTimes;
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if(world.isClient()){
            return false;
        }
        return recipeItems.get(0).test(inventory.getStack(0));
    }

    @Override
    public ItemStack craft(SimpleInventory inventory, DynamicRegistryManager registryManager) {
        return output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return output.copy();
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return recipeItems;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return SharpeningRecipe.Type.INSTANCE;
    }

    public static class Type implements RecipeType<SharpeningRecipe> {
        public static final SharpeningRecipe.Type INSTANCE = new SharpeningRecipe.Type();
        public static final String ID = "sharpening";

        private Type() {
        }
    }
    public static class Serializer implements RecipeSerializer<SharpeningRecipe> {
        public static final SharpeningRecipe.Serializer INSTANCE = new SharpeningRecipe.Serializer();
        public static final String ID = "sharpening";
        // this is the name given in the json file

        @Override
        public SharpeningRecipe read(Identifier id, JsonObject json) {
            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"));
            JsonObject input = JsonHelper.getObject(json, "input");
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(1, Ingredient.EMPTY);
            inputs.set(0, Ingredient.fromJson(input));
            int sharpenTimes = JsonHelper.getInt(json, "sharpen_times");
            return new SharpeningRecipe(id, output, inputs, sharpenTimes);
        }

        @Override
        public SharpeningRecipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromPacket(buf));
            }

            ItemStack output = buf.readItemStack();
            int sharpenTimes = buf.readInt();
            return new SharpeningRecipe(id, output, inputs, sharpenTimes);
        }

        @Override
        public void write(PacketByteBuf buf, SharpeningRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.write(buf);
            }
            buf.writeItemStack(recipe.getOutput(null));
            buf.writeInt(recipe.sharpenTimes);
        }
    }

}
