package mokilop.paleolithic.block;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import mokilop.paleolithic.Paleolithic;
import mokilop.paleolithic.block.custom.*;
import mokilop.paleolithic.data.Constants;
import mokilop.paleolithic.item.ModItemGroup;
import mokilop.paleolithic.item.custom.RockBlockItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.WoodType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.Set;
import java.util.stream.Stream;

public class ModBlocks {
    public static final Block ROCK = registerRockBlock();
    public static final Block PRIMITIVE_CAMPFIRE = registerPrimitiveCampfire();
    // region RockSharpeningStations
    private static final Set<Block> ROCK_SHARPENING_STATIONS = new ObjectArraySet<>();
    public static final Block OAK_ROCK_SHARPENING_STATION = registerRockSharpeningStation(WoodType.OAK, false);
    public static final Block SPRUCE_ROCK_SHARPENING_STATION = registerRockSharpeningStation(WoodType.SPRUCE, false);
    public static final Block BIRCH_ROCK_SHARPENING_STATION = registerRockSharpeningStation(WoodType.BIRCH, false);
    public static final Block ACACIA_ROCK_SHARPENING_STATION = registerRockSharpeningStation(WoodType.ACACIA, false);
    public static final Block CHERRY_ROCK_SHARPENING_STATION = registerRockSharpeningStation(WoodType.CHERRY, false);
    public static final Block JUNGLE_ROCK_SHARPENING_STATION = registerRockSharpeningStation(WoodType.JUNGLE, false);
    public static final Block DARK_OAK_ROCK_SHARPENING_STATION = registerRockSharpeningStation(WoodType.DARK_OAK, false);
    public static final Block CRIMSON_ROCK_SHARPENING_STATION = registerRockSharpeningStation(WoodType.CRIMSON, false);
    public static final Block WARPED_ROCK_SHARPENING_STATION = registerRockSharpeningStation(WoodType.WARPED, false);
    public static final Block MANGROVE_ROCK_SHARPENING_STATION = registerRockSharpeningStation(WoodType.MANGROVE, false);
    public static final Block BAMBOO_ROCK_SHARPENING_STATION = registerRockSharpeningStation(WoodType.BAMBOO, false);
    public static final Block STRIPPED_OAK_ROCK_SHARPENING_STATION = registerRockSharpeningStation(WoodType.OAK, true);
    public static final Block STRIPPED_SPRUCE_ROCK_SHARPENING_STATION = registerRockSharpeningStation(WoodType.SPRUCE, true);
    public static final Block STRIPPED_BIRCH_ROCK_SHARPENING_STATION = registerRockSharpeningStation(WoodType.BIRCH, true);
    public static final Block STRIPPED_ACACIA_ROCK_SHARPENING_STATION = registerRockSharpeningStation(WoodType.ACACIA, true);
    public static final Block STRIPPED_CHERRY_ROCK_SHARPENING_STATION = registerRockSharpeningStation(WoodType.CHERRY, true);
    public static final Block STRIPPED_JUNGLE_ROCK_SHARPENING_STATION = registerRockSharpeningStation(WoodType.JUNGLE, true);
    public static final Block STRIPPED_DARK_OAK_ROCK_SHARPENING_STATION = registerRockSharpeningStation(WoodType.DARK_OAK, true);
    public static final Block STRIPPED_CRIMSON_ROCK_SHARPENING_STATION = registerRockSharpeningStation(WoodType.CRIMSON, true);
    public static final Block STRIPPED_WARPED_ROCK_SHARPENING_STATION = registerRockSharpeningStation(WoodType.WARPED, true);
    public static final Block STRIPPED_MANGROVE_ROCK_SHARPENING_STATION = registerRockSharpeningStation(WoodType.MANGROVE, true);
    public static final Block STRIPPED_BAMBOO_ROCK_SHARPENING_STATION = registerRockSharpeningStation(WoodType.BAMBOO, true);
    //endregion

