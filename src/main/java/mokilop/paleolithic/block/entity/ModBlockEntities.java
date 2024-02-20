package mokilop.paleolithic.block.entity;

import mokilop.paleolithic.Paleolithic;
import mokilop.paleolithic.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static BlockEntityType<CraftingStumpBlockEntity> CRAFTING_STUMP;
    public static BlockEntityType<DryingRackBlockEntity> DRYING_RACK;
    public static BlockEntityType<PrimitiveCampfireBlockEntity> PRIMITIVE_CAMPFIRE;
    public static BlockEntityType<StumpBlockEntity> STUMP;
    public static BlockEntityType<SharpeningStumpBlockEntity> SHARPENING_STUMP;
    public static BlockEntityType<HandMillBlockEntity> HAND_MILL;

    public static void registerBlocksEntities() {
        CRAFTING_STUMP = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(Paleolithic.MOD_ID, "crafting_stump"),
                FabricBlockEntityTypeBuilder.create(CraftingStumpBlockEntity::new,
                        ModBlocks.getAllCraftingStumps().toArray(Block[]::new)).build(null));

        PRIMITIVE_CAMPFIRE = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(Paleolithic.MOD_ID, "primitive_campfire"),
                FabricBlockEntityTypeBuilder.create(PrimitiveCampfireBlockEntity::new,
                        ModBlocks.PRIMITIVE_CAMPFIRE).build(null));

        STUMP = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(Paleolithic.MOD_ID, "stump"),
                FabricBlockEntityTypeBuilder.create(StumpBlockEntity::new,
                        ModBlocks.getAllStumps().toArray(Block[]::new)).build(null));

        DRYING_RACK = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(Paleolithic.MOD_ID, "drying_rack"),
                FabricBlockEntityTypeBuilder.create(DryingRackBlockEntity::new,
                        ModBlocks.getAllDryingRacks().toArray(Block[]::new)).build(null));

        SHARPENING_STUMP = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(Paleolithic.MOD_ID, "sharpening_stump"),
                FabricBlockEntityTypeBuilder.create(SharpeningStumpBlockEntity::new,
                        ModBlocks.getAllSharpeningStumps().toArray(Block[]::new)).build(null));

        HAND_MILL = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(Paleolithic.MOD_ID, "grindstone"),
                FabricBlockEntityTypeBuilder.create(HandMillBlockEntity::new, ModBlocks.HAND_MILL).build(null));
    }
}
