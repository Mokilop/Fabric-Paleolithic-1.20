package mokilop.paleolithic.data;

import mokilop.paleolithic.block.ModBlocks;
import mokilop.paleolithic.item.ModItems;
import mokilop.paleolithic.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(ModTags.Items.HAMMERS).add(ModItems.STONE_HAMMER);
        getOrCreateTagBuilder(ModTags.Items.CRAFTING_STUMPS)
                .add(ModBlocks.getAllCraftingStumps().map(Block::asItem).toArray(Item[]::new));

        getOrCreateTagBuilder(ModTags.Items.STUMPS)
                .add(ModBlocks.getAllStumps().map(Block::asItem).toArray(Item[]::new));

        getOrCreateTagBuilder(ModTags.Items.ROCK_SHARPENING_STATIONS)
                .add(ModBlocks.getAllSharpeningStumps().map(Block::asItem).toArray(Item[]::new));

        getOrCreateTagBuilder(ModTags.Items.DRYING_RACKS)
                .add(ModBlocks.getAllDryingRacks().map(Block::asItem).toArray(Item[]::new));

    }
}
