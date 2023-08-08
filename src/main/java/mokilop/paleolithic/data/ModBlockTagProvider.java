package mokilop.paleolithic.data;

import mokilop.paleolithic.block.ModBlocks;
import mokilop.paleolithic.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(ModTags.Blocks.ROCK_SHARPENING_STATIONS)
                .add(ModBlocks.getAllRockSharpeningStations().toArray(Block[]::new));

        getOrCreateTagBuilder(ModTags.Blocks.STUMPS)
                .add(ModBlocks.getAllStumps().toArray(Block[]::new));

        getOrCreateTagBuilder(ModTags.Blocks.CRAFTING_STUMPS)
                .add(ModBlocks.getAllCraftingStumps().toArray(Block[]::new));

        getOrCreateTagBuilder(ModTags.Blocks.DRYING_RACKS)
                .add(ModBlocks.getAllDryingRacks().toArray(Block[]::new));

        getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
                .addTag(ModTags.Blocks.ROCK_SHARPENING_STATIONS)
                .addTag(ModTags.Blocks.STUMPS)
                .addTag(ModTags.Blocks.CRAFTING_STUMPS)
                .addTag(ModTags.Blocks.DRYING_RACKS);


    }
}
