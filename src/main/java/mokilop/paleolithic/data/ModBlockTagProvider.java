package mokilop.paleolithic.data;

import mokilop.paleolithic.block.ModBlocks;
import mokilop.paleolithic.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(ModTags.Blocks.SHARPENING_STUMPS)
                .add(ModBlocks.getAllSharpeningStumps().toArray(Block[]::new));

        getOrCreateTagBuilder(ModTags.Blocks.STUMPS)
                .add(ModBlocks.getAllStumps().toArray(Block[]::new));

        getOrCreateTagBuilder(ModTags.Blocks.CRAFTING_STUMPS)
                .add(ModBlocks.getAllCraftingStumps().toArray(Block[]::new));

        getOrCreateTagBuilder(ModTags.Blocks.DRYING_RACKS)
                .add(ModBlocks.getAllDryingRacks().toArray(Block[]::new));

        getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
                .addTag(ModTags.Blocks.SHARPENING_STUMPS)
                .addTag(ModTags.Blocks.STUMPS)
                .addTag(ModTags.Blocks.CRAFTING_STUMPS)
                .addTag(ModTags.Blocks.DRYING_RACKS);

        getOrCreateTagBuilder(ConventionalBlockTags.ORES);
        getOrCreateTagBuilder(BlockTags.BASE_STONE_NETHER);
        getOrCreateTagBuilder(BlockTags.BASE_STONE_OVERWORLD);
        getOrCreateTagBuilder(BlockTags.LOGS);
        getOrCreateTagBuilder(ConventionalBlockTags.SANDSTONE_BLOCKS);

        getOrCreateTagBuilder(ModTags.Blocks.NOT_HAND_MINEABLE)
                .addTag(ConventionalBlockTags.ORES)
                .addTag(BlockTags.BASE_STONE_NETHER)
                .addTag(BlockTags.BASE_STONE_OVERWORLD)
                .addTag(BlockTags.LOGS)
                .addTag(ConventionalBlockTags.SANDSTONE_BLOCKS)
                .add(Blocks.OBSIDIAN);

    }
}
