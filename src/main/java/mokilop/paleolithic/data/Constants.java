package mokilop.paleolithic.data;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.WoodType;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class Constants {
    public static final HashMap<WoodType, Block> LOGS_MAP = createLogsMap();
    public static final HashMap<WoodType, Block> STRIPPED_LOGS_MAP = createStrippedLogsMap();
    public static final HashMap<WoodType, Block> PLANKS_MAP = createPlanksMap();
    public static final HashMap<WoodType, Block> WOOD_MAP = createWoodMap();
    public static final HashMap<WoodType, Block> STRIPPED_WOOD_MAP = createStrippedWoodMap();
    public static final List<Block> LOGS_LIST = createLogsList();

    private static List<Block> createLogsList() {
        final ArrayList<Block> logs = new ArrayList<>();
        logs.addAll(LOGS_MAP.values());
        logs.addAll(STRIPPED_LOGS_MAP.values());
        logs.addAll(WOOD_MAP.values());
        logs.addAll(STRIPPED_WOOD_MAP.values());
        return logs;
    }

    private static HashMap<WoodType, Block> createLogsMap() {
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

    private static HashMap<WoodType, Block> createStrippedLogsMap() {
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

    private static HashMap<WoodType, Block> createPlanksMap() {
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

    private static HashMap<WoodType, Block> createWoodMap() {
        HashMap<WoodType, Block> map = new HashMap<>();
        map.put(WoodType.OAK, Blocks.OAK_WOOD);
        map.put(WoodType.SPRUCE, Blocks.SPRUCE_WOOD);
        map.put(WoodType.BIRCH, Blocks.BIRCH_WOOD);
        map.put(WoodType.ACACIA, Blocks.ACACIA_WOOD);
        map.put(WoodType.CHERRY, Blocks.CHERRY_WOOD);
        map.put(WoodType.JUNGLE, Blocks.JUNGLE_WOOD);
        map.put(WoodType.DARK_OAK, Blocks.DARK_OAK_WOOD);
        map.put(WoodType.CRIMSON, Blocks.CRIMSON_HYPHAE);
        map.put(WoodType.WARPED, Blocks.WARPED_HYPHAE);
        map.put(WoodType.MANGROVE, Blocks.MANGROVE_WOOD);
        return map;
    }

    private static HashMap<WoodType, Block> createStrippedWoodMap() {
        HashMap<WoodType, Block> map = new HashMap<>();
        map.put(WoodType.OAK, Blocks.STRIPPED_OAK_WOOD);
        map.put(WoodType.SPRUCE, Blocks.STRIPPED_SPRUCE_WOOD);
        map.put(WoodType.BIRCH, Blocks.STRIPPED_BIRCH_WOOD);
        map.put(WoodType.ACACIA, Blocks.STRIPPED_ACACIA_WOOD);
        map.put(WoodType.CHERRY, Blocks.STRIPPED_CHERRY_WOOD);
        map.put(WoodType.JUNGLE, Blocks.STRIPPED_JUNGLE_WOOD);
        map.put(WoodType.DARK_OAK, Blocks.STRIPPED_DARK_OAK_WOOD);
        map.put(WoodType.CRIMSON, Blocks.STRIPPED_CRIMSON_HYPHAE);
        map.put(WoodType.WARPED, Blocks.STRIPPED_WARPED_HYPHAE);
        map.put(WoodType.MANGROVE, Blocks.STRIPPED_MANGROVE_WOOD);
        return map;
    }

}
