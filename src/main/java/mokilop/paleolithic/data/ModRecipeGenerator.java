package mokilop.paleolithic.data;

import mokilop.paleolithic.Paleolithic;
import mokilop.paleolithic.block.ModBlocks;
import mokilop.paleolithic.block.custom.CraftingStumpBlock;
import mokilop.paleolithic.block.custom.SharpeningStumpBlock;
import mokilop.paleolithic.block.custom.StumpBlock;
import mokilop.paleolithic.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class ModRecipeGenerator extends FabricRecipeProvider {

    public ModRecipeGenerator(FabricDataOutput output) {
        super(output);
    }

    private static void generateStumpRecipe(Consumer<RecipeJsonProvider> exporter, StumpBlock stump) {
        Block logBlock = stump.getLogBlock();
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, stump)
                .input(logBlock)
                .criterion(FabricRecipeProvider.hasItem(logBlock), FabricRecipeProvider.conditionsFromItem(logBlock))
                .group("stump")
                .offerTo(exporter, new Identifier(Paleolithic.MOD_ID, FabricRecipeProvider.getRecipeName(stump)));
    }

    private static void generateCraftingStumpRecipe(Consumer<RecipeJsonProvider> exporter, CraftingStumpBlock crStump){
        Block logBlock = crStump.getLogBlock();
        Block stump = ModBlocks.getAllStumps().map(b -> (StumpBlock)b).filter(b -> b.getLogBlock() == logBlock).findFirst().orElseThrow();
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, crStump)
                .input('L', logBlock)
                .input('S', stump)
                .pattern("S")
                .pattern("L")
                .criterion(FabricRecipeProvider.hasItem(logBlock), FabricRecipeProvider.conditionsFromItem(logBlock))
                .criterion(FabricRecipeProvider.hasItem(stump), FabricRecipeProvider.conditionsFromItem(stump))
                .group("crafting_stump")
                .offerTo(exporter, new Identifier(Paleolithic.MOD_ID, FabricRecipeProvider.getRecipeName(crStump)));
    }

    private static void generateRockSharpeningStationRecipe(Consumer<RecipeJsonProvider> exporter, SharpeningStumpBlock stationBlock) {
        Block logBlock = stationBlock.getLogBlock();
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, stationBlock)
                .input(logBlock)
                .input(ModBlocks.ROCK)
                .input(ModBlocks.ROCK)
                .criterion(FabricRecipeProvider.hasItem(logBlock), FabricRecipeProvider.conditionsFromItem(logBlock))
                .group("rock_sharpening_station")
                .offerTo(exporter, new Identifier(Paleolithic.MOD_ID, FabricRecipeProvider.getRecipeName(stationBlock)));
    }

    private static void generateHatchetRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible hatchedHead, ItemConvertible hatchetTwine, ItemConvertible hatchetHandle, Item hatchet){

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, hatchet)
                .input('H', hatchedHead)
                .input('T', hatchetTwine)
                .input('S', hatchetHandle)
                .pattern("HT")
                .pattern(" S")
                .criterion(FabricRecipeProvider.hasItem(hatchedHead), FabricRecipeProvider.conditionsFromItem(hatchedHead))
                .criterion(FabricRecipeProvider.hasItem(hatchetTwine), FabricRecipeProvider.conditionsFromItem(hatchetTwine))
                .criterion(FabricRecipeProvider.hasItem(hatchetHandle), FabricRecipeProvider.conditionsFromItem(hatchetHandle))
                .offerTo(exporter, new Identifier(Paleolithic.MOD_ID, FabricRecipeProvider.getRecipeName(hatchet)));
    }

    private static void generateHatchetRecipes(Consumer<RecipeJsonProvider> exporter){
        generateHatchetRecipe(exporter, ModItems.FLAKED_ROCK, ModItems.PLANT_TWINE, Items.STICK, ModItems.STONE_HATCHET);
    }

    private static void generateCraftingStumpRecipes(Consumer<RecipeJsonProvider> exporter){
        ModBlocks.getAllCraftingStumps().forEach(cs -> generateCraftingStumpRecipe(exporter, (CraftingStumpBlock) cs));
    }

    private static void generateStumpRecipes(Consumer<RecipeJsonProvider> exporter) {
        ModBlocks.getAllStumps().forEach((s) -> generateStumpRecipe(exporter, ((StumpBlock) s)));
    }

    private static void generateRockSharpeningStationRecipes(Consumer<RecipeJsonProvider> exporter) {
        ModBlocks.getAllSharpeningStumps().forEach((s) -> generateRockSharpeningStationRecipe(exporter, (SharpeningStumpBlock) s));
    }

    private static void generateCampfireRecipe(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.PRIMITIVE_CAMPFIRE)
                .input('R', ModBlocks.ROCK)
                .input('S', Items.STICK)
                .input('T', ModItems.PLANT_TWINE)
                .pattern("SRS")
                .pattern("RTR")
                .pattern("SRS")
                .criterion(FabricRecipeProvider.hasItem(ModBlocks.ROCK), FabricRecipeProvider.conditionsFromItem(ModBlocks.ROCK))
                .group("primitive_campfire")
                .offerTo(exporter, new Identifier(Paleolithic.MOD_ID, FabricRecipeProvider.getRecipeName(ModBlocks.PRIMITIVE_CAMPFIRE)));
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        generateCampfireRecipe(exporter);
        generateStumpRecipes(exporter);
        generateRockSharpeningStationRecipes(exporter);
        generateCraftingStumpRecipes(exporter);
        generateHatchetRecipes(exporter);
    }
}
