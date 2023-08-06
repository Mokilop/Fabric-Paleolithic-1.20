package mokilop.paleolithic.data;

import mokilop.paleolithic.block.ModBlocks;
import mokilop.paleolithic.block.custom.CraftingStumpBlock;
import mokilop.paleolithic.block.custom.DryingRackBlock;
import mokilop.paleolithic.block.custom.RockSharpeningStationBlock;
import mokilop.paleolithic.block.custom.StumpBlock;
import mokilop.paleolithic.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.Attachment;
import net.minecraft.data.client.*;
import net.minecraft.item.Items;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

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
    private void registerDryingRack(BlockStateModelGenerator blockStateModelGenerator, DryingRackBlock dryingRack){
        Identifier floorId = DryingRackBlock.PARENT_MODEL
                .upload(dryingRack, dryingRack.getTextureMap(),
                        blockStateModelGenerator.modelCollector);

        Identifier wallId = DryingRackBlock.PARENT_MODEL_ON_WALL
                .upload(dryingRack, dryingRack.getTextureMap(),
                        blockStateModelGenerator.modelCollector);

        Identifier ceilingId = DryingRackBlock.PARENT_MODEL_ON_CEILING
                .upload(dryingRack, dryingRack.getTextureMap(),
                        blockStateModelGenerator.modelCollector);

        blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(dryingRack).coordinate(BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, Properties.ATTACHMENT)

                .register(Direction.NORTH, Attachment.FLOOR, BlockStateVariant.create().put(VariantSettings.MODEL, floorId)).register(Direction.SOUTH, Attachment.FLOOR, BlockStateVariant.create().put(VariantSettings.MODEL, floorId).put(VariantSettings.Y, VariantSettings.Rotation.R180)).register(Direction.EAST, Attachment.FLOOR, BlockStateVariant.create().put(VariantSettings.MODEL, floorId).put(VariantSettings.Y, VariantSettings.Rotation.R90)).register(Direction.WEST, Attachment.FLOOR, BlockStateVariant.create().put(VariantSettings.MODEL, floorId).put(VariantSettings.Y, VariantSettings.Rotation.R270))

                .register(Direction.NORTH, Attachment.CEILING, BlockStateVariant.create().put(VariantSettings.MODEL, ceilingId)).register(Direction.SOUTH, Attachment.CEILING, BlockStateVariant.create().put(VariantSettings.MODEL, ceilingId).put(VariantSettings.Y, VariantSettings.Rotation.R180)).register(Direction.EAST, Attachment.CEILING, BlockStateVariant.create().put(VariantSettings.MODEL, ceilingId).put(VariantSettings.Y, VariantSettings.Rotation.R90)).register(Direction.WEST, Attachment.CEILING, BlockStateVariant.create().put(VariantSettings.MODEL, ceilingId).put(VariantSettings.Y, VariantSettings.Rotation.R270))

                .register(Direction.EAST, Attachment.SINGLE_WALL, BlockStateVariant.create()
                        .put(VariantSettings.MODEL, wallId)
                        .put(VariantSettings.Y, VariantSettings.Rotation.R270))
                .register(Direction.WEST, Attachment.SINGLE_WALL, BlockStateVariant.create()
                        .put(VariantSettings.MODEL, wallId)
                        .put(VariantSettings.Y, VariantSettings.Rotation.R90))
                .register(Direction.SOUTH, Attachment.SINGLE_WALL, BlockStateVariant.create()
                        .put(VariantSettings.MODEL, wallId))
                .register(Direction.NORTH, Attachment.SINGLE_WALL, BlockStateVariant.create()
                        .put(VariantSettings.MODEL, wallId)
                        .put(VariantSettings.Y, VariantSettings.Rotation.R180))

                .register(Direction.NORTH, Attachment.DOUBLE_WALL, BlockStateVariant.create().put(VariantSettings.MODEL, ceilingId).put(VariantSettings.Y, VariantSettings.Rotation.R90)).register(Direction.EAST, Attachment.DOUBLE_WALL, BlockStateVariant.create().put(VariantSettings.MODEL, ceilingId).put(VariantSettings.Y, VariantSettings.Rotation.R270)).register(Direction.WEST, Attachment.DOUBLE_WALL, BlockStateVariant.create().put(VariantSettings.MODEL, ceilingId)).register(Direction.SOUTH, Attachment.DOUBLE_WALL, BlockStateVariant.create().put(VariantSettings.MODEL, ceilingId).put(VariantSettings.Y, VariantSettings.Rotation.R180))));

    }

    private void registerDryingRacks(BlockStateModelGenerator blockStateModelGenerator){
        ModBlocks.getAllDryingRacks().forEach((dr) -> registerDryingRack(blockStateModelGenerator, (DryingRackBlock) dr));
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
