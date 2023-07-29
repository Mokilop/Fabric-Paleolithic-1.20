package mokilop.paleolithic.data;

import mokilop.paleolithic.block.ModBlocks;
import mokilop.paleolithic.block.custom.CraftingStumpBlock;
import mokilop.paleolithic.block.custom.RockSharpeningStationBlock;
import mokilop.paleolithic.block.custom.StumpBlock;
import mokilop.paleolithic.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;

public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        registerStumps(blockStateModelGenerator);
        registerRockSharpeningStations(blockStateModelGenerator);
        registerCraftingStumps(blockStateModelGenerator);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.STONE_HATCHET, Models.HANDHELD);
        itemModelGenerator.register(ModItems.STONE_HAMMER, Models.HANDHELD);
        itemModelGenerator.register(ModItems.PLANT_TWINE, Models.GENERATED);
        itemModelGenerator.register(ModItems.PLANT_FIBER, Models.GENERATED);

    }

    private void registerStump(BlockStateModelGenerator blockStateModelGenerator, StumpBlock stump) {
        blockStateModelGenerator.blockStateCollector
                .accept(BlockStateModelGenerator.createSingletonBlockState(stump, StumpBlock.PARENT_MODEL
                        .upload(stump, stump.getTextureMap(), blockStateModelGenerator.modelCollector)));
    }

    private void registerRockSharpeningStation(BlockStateModelGenerator blockStateModelGenerator, RockSharpeningStationBlock stationBlock) {
        blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier
                .create(stationBlock, BlockStateVariant.create()
                        .put(VariantSettings.MODEL, RockSharpeningStationBlock.PARENT_MODEL
                                .upload(stationBlock, stationBlock.getTextureMap(),
                                        blockStateModelGenerator.modelCollector)))
                .coordinate(BlockStateModelGenerator
                        .createNorthDefaultHorizontalRotationStates()));
    }

    private void registerCraftingStump(BlockStateModelGenerator blockStateModelGenerator, CraftingStumpBlock craftingStump) {
        blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier
                .create(craftingStump, BlockStateVariant.create()
                        .put(VariantSettings.MODEL, CraftingStumpBlock.PARENT_MODEL
                                .upload(craftingStump, craftingStump.getTextureMap(),
                                        blockStateModelGenerator.modelCollector)))
                .coordinate(BlockStateModelGenerator
                        .createNorthDefaultHorizontalRotationStates()));
    }

    private void registerRockSharpeningStations(BlockStateModelGenerator blockStateModelGenerator) {
        ModBlocks.getAllRockSharpeningStations().forEach((s) -> registerRockSharpeningStation(blockStateModelGenerator, (RockSharpeningStationBlock) s));
    }

    private void registerStumps(BlockStateModelGenerator blockStateModelGenerator) {
        ModBlocks.getAllStumps().forEach((s) -> registerStump(blockStateModelGenerator, ((StumpBlock) s)));
    }

    private void registerCraftingStumps(BlockStateModelGenerator blockStateModelGenerator) {
        ModBlocks.getAllCraftingStumps().forEach((s) -> registerCraftingStump(blockStateModelGenerator, ((CraftingStumpBlock) s)));
    }


}
