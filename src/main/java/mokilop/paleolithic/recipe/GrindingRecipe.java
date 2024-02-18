package mokilop.paleolithic.recipe;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

public class GrindingRecipe extends SingleIngredienRecipe {
    public final int grindingTimes;

    protected GrindingRecipe(Identifier id, ItemStack output, DefaultedList<Ingredient> recipeItems, int grindingTimes) {
        super(id, output, recipeItems);
        this.grindingTimes = grindingTimes;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<GrindingRecipe> {
        public static final GrindingRecipe.Type INSTANCE = new Type();
        public static final String ID = "grinding";

        private Type() {
        }
    }

    public static class Serializer implements RecipeSerializer<GrindingRecipe> {
        public static final GrindingRecipe.Serializer INSTANCE = new GrindingRecipe.Serializer();
        public static final String ID = "grinding";

        @Override
        public GrindingRecipe read(Identifier id, JsonObject json) {
            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"));
            JsonObject input = JsonHelper.getObject(json, "input");
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(1, Ingredient.EMPTY);
            inputs.set(0, Ingredient.fromJson(input));
            int grindingTimes = JsonHelper.getInt(json, "grinding_times", 8);
            return new GrindingRecipe(id, output, inputs, grindingTimes);
        }

        @Override
        public GrindingRecipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);

            inputs.replaceAll(ignored -> Ingredient.fromPacket(buf));

            ItemStack output = buf.readItemStack();
            int grindingTimes = buf.readInt();
            return new GrindingRecipe(id, output, inputs, grindingTimes);
        }

        @Override
        public void write(PacketByteBuf buf, GrindingRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.write(buf);
            }
            buf.writeItemStack(recipe.getOutput(null));
            buf.writeInt(recipe.grindingTimes);
        }
    }
}
