package mokilop.paleolithic.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mokilop.paleolithic.Paleolithic;
import mokilop.paleolithic.block.ModBlocks;
import mokilop.paleolithic.block.custom.StumpBlock;
import mokilop.paleolithic.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.block.WoodType;
import net.minecraft.data.client.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;

public class ModModelProvider extends FabricModelProvider {

    private static final Model STUMP_MODEL = new Model(Optional.of(new Identifier(Paleolithic.MOD_ID, "stump")),
            Optional.of("oak"), TextureKey.TEXTURE);

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        registerStumps(blockStateModelGenerator);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.STONE_HATCHET, Models.HANDHELD);
        itemModelGenerator.register(ModItems.STONE_HAMMER, Models.HANDHELD);
        itemModelGenerator.register(ModItems.PLANT_TWINE, Models.GENERATED);
        itemModelGenerator.register(ModItems.PLANT_FIBER, Models.GENERATED);

    }

    private void registerStump(BlockStateModelGenerator blockStateModelGenerator, StumpBlock stump){
        blockStateModelGenerator.blockStateCollector
                .accept(BlockStateModelGenerator.createSingletonBlockState(stump, StumpBlock.PARENT_MODEL
                        .upload(stump, stump.textureMap, blockStateModelGenerator.modelCollector)));
    }

    private void registerStumps(BlockStateModelGenerator blockStateModelGenerator){
        ModBlocks.getAllStumps().forEach((s) -> registerStump(blockStateModelGenerator, ((StumpBlock) s)));
    }

}
