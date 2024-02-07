package mokilop.paleolithic.util;

import mokilop.paleolithic.item.ModItems;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.BinomialLootNumberProvider;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.List;

public class ModLootTableModifiers {

    private static final List<Identifier> GRASS_IDs = List.of(
            Blocks.GRASS.getLootTableId(),Blocks.TALL_GRASS.getLootTableId(),
            Blocks.FERN.getLootTableId(),Blocks.LARGE_FERN.getLootTableId());
    //private static final List<Identifier> LEAVES_IDs = MixinCollections.LEAVES_BLOCKS.stream().map(AbstractBlock::getLootTableId).toList();
    public static void modifyLootTables() {
        LootTableEvents.REPLACE.register((resourceManager, lootManager, id, original, source) -> {
            if(source.isBuiltin() && GRASS_IDs.contains(id)){
                LootPool.Builder pool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(ModItems.PLANT_FIBER))
                        .apply(SetCountLootFunction.builder(BinomialLootNumberProvider.create(1, .3f)));
                return LootTable.builder().pool(pool).build();
            }
            return null;
        });
        /*LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (source.isBuiltin() && LEAVES_IDs.contains(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(Items.STICK))
                        .apply(SetCountLootFunction.builder(BinomialLootNumberProvider.create(2, .1f)));
                tableBuilder.pool(poolBuilder.build());
            }
        });*/
    }
}
