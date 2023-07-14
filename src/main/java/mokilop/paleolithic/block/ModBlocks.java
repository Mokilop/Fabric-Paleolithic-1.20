package mokilop.paleolithic.block;

import mokilop.paleolithic.Paleolithic;
import mokilop.paleolithic.block.custom.CraftingBenchBlock;
import mokilop.paleolithic.block.custom.RockBlock;
import mokilop.paleolithic.block.custom.RockSharpeningStationBlock;
import mokilop.paleolithic.item.ModItemGroup;
import mokilop.paleolithic.item.custom.RockBlockItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block ROCK = registerRockBlock("rock",
            new RockBlock(
                    FabricBlockSettings
                            .create()
                            .noBlockBreakParticles()
                            .breakInstantly()
                            .noCollision()
                            .pistonBehavior(PistonBehavior.DESTROY)
                            .sounds(BlockSoundGroup.STONE)
            ),
            ModItemGroup.PALEOLITHIC);

    //region Rock Sharpening Stations
    private static final FabricBlockSettings ROCK_SHARPENING_STATION_SETTINGS_BASE =
            FabricBlockSettings
                    .create().nonOpaque()
                    .strength(Blocks.OAK_LOG.getHardness(), Blocks.OAK_LOG.getBlastResistance())
                    .sounds(BlockSoundGroup.WOOD);
    public static final Block OAK_ROCK_SHARPENING_STATION = registerBlock("oak_rock_sharpening_station",
            new RockSharpeningStationBlock(ROCK_SHARPENING_STATION_SETTINGS_BASE), ModItemGroup.PALEOLITHIC);
    public static final Block SPRUCE_ROCK_SHARPENING_STATION = registerBlock("spruce_rock_sharpening_station",
            new RockSharpeningStationBlock(ROCK_SHARPENING_STATION_SETTINGS_BASE), ModItemGroup.PALEOLITHIC);
    public static final Block ACACIA_ROCK_SHARPENING_STATION = registerBlock("acacia_rock_sharpening_station",
            new RockSharpeningStationBlock(ROCK_SHARPENING_STATION_SETTINGS_BASE), ModItemGroup.PALEOLITHIC);
    public static final Block BIRCH_ROCK_SHARPENING_STATION = registerBlock("birch_rock_sharpening_station",
            new RockSharpeningStationBlock(ROCK_SHARPENING_STATION_SETTINGS_BASE), ModItemGroup.PALEOLITHIC);
    public static final Block CHERRY_ROCK_SHARPENING_STATION = registerBlock("cherry_rock_sharpening_station",
            new RockSharpeningStationBlock(ROCK_SHARPENING_STATION_SETTINGS_BASE.sounds(BlockSoundGroup.CHERRY_WOOD)), ModItemGroup.PALEOLITHIC);
    public static final Block DARK_OAK_ROCK_SHARPENING_STATION = registerBlock("dark_oak_rock_sharpening_station",
            new RockSharpeningStationBlock(ROCK_SHARPENING_STATION_SETTINGS_BASE), ModItemGroup.PALEOLITHIC);
    public static final Block JUNGLE_ROCK_SHARPENING_STATION = registerBlock("jungle_rock_sharpening_station",
            new RockSharpeningStationBlock(ROCK_SHARPENING_STATION_SETTINGS_BASE), ModItemGroup.PALEOLITHIC);
    public static final Block MANGROVE_ROCK_SHARPENING_STATION = registerBlock("mangrove_rock_sharpening_station",
            new RockSharpeningStationBlock(ROCK_SHARPENING_STATION_SETTINGS_BASE), ModItemGroup.PALEOLITHIC);
    public static final Block STRIPPED_OAK_ROCK_SHARPENING_STATION = registerBlock("stripped_oak_rock_sharpening_station",
            new RockSharpeningStationBlock(ROCK_SHARPENING_STATION_SETTINGS_BASE), ModItemGroup.PALEOLITHIC);
    public static final Block STRIPPED_SPRUCE_ROCK_SHARPENING_STATION = registerBlock("stripped_spruce_rock_sharpening_station",
            new RockSharpeningStationBlock(ROCK_SHARPENING_STATION_SETTINGS_BASE), ModItemGroup.PALEOLITHIC);
    public static final Block STRIPPED_ACACIA_ROCK_SHARPENING_STATION = registerBlock("stripped_acacia_rock_sharpening_station",
            new RockSharpeningStationBlock(ROCK_SHARPENING_STATION_SETTINGS_BASE), ModItemGroup.PALEOLITHIC);
    public static final Block STRIPPED_BIRCH_ROCK_SHARPENING_STATION = registerBlock("stripped_birch_rock_sharpening_station",
            new RockSharpeningStationBlock(ROCK_SHARPENING_STATION_SETTINGS_BASE), ModItemGroup.PALEOLITHIC);
    public static final Block STRIPPED_CHERRY_ROCK_SHARPENING_STATION = registerBlock("stripped_cherry_rock_sharpening_station",
            new RockSharpeningStationBlock(ROCK_SHARPENING_STATION_SETTINGS_BASE.sounds(BlockSoundGroup.CHERRY_WOOD)), ModItemGroup.PALEOLITHIC);
    public static final Block STRIPPED_DARK_OAK_ROCK_SHARPENING_STATION = registerBlock("stripped_dark_oak_rock_sharpening_station",
            new RockSharpeningStationBlock(ROCK_SHARPENING_STATION_SETTINGS_BASE), ModItemGroup.PALEOLITHIC);
    public static final Block STRIPPED_JUNGLE_ROCK_SHARPENING_STATION = registerBlock("stripped_jungle_rock_sharpening_station",
            new RockSharpeningStationBlock(ROCK_SHARPENING_STATION_SETTINGS_BASE), ModItemGroup.PALEOLITHIC);
    public static final Block STRIPPED_MANGROVE_ROCK_SHARPENING_STATION = registerBlock("stripped_mangrove_rock_sharpening_station",
            new RockSharpeningStationBlock(ROCK_SHARPENING_STATION_SETTINGS_BASE), ModItemGroup.PALEOLITHIC);
    public static final Block BAMBOO_ROCK_SHARPENING_STATION = registerBlock("bamboo_rock_sharpening_station",
            new RockSharpeningStationBlock(ROCK_SHARPENING_STATION_SETTINGS_BASE
                    .sounds(BlockSoundGroup.BAMBOO_WOOD)
                    .strength(Blocks.BAMBOO_BLOCK.getHardness(), Blocks.BAMBOO_BLOCK.getBlastResistance())), ModItemGroup.PALEOLITHIC);
    public static final Block STRIPPED_BAMBOO_SHARPENING_STATION =  registerBlock("stripped_bamboo_rock_sharpening_station",
            new RockSharpeningStationBlock(ROCK_SHARPENING_STATION_SETTINGS_BASE
                    .sounds(BlockSoundGroup.BAMBOO_WOOD)
                    .strength(Blocks.BAMBOO_BLOCK.getHardness(), Blocks.BAMBOO_BLOCK.getBlastResistance())), ModItemGroup.PALEOLITHIC);
    //endregion

    //region Primitive Crafting Stations

    public static final Block PRIMITIVE_OAK_CRAFTING_BENCH = registerBlock("primitive_oak_crafting_bench",
            new CraftingBenchBlock(ROCK_SHARPENING_STATION_SETTINGS_BASE), ModItemGroup.PALEOLITHIC);

    //endregion

    private  static Block registerBlock(String name, Block block, RegistryKey<ItemGroup> group){
        registerBlockItem(name, block, group);
        return Registry.register(Registries.BLOCK, new Identifier(Paleolithic.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, RegistryKey<ItemGroup> group){
        Item item = Registry.register(Registries.ITEM, new Identifier(Paleolithic.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
        ItemGroupEvents.modifyEntriesEvent(group).register(entries -> entries.add(item));
        return item;
    }

    private static Block registerRockBlock(String name, RockBlock block, RegistryKey<ItemGroup> group){
        Item item = Registry.register(Registries.ITEM, new Identifier(Paleolithic.MOD_ID, name),
                new RockBlockItem(block, new FabricItemSettings()));
        ItemGroupEvents.modifyEntriesEvent(group).register(entries -> entries.add(item));
        return Registry.register(Registries.BLOCK, new Identifier(Paleolithic.MOD_ID, name), block);
    }

    public static void registerModBlocks(){
        Paleolithic.LOGGER.info("Registering ModBlocks for " + Paleolithic.MOD_ID);
    }
}
