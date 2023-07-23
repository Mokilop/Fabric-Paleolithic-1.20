package mokilop.paleolithic.block.entity;

import mokilop.paleolithic.Paleolithic;
import mokilop.paleolithic.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static BlockEntityType<PrimitiveCraftingBenchBlockEntity> PRIMITIVE_OAK_CRAFTING_BENCH;
    public static BlockEntityType<PrimitiveCampfireBlockEntity> PRIMITIVE_CAMPFIRE;

    public static void registerBlocksEntities() {
        PRIMITIVE_OAK_CRAFTING_BENCH = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(Paleolithic.MOD_ID, "primitive_oak_crafting_bench"),
                FabricBlockEntityTypeBuilder.create(PrimitiveCraftingBenchBlockEntity::new,
                        ModBlocks.PRIMITIVE_OAK_CRAFTING_BENCH).build(null));
        PRIMITIVE_CAMPFIRE = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(Paleolithic.MOD_ID, "primitive_campfire"),
                FabricBlockEntityTypeBuilder.create(PrimitiveCampfireBlockEntity::new,
                        ModBlocks.PRIMITIVE_CAMPFIRE).build(null));

    }
}
