package mokilop.paleolithic.data;

import mokilop.paleolithic.Paleolithic;
import mokilop.paleolithic.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

import static mokilop.paleolithic.data.Constants.STATION_TYPES;
import static mokilop.paleolithic.data.Constants.WOOD_TYPES;

public class ModRecipeGenerator extends FabricRecipeProvider {

    public ModRecipeGenerator(FabricDataOutput output) {
        super(output);
    }
    
    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        generateRockSharpeningStationRecipes(exporter);
    }

    private static void generateRockSharpeningStationRecipes(Consumer<RecipeJsonProvider> exporter) {

        for(int i = 0; i < STATION_TYPES.length; i++){
            ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, STATION_TYPES[i])
                    .input(ModBlocks.ROCK)
                    .input(ModBlocks.ROCK)
                    .input(WOOD_TYPES[i])
                    .criterion(FabricRecipeProvider.hasItem(WOOD_TYPES[i]), FabricRecipeProvider.conditionsFromItem(WOOD_TYPES[i]))
                    .group("rock_sharpening_station")
                    .offerTo(exporter, new Identifier(Paleolithic.MOD_ID, FabricRecipeProvider.getRecipeName(STATION_TYPES[i])));
        }
    }
}
