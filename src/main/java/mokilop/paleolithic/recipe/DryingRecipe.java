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

public class DryingRecipe extends SingleIngredienRecipe {
    public static final String ID = "drying";
    public final int dryingTicks;

    public DryingRecipe(Identifier id, ItemStack output, DefaultedList<Ingredient> recipeItems, int dryingTicks) {
        super(id, output, recipeItems);
        this.dryingTicks = dryingTicks;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<DryingRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = DryingRecipe.ID;

        private Type() {
        }
    }

    public static class Serializer implements RecipeSerializer<DryingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = DryingRecipe.ID;

        @Override
        public DryingRecipe read(Identifier id, JsonObject json) {
            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"));
            JsonObject input = JsonHelper.getObject(json, "input");
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(1, Ingredient.fromJson(input));
            int dryingTicks = JsonHelper.getInt(json, "time", 3600);
            return new DryingRecipe(id, output, inputs, dryingTicks);
        }

        @Override
        public DryingRecipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);
            inputs.replaceAll(ignored -> Ingredient.fromPacket(buf));
            ItemStack output = buf.readItemStack();
            int dryingTicks = buf.readInt();
            return new DryingRecipe(id, output, inputs, dryingTicks);
        }

        @Override
        public void write(PacketByteBuf buf, DryingRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.write(buf);
            }
            buf.writeItemStack(recipe.output);
            buf.writeInt(recipe.dryingTicks);
        }
    }
}

