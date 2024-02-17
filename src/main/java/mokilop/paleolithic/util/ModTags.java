package mokilop.paleolithic.util;

import mokilop.paleolithic.Paleolithic;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> STUMPS = createBlockTag("stumps");
        public static final TagKey<Block> CRAFTING_STUMPS = createBlockTag("crafting_stumps");
        public static final TagKey<Block> SHARPENING_STUMPS = createBlockTag("rock_sharpening_stations");
        public static final TagKey<Block> DRYING_RACKS = createBlockTag("drying_racks");
        public static final TagKey<Block> HAMMER_MINEABLE = createBlockTag("hammer_mineable");
        public static final TagKey<Block> NOT_HAND_MINEABLE = createBlockTag("not_hand_mineable");

        private static TagKey<Block> createBlockTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, new Identifier(Paleolithic.MOD_ID, name));
        }

        private static TagKey<Block> createCommonBlockTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, new Identifier("c", name));
        }

    }

    public static class Items {
        public static final TagKey<Item> STUMPS = createItemTag("stumps");
        public static final TagKey<Item> CRAFTING_STUMPS = createItemTag("crafting_stumps");
        public static final TagKey<Item> ROCK_SHARPENING_STATIONS = createItemTag("rock_sharpening_stations");
        public static final TagKey<Item> DRYING_RACKS = createItemTag("drying_racks");
        public static final TagKey<Item> OFFHAND_EQUIPMENT = createItemTag("offhand_equipment");
        public static final TagKey<Item> HAMMERS = createItemTag("hammers");

        private static TagKey<Item> createItemTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, new Identifier(Paleolithic.MOD_ID, name));
        }

        private static TagKey<Item> createCommonItemTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, new Identifier("c", name));
        }
    }
}
