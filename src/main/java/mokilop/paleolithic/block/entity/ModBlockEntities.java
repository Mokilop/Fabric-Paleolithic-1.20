package mokilop.paleolithic.block.entity;

import mokilop.paleolithic.Paleolithic;
import mokilop.paleolithic.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static BlockEntityType<PrimitiveCraftingBenchBlockEntity> PRIMITIVE_CRAFTING_BENCH;
    public static BlockEntityType<PrimitiveCampfireBlockEntity> PRIMITIVE_CAMPFIRE;
    public static BlockEntityType<StumpBlockEntity> STUMP;

    public static void registerBlocksEntities() {
        PRIMITIVE_CRAFTING_BENCH = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(Paleolithic.MOD_ID, "primitive_oak_crafting_bench"),
                FabricBlockEntityTypeBuilder.create(PrimitiveCraftingBenchBlockEntity::new,
                        ModBlocks.PRIMITIVE_OAK_CRAFTING_BENCH).build(null));
        PRIMITIVE_CAMPFIRE = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(Paleolithic.MOD_ID, "primitive_campfire"),
                FabricBlockEntityTypeBuilder.create(PrimitiveCampfireBlockEntity::new,
                        ModBlocks.PRIMITIVE_CAMPFIRE).build(null));
        STUMP = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(Paleolithic.MOD_ID, "stump"),
                FabricBlockEntityTypeBuilder.create(StumpBlockEntity::new,
                        ModBlocks.BAMBOO_STUMP,
                        ModBlocks.ACACIA_STUMP,
                        ModBlocks.BIRCH_STUMP,
                        ModBlocks.CHERRY_STUMP,
                        ModBlocks.DARK_OAK_STUMP,
                        ModBlocks.JUNGLE_STUMP,
                        ModBlocks.MANGROVE_STUMP,
                        ModBlocks.OAK_STUMP,
                        ModBlocks.SPRUCE_STUMP,
                        ModBlocks.STRIPPED_ACACIA_STUMP,
                        ModBlocks.STRIPPED_BAMBOO_STUMP,
                        ModBlocks.STRIPPED_BIRCH_STUMP,
                        ModBlocks.STRIPPED_CHERRY_STUMP,
                        ModBlocks.STRIPPED_DARK_OAK_STUMP,
                        ModBlocks.STRIPPED_JUNGLE_STUMP,
                        ModBlocks.STRIPPED_MANGROVE_STUMP,
                        ModBlocks.STRIPPED_OAK_STUMP,
                        ModBlocks.STRIPPED_SPRUCE_STUMP).build(null));

    }
}
