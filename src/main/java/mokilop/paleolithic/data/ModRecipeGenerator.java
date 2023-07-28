package mokilop.paleolithic.data;

import mokilop.paleolithic.Paleolithic;
import mokilop.paleolithic.block.ModBlocks;
import mokilop.paleolithic.block.custom.StumpBlock;
import mokilop.paleolithic.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.block.WoodType;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;
import java.util.function.Consumer;

import static mokilop.paleolithic.data.Constants.*;

public class ModRecipeGenerator extends FabricRecipeProvider {

    public ModRecipeGenerator(FabricDataOutput output) {
        super(output);
    }
    
    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        generateCampfireRecipe(exporter);
        generateStumpRecipes(exporter);
    }
    private static void generateStumpRecipe(Consumer<RecipeJsonProvider> exporter, StumpBlock stump){
        Block logBlock = (stump.isStripped ? STRIPPED_LOGS_MAP : LOGS_MAP).get(stump.woodType);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, stump)
                .input(logBlock)
                .criterion(FabricRecipeProvider.hasItem(logBlock), FabricRecipeProvider.conditionsFromItem(logBlock))
                .group("stump")
                .offerTo(exporter, new Identifier(Paleolithic.MOD_ID, FabricRecipeProvider.getRecipeName(stump)));
    }
    private static void generateStumpRecipes(Consumer<RecipeJsonProvider> exporter){
        ModBlocks.getAllStumps().forEach((s) -> generateStumpRecipe(exporter, ((StumpBlock) s)));
    }
    private static void generateCampfireRecipe(Consumer<RecipeJsonProvider> exporter){
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.PRIMITIVE_CAMPFIRE)
                .input('R', ModBlocks.ROCK)
                .input('S', Items.STICK)
                .input('T', ModItems.PLANT_TWINE)
                .pattern("SSS")
                .pattern("RTR")
                .pattern("RRR")
                .criterion(FabricRecipeProvider.hasItem(ModBlocks.ROCK), FabricRecipeProvider.conditionsFromItem(ModBlocks.ROCK))
                .group("primitive_campfire")
                .offerTo(exporter, new Identifier(Paleolithic.MOD_ID, FabricRecipeProvider.getRecipeName(ModBlocks.PRIMITIVE_CAMPFIRE)));

    }
}
