package mokilop.paleolithic.recipe;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class StumpChoppingRecipe implements Recipe<SimpleInventory> {
    private final Identifier id;
    private final ItemStack output;
    private final DefaultedList<Ingredient> recipeItems;

    public StumpChoppingRecipe(Identifier id, ItemStack output, DefaultedList<Ingredient> recipeItems) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if (world.isClient()) return false;

        return recipeItems.get(0).test(inventory.getStack(0)) && recipeItems.get(1).test(inventory.getStack(1));
    }

    @Override
    public ItemStack craft(SimpleInventory inventory, DynamicRegistryManager registryManager) {
        return output;
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
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class JsonFormat {
        JsonObject tool;
        JsonObject input;
        String result;
        int count;
    }

    public static class Type implements RecipeType<StumpChoppingRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "stump_chopping";
        private Type() {
        }
    }

    public static class Serializer implements RecipeSerializer<StumpChoppingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "stump_chopping";

        @Override
        public StumpChoppingRecipe read(Identifier id, JsonObject json) {
            JsonFormat recipeJson = new Gson().fromJson(json, JsonFormat.class);
            Ingredient input = Ingredient.fromJson(recipeJson.input);
            Ingredient tool = Ingredient.fromJson(recipeJson.tool);
            Item resultItem = Registries.ITEM.getOrEmpty(new Identifier(recipeJson.result)).get();
            ItemStack result = new ItemStack(resultItem, recipeJson.count);
            DefaultedList<Ingredient> ingList = DefaultedList.ofSize(2, Ingredient.EMPTY);
            ingList.set(0, tool);
            ingList.set(1, input);
            return new StumpChoppingRecipe(id, result, ingList);
        }

        @Override
        public StumpChoppingRecipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromPacket(buf));
            }

            ItemStack output = buf.readItemStack();
            return new StumpChoppingRecipe(id, output, inputs);
        }

        @Override
        public void write(PacketByteBuf buf, StumpChoppingRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.write(buf);
            }
            buf.writeItemStack(recipe.getOutput(null));
        }
    }
}
