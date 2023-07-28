package mokilop.paleolithic.data;

import mokilop.paleolithic.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.Blocks;
import net.minecraft.block.WoodType;

import java.util.HashMap;

public class Constants {
    public static final Block[] STATION_TYPES = {
            ModBlocks.OAK_ROCK_SHARPENING_STATION,
            ModBlocks.SPRUCE_ROCK_SHARPENING_STATION,
            ModBlocks.ACACIA_ROCK_SHARPENING_STATION,
            ModBlocks.BIRCH_ROCK_SHARPENING_STATION,
            ModBlocks.CHERRY_ROCK_SHARPENING_STATION,
            ModBlocks.DARK_OAK_ROCK_SHARPENING_STATION,
            ModBlocks.JUNGLE_ROCK_SHARPENING_STATION,
            ModBlocks.MANGROVE_ROCK_SHARPENING_STATION,
            ModBlocks.STRIPPED_OAK_ROCK_SHARPENING_STATION,
            ModBlocks.STRIPPED_SPRUCE_ROCK_SHARPENING_STATION,
            ModBlocks.STRIPPED_ACACIA_ROCK_SHARPENING_STATION,
            ModBlocks.STRIPPED_BIRCH_ROCK_SHARPENING_STATION,
            ModBlocks.STRIPPED_CHERRY_ROCK_SHARPENING_STATION,
            ModBlocks.STRIPPED_DARK_OAK_ROCK_SHARPENING_STATION,
            ModBlocks.STRIPPED_JUNGLE_ROCK_SHARPENING_STATION,
            ModBlocks.STRIPPED_MANGROVE_ROCK_SHARPENING_STATION,
            ModBlocks.BAMBOO_ROCK_SHARPENING_STATION,
            ModBlocks.STRIPPED_BAMBOO_SHARPENING_STATION
    };
    public static final Block[] STUMP_TYPES = {
            ModBlocks.OAK_STUMP,
            ModBlocks.SPRUCE_STUMP,
            ModBlocks.ACACIA_STUMP,
            ModBlocks.BIRCH_STUMP,
            ModBlocks.CHERRY_STUMP,
            ModBlocks.DARK_OAK_STUMP,
            ModBlocks.JUNGLE_STUMP,
            ModBlocks.MANGROVE_STUMP,
            ModBlocks.STRIPPED_OAK_STUMP,
            ModBlocks.STRIPPED_SPRUCE_STUMP,
            ModBlocks.STRIPPED_ACACIA_STUMP,
            ModBlocks.STRIPPED_BIRCH_STUMP,
            ModBlocks.STRIPPED_CHERRY_STUMP,
            ModBlocks.STRIPPED_DARK_OAK_STUMP,
            ModBlocks.STRIPPED_JUNGLE_STUMP,
            ModBlocks.STRIPPED_MANGROVE_STUMP,
            ModBlocks.BAMBOO_STUMP,
            ModBlocks.STRIPPED_BAMBOO_STUMP
    };

    public static HashMap<WoodType, Block> LOGS_MAP = createLogsMap();
    public static HashMap<WoodType, Block> STRIPPED_LOGS_MAP = createStrippedLogsMap();
    public static HashMap<WoodType, Block> PLANKS_MAP = createPlanksMap();