    //region Stumps
    private static final Set<Block> STUMPS = new ObjectArraySet<>();
    public static final Block OAK_STUMP = registerStump(WoodType.OAK, false);
    public static final Block SPRUCE_STUMP = registerStump(WoodType.SPRUCE, false);
    public static final Block BIRCH_STUMP = registerStump(WoodType.BIRCH, false);
    public static final Block ACACIA_STUMP = registerStump(WoodType.ACACIA, false);
    public static final Block CHERRY_STUMP = registerStump(WoodType.CHERRY, false);
    public static final Block JUNGLE_STUMP = registerStump(WoodType.JUNGLE, false);
    public static final Block DARK_OAK_STUMP = registerStump(WoodType.DARK_OAK, false);
    public static final Block CRIMSON_STUMP = registerStump(WoodType.CRIMSON, false);
    public static final Block WARPED_STUMP = registerStump(WoodType.WARPED, false);
    public static final Block MANGROVE_STUMP = registerStump(WoodType.MANGROVE, false);
    public static final Block BAMBOO_STUMP = registerStump(WoodType.BAMBOO, false);
    public static final Block STRIPPED_OAK_STUMP = registerStump(WoodType.OAK, true);
    public static final Block STRIPPED_SPRUCE_STUMP = registerStump(WoodType.SPRUCE, true);
    public static final Block STRIPPED_BIRCH_STUMP = registerStump(WoodType.BIRCH, true);
    public static final Block STRIPPED_ACACIA_STUMP = registerStump(WoodType.ACACIA, true);
    public static final Block STRIPPED_CHERRY_STUMP = registerStump(WoodType.CHERRY, true);
    public static final Block STRIPPED_JUNGLE_STUMP = registerStump(WoodType.JUNGLE, true);
    public static final Block STRIPPED_DARK_OAK_STUMP = registerStump(WoodType.DARK_OAK, true);
    public static final Block STRIPPED_CRIMSON_STUMP = registerStump(WoodType.CRIMSON, true);
    public static final Block STRIPPED_WARPED_STUMP = registerStump(WoodType.WARPED, true);
    public static final Block STRIPPED_MANGROVE_STUMP = registerStump(WoodType.MANGROVE, true);
    public static final Block STRIPPED_BAMBOO_STUMP = registerStump(WoodType.BAMBOO, true);
    //endregion

    //region CraftingStumps
    private static final Set<Block> CRAFTING_STUMPS = new ObjectArraySet<>();
    public static final Block OAK_CRAFTING_STUMP = registerCraftingStump(WoodType.OAK, false);
    public static final Block SPRUCE_CRAFTING_STUMP = registerCraftingStump(WoodType.SPRUCE, false);
    public static final Block BIRCH_CRAFTING_STUMP = registerCraftingStump(WoodType.BIRCH, false);
    public static final Block ACACIA_CRAFTING_STUMP = registerCraftingStump(WoodType.ACACIA, false);
    public static final Block CHERRY_CRAFTING_STUMP = registerCraftingStump(WoodType.CHERRY, false);
    public static final Block JUNGLE_CRAFTING_STUMP = registerCraftingStump(WoodType.JUNGLE, false);
    public static final Block DARK_OAK_CRAFTING_STUMP = registerCraftingStump(WoodType.DARK_OAK, false);
    public static final Block CRIMSON_CRAFTING_STUMP = registerCraftingStump(WoodType.CRIMSON, false);
    public static final Block WARPED_CRAFTING_STUMP = registerCraftingStump(WoodType.WARPED, false);
    public static final Block MANGROVE_CRAFTING_STUMP = registerCraftingStump(WoodType.MANGROVE, false);
    public static final Block BAMBOO_CRAFTING_STUMP = registerCraftingStump(WoodType.BAMBOO, false);
    public static final Block STRIPPED_OAK_CRAFTING_STUMP = registerCraftingStump(WoodType.OAK, true);
    public static final Block STRIPPED_SPRUCE_CRAFTING_STUMP = registerCraftingStump(WoodType.SPRUCE, true);
    public static final Block STRIPPED_BIRCH_CRAFTING_STUMP = registerCraftingStump(WoodType.BIRCH, true);
    public static final Block STRIPPED_ACACIA_CRAFTING_STUMP = registerCraftingStump(WoodType.ACACIA, true);
    public static final Block STRIPPED_CHERRY_CRAFTING_STUMP = registerCraftingStump(WoodType.CHERRY, true);
    public static final Block STRIPPED_JUNGLE_CRAFTING_STUMP = registerCraftingStump(WoodType.JUNGLE, true);
    public static final Block STRIPPED_DARK_OAK_CRAFTING_STUMP = registerCraftingStump(WoodType.DARK_OAK, true);
    public static final Block STRIPPED_CRIMSON_CRAFTING_STUMP = registerCraftingStump(WoodType.CRIMSON, true);
    public static final Block STRIPPED_WARPED_CRAFTING_STUMP = registerCraftingStump(WoodType.WARPED, true);
    public static final Block STRIPPED_MANGROVE_CRAFTING_STUMP = registerCraftingStump(WoodType.MANGROVE, true);
    public static final Block STRIPPED_BAMBOO_CRAFTING_STUMP = registerCraftingStump(WoodType.BAMBOO, true);
    // endregion

    //region DryingRacks
    private static final Set<Block> DRYING_RACKS = new ObjectArraySet<>();
    public static final Block OAK_DRYING_RACK = registerDryingRack(WoodType.OAK);

