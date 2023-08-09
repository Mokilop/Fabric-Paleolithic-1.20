package mokilop.paleolithic.recipe;

import mokilop.paleolithic.Paleolithic;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {
    public static void registerRecipes() {
        Registry.register(Registries.RECIPE_TYPE, new Identifier(Paleolithic.MOD_ID, StumpChoppingRecipe.Type.ID), StumpChoppingRecipe.Type.INSTANCE);
        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Paleolithic.MOD_ID, StumpChoppingRecipe.Serializer.ID), StumpChoppingRecipe.Serializer.INSTANCE);
    Registry.register(Registries.RECIPE_TYPE, new Identifier(Paleolithic.MOD_ID, DryingRecipe.Type.ID), DryingRecipe.Type.INSTANCE);
        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Paleolithic.MOD_ID, DryingRecipe.Serializer.ID), DryingRecipe.Serializer.INSTANCE);
    }
}
