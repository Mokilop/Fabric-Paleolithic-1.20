package mokilop.paleolithic.data;

import mokilop.paleolithic.block.ModBlocks;
import mokilop.paleolithic.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.STONE_HATCHET, Models.HANDHELD);
        itemModelGenerator.register(ModItems.STONE_HAMMER, Models.HANDHELD);
        itemModelGenerator.register(ModItems.PLANT_TWINE, Models.GENERATED);
        itemModelGenerator.register(ModItems.PLANT_FIBER, Models.GENERATED);
    }
}