    //endregion

    private static Block registerRockSharpeningStation(WoodType type, boolean isStripped) {
        String name = (isStripped ? "stripped_" : "") + type.name() + "_rock_sharpening_station";
        Block L = Constants.LOGS_MAP.get(type);
        FabricBlockSettings settings = FabricBlockSettings.create()
                .strength(L.getHardness(), L.getBlastResistance());
        Block sharpeningStation = registerBlock(name, new RockSharpeningStationBlock(settings, type, isStripped), ModItemGroup.PALEOLITHIC_BLOCKS);
        ROCK_SHARPENING_STATIONS.add(sharpeningStation);
        return sharpeningStation;
    }

    public static Stream<Block> getAllRockSharpeningStations() {
        return ROCK_SHARPENING_STATIONS.stream();
    }

    private static Block registerStump(WoodType type, boolean isStripped) {
        String name = (isStripped ? "stripped_" : "") + type.name() + "_stump";
        Block L = Constants.LOGS_MAP.get(type);
        FabricBlockSettings settings = FabricBlockSettings.create()
                .strength(L.getHardness(), L.getBlastResistance());
        Block stump = registerBlock(name, new StumpBlock(settings, type, isStripped), ModItemGroup.PALEOLITHIC_BLOCKS);
        STUMPS.add(stump);
        return stump;
    }

    public static Stream<Block> getAllStumps() {
        return STUMPS.stream();
    }

    private static Block registerCraftingStump(WoodType type, boolean isStripped) {
        String name = (isStripped ? "stripped_" : "") + type.name() + "_crafting_stump";
        Block L = Constants.LOGS_MAP.get(type);
        FabricBlockSettings settings = FabricBlockSettings.create()
                .strength(L.getHardness(), L.getBlastResistance());
        Block crafting_stump = registerBlock(name, new CraftingStumpBlock(settings, type, isStripped), ModItemGroup.PALEOLITHIC_BLOCKS);
        CRAFTING_STUMPS.add(crafting_stump);
        return crafting_stump;
    }

    public static Stream<Block> getAllCraftingStumps() {
        return CRAFTING_STUMPS.stream();
    }
    private static Block registerDryingRack(WoodType type){
        String name = type.name() + "_drying_rack";
        Block p = Constants.PLANKS_MAP.get(type);
        FabricBlockSettings settings = FabricBlockSettings.create()
                .strength(p.getHardness(), p.getBlastResistance())
                .sounds(type.soundType());
        Block rack = registerBlock(name, new DryingRackBlock(settings, type), ModItemGroup.PALEOLITHIC_BLOCKS);
        DRYING_RACKS.add(rack);
        return rack;
    }
    public static Stream<Block> getAllDryingRacks(){ return DRYING_RACKS.stream(); }

    private static Block registerBlock(String name, Block block, RegistryKey<ItemGroup> group) {
        registerBlockItem(name, block, group);
        return Registry.register(Registries.BLOCK, new Identifier(Paleolithic.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, RegistryKey<ItemGroup> group) {
        Item item = Registry.register(Registries.ITEM, new Identifier(Paleolithic.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
        ItemGroupEvents.modifyEntriesEvent(group).register(entries -> entries.add(item));
        return item;
    }

    private static Block registerPrimitiveCampfire() {
        return registerBlock("primitive_campfire",
                new PrimitiveCampfireBlock(FabricBlockSettings.create()
                        .nonOpaque().sounds(BlockSoundGroup.STONE)
                        .strength(0.2f, 0.2f)
                        .luminance(state -> state.get(PrimitiveCampfireBlock.LIT) ? state.get(PrimitiveCampfireBlock.FIRE_STRENGTH) * 3 + 2 : 0)),
                ModItemGroup.PALEOLITHIC_BLOCKS);
    }

    private static Block registerRockBlock() {
        String name = "rock";
        Block block = Registry.register(Registries.BLOCK, new Identifier(Paleolithic.MOD_ID, name), new RockBlock(
                FabricBlockSettings
                        .create()
                        .noBlockBreakParticles()
                        .breakInstantly()
                        .noCollision()
                        .pistonBehavior(PistonBehavior.DESTROY)
                        .sounds(BlockSoundGroup.STONE)));
        Item item = Registry.register(Registries.ITEM, new Identifier(Paleolithic.MOD_ID, name),
                new RockBlockItem(block, new FabricItemSettings()));
        ItemGroupEvents.modifyEntriesEvent(ModItemGroup.PALEOLITHIC_ITEMS).register(entries -> entries.add(item));
        return block;
    }

    public static void registerModBlocks() {
        Paleolithic.LOGGER.info("Registering ModBlocks for " + Paleolithic.MOD_ID);
    }
}
