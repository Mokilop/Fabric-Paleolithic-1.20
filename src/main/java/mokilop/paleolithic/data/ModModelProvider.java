package mokilop.paleolithic.data;

import mokilop.paleolithic.block.ModBlocks;
import mokilop.paleolithic.block.custom.CraftingStumpBlock;
import mokilop.paleolithic.block.custom.DryingRackBlock;
import mokilop.paleolithic.block.custom.SharpeningStumpBlock;
import mokilop.paleolithic.block.custom.StumpBlock;
import mokilop.paleolithic.block.enums.ComplexAttachment;
import mokilop.paleolithic.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        registerStumps(blockStateModelGenerator);
        registerRockSharpeningStations(blockStateModelGenerator);
        registerCraftingStumps(blockStateModelGenerator);
        registerDryingRacks(blockStateModelGenerator);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.FLAKED_ROCK, Models.GENERATED);
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

    private void registerSharpeningStump(BlockStateModelGenerator blockStateModelGenerator, SharpeningStumpBlock stationBlock) {
        blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier
                .create(stationBlock, BlockStateVariant.create()
                        .put(VariantSettings.MODEL, SharpeningStumpBlock.PARENT_MODEL
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

    private void registerDryingRack(BlockStateModelGenerator blockStateModelGenerator, DryingRackBlock dryingRack) {
        Identifier floorId = DryingRackBlock.PARENT_MODEL
                .upload(dryingRack, dryingRack.getTextureMap(),
                        blockStateModelGenerator.modelCollector);
        Identifier floorCloseId = DryingRackBlock.PARENT_MODEL_CLOSE
                .upload(dryingRack, dryingRack.getTextureMap(),
                        blockStateModelGenerator.modelCollector);
        Identifier floorFarId = DryingRackBlock.PARENT_MODEL_FAR
                .upload(dryingRack, dryingRack.getTextureMap(),
                        blockStateModelGenerator.modelCollector);


        Identifier wallId = DryingRackBlock.PARENT_MODEL_ON_WALL
                .upload(dryingRack, dryingRack.getTextureMap(),
                        blockStateModelGenerator.modelCollector);
        Identifier wallLeftId = DryingRackBlock.PARENT_MODEL_ON_WALL_LEFT
                .upload(dryingRack, dryingRack.getTextureMap(),
                        blockStateModelGenerator.modelCollector);
        Identifier wallRightId = DryingRackBlock.PARENT_MODEL_ON_WALL_RIGHT
                .upload(dryingRack, dryingRack.getTextureMap(),
                        blockStateModelGenerator.modelCollector);
        Identifier wallUpId = DryingRackBlock.PARENT_MODEL_ON_WALL_UP
                .upload(dryingRack, dryingRack.getTextureMap(),
                        blockStateModelGenerator.modelCollector);


        Identifier ceilingId = DryingRackBlock.PARENT_MODEL_ON_CEILING
                .upload(dryingRack, dryingRack.getTextureMap(),
                        blockStateModelGenerator.modelCollector);
        Identifier ceilingCloseId = DryingRackBlock.PARENT_MODEL_ON_CEILING_CLOSE
                .upload(dryingRack, dryingRack.getTextureMap(),
                        blockStateModelGenerator.modelCollector);
        Identifier ceilingFarId = DryingRackBlock.PARENT_MODEL_ON_CEILING_FAR
                .upload(dryingRack, dryingRack.getTextureMap(),
                        blockStateModelGenerator.modelCollector);


        blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(dryingRack)
                .coordinate(BlockStateVariantMap.create(DryingRackBlock.ATTACHMENT)
                        .register(ComplexAttachment.FLOOR, BlockStateVariant.create()
                                .put(VariantSettings.MODEL, floorId))
                        .register(ComplexAttachment.FLOOR_CLOSE, BlockStateVariant.create()
                                .put(VariantSettings.MODEL, floorCloseId))
                        .register(ComplexAttachment.FLOOR_FAR, BlockStateVariant.create()
                                .put(VariantSettings.MODEL, floorFarId))
                        .register(ComplexAttachment.CEILING, BlockStateVariant.create()
                                .put(VariantSettings.MODEL, ceilingId))
                        .register(ComplexAttachment.CEILING_CLOSE, BlockStateVariant.create()
                                .put(VariantSettings.MODEL, ceilingCloseId))
                        .register(ComplexAttachment.CEILING_FAR, BlockStateVariant.create()
                                .put(VariantSettings.MODEL, ceilingFarId))
                        .register(ComplexAttachment.WALL, BlockStateVariant.create()
                                .put(VariantSettings.MODEL, wallId))
                        .register(ComplexAttachment.WALL_LEFT, BlockStateVariant.create()
                                .put(VariantSettings.MODEL, wallLeftId))
                        .register(ComplexAttachment.WALL_RIGHT, BlockStateVariant.create()
                                .put(VariantSettings.MODEL, wallRightId))
                        .register(ComplexAttachment.WALL_UP, BlockStateVariant.create()
                                .put(VariantSettings.MODEL, wallUpId)))
                .coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates()));

    }

    private void registerDryingRacks(BlockStateModelGenerator blockStateModelGenerator) {
        ModBlocks.getAllDryingRacks().forEach((dr) -> registerDryingRack(blockStateModelGenerator, (DryingRackBlock) dr));
    }

    private void registerRockSharpeningStations(BlockStateModelGenerator blockStateModelGenerator) {
        ModBlocks.getAllSharpeningStumps().forEach((s) -> registerSharpeningStump(blockStateModelGenerator, (SharpeningStumpBlock) s));
    }

    private void registerStumps(BlockStateModelGenerator blockStateModelGenerator) {
        ModBlocks.getAllStumps().forEach((s) -> registerStump(blockStateModelGenerator, ((StumpBlock) s)));
    }

    private void registerCraftingStumps(BlockStateModelGenerator blockStateModelGenerator) {
        ModBlocks.getAllCraftingStumps().forEach((s) -> registerCraftingStump(blockStateModelGenerator, ((CraftingStumpBlock) s)));
    }


}
