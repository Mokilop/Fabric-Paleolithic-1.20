package mokilop.paleolithic.data;

import mokilop.paleolithic.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class ModLootTableGenerator extends FabricBlockLootTableProvider {
    public ModLootTableGenerator(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.OAK_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.SPRUCE_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.ACACIA_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.BIRCH_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.CHERRY_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.DARK_OAK_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.JUNGLE_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.MANGROVE_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.STRIPPED_OAK_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.STRIPPED_SPRUCE_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.STRIPPED_ACACIA_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.STRIPPED_BIRCH_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.STRIPPED_CHERRY_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.STRIPPED_DARK_OAK_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.STRIPPED_JUNGLE_ROCK_SHARPENING_STATION );
        addDrop(ModBlocks.STRIPPED_MANGROVE_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.BAMBOO_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.STRIPPED_BAMBOO_SHARPENING_STATION);
        addDrop(ModBlocks.PRIMITIVE_OAK_CRAFTING_BENCH);
    }
}