    private static HashMap<WoodType, Block> createLogsMap(){
        HashMap<WoodType, Block> map = new HashMap<>();
        map.put(WoodType.OAK, Blocks.OAK_LOG);
        map.put(WoodType.SPRUCE, Blocks.SPRUCE_LOG);
        map.put(WoodType.BIRCH, Blocks.BIRCH_LOG);
        map.put(WoodType.ACACIA, Blocks.ACACIA_LOG);
        map.put(WoodType.CHERRY, Blocks.CHERRY_LOG);
        map.put(WoodType.JUNGLE, Blocks.JUNGLE_LOG);
        map.put(WoodType.DARK_OAK, Blocks.DARK_OAK_LOG);
        map.put(WoodType.CRIMSON, Blocks.CRIMSON_STEM);
        map.put(WoodType.WARPED, Blocks.WARPED_STEM);
        map.put(WoodType.MANGROVE, Blocks.MANGROVE_LOG);
        map.put(WoodType.BAMBOO, Blocks.BAMBOO_BLOCK);
        return map;
    }
    private static HashMap<WoodType, Block> createStrippedLogsMap(){
        HashMap<WoodType, Block> map = new HashMap<>();
        map.put(WoodType.OAK, Blocks.STRIPPED_OAK_LOG);
        map.put(WoodType.SPRUCE, Blocks.STRIPPED_SPRUCE_LOG);
        map.put(WoodType.BIRCH, Blocks.STRIPPED_BIRCH_LOG);
        map.put(WoodType.ACACIA, Blocks.STRIPPED_ACACIA_LOG);
        map.put(WoodType.CHERRY, Blocks.STRIPPED_CHERRY_LOG);
        map.put(WoodType.JUNGLE, Blocks.STRIPPED_JUNGLE_LOG);
        map.put(WoodType.DARK_OAK, Blocks.STRIPPED_DARK_OAK_LOG);
        map.put(WoodType.CRIMSON, Blocks.STRIPPED_CRIMSON_STEM);
        map.put(WoodType.WARPED, Blocks.STRIPPED_WARPED_STEM);
        map.put(WoodType.MANGROVE, Blocks.STRIPPED_MANGROVE_LOG);
        map.put(WoodType.BAMBOO, Blocks.STRIPPED_BAMBOO_BLOCK);
        return map;
    }
    private static HashMap<WoodType, Block> createPlanksMap(){
        HashMap<WoodType, Block> map = new HashMap<>();
        map.put(WoodType.OAK, Blocks.OAK_PLANKS);
        map.put(WoodType.SPRUCE, Blocks.SPRUCE_PLANKS);
        map.put(WoodType.BIRCH, Blocks.BIRCH_PLANKS);
        map.put(WoodType.ACACIA, Blocks.ACACIA_PLANKS);
        map.put(WoodType.CHERRY, Blocks.CHERRY_PLANKS);
        map.put(WoodType.JUNGLE, Blocks.JUNGLE_PLANKS);
        map.put(WoodType.DARK_OAK, Blocks.DARK_OAK_PLANKS);
        map.put(WoodType.CRIMSON, Blocks.CRIMSON_PLANKS);
        map.put(WoodType.WARPED, Blocks.WARPED_PLANKS);
        map.put(WoodType.MANGROVE, Blocks.MANGROVE_PLANKS);
        map.put(WoodType.BAMBOO, Blocks.BAMBOO_PLANKS);
        return map;
    }
    /*private static HashMap<WoodType, Block> createStumpsMap(){
        HashMap<WoodType, Block> map = new HashMap<>();
        map.put(WoodType.OAK, ModBlocks.OAK_STUMP);
        map.put(WoodType.SPRUCE, ModBlocks.SPRUCE_STUMP);
        map.put(WoodType.BIRCH, ModBlocks.BIRCH_STUMP);
        map.put(WoodType.ACACIA, ModBlocks.ACACIA_STUMP);
        map.put(WoodType.CHERRY, ModBlocks.CHERRY_STUMP);
        map.put(WoodType.JUNGLE, ModBlocks.JUNGLE_STUMP);
        map.put(WoodType.DARK_OAK, ModBlocks.DARK_OAK_STUMP);
        map.put(WoodType.CRIMSON, ModBlocks.CRIMSON_STUMP);
        map.put(WoodType.WARPED, ModBlocks.WARPED_STUMP);
        map.put(WoodType.MANGROVE, ModBlocks.MANGROVE_STUMP);
        map.put(WoodType.BAMBOO, ModBlocks.BAMBOO_STUMP);
        return map;
    }
    private static HashMap<WoodType, Block> createStrippedStumpsMap(){
        HashMap<WoodType, Block> map = new HashMap<>();
        map.put(WoodType.OAK, ModBlocks.STRIPPED_OAK_STUMP);
        map.put(WoodType.SPRUCE, ModBlocks.STRIPPED_SPRUCE_STUMP);
        map.put(WoodType.BIRCH, ModBlocks.STRIPPED_BIRCH_STUMP);
        map.put(WoodType.ACACIA, ModBlocks.STRIPPED_ACACIA_STUMP);
        map.put(WoodType.CHERRY, ModBlocks.STRIPPED_CHERRY_STUMP);
        map.put(WoodType.JUNGLE, ModBlocks.STRIPPED_JUNGLE_STUMP);
        map.put(WoodType.DARK_OAK, ModBlocks.STRIPPED_DARK_OAK_STUMP);
        map.put(WoodType.CRIMSON, ModBlocks.STRIPPED_CRIMSON_STUMP);
        map.put(WoodType.WARPED, ModBlocks.STRIPPED_WARPED_STUMP);
        map.put(WoodType.MANGROVE, ModBlocks.STRIPPED_MANGROVE_STUMP);
        map.put(WoodType.BAMBOO, ModBlocks.STRIPPED_BAMBOO_STUMP);
        return map;
    }*/


}
