package mokilop.paleolithic.data;

import mokilop.paleolithic.block.ModBlocks;
import mokilop.paleolithic.block.custom.RockBlock;
import mokilop.paleolithic.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerbedBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.data.server.loottable.BlockLootTableGenerator;
import net.minecraft.data.server.loottable.vanilla.VanillaBlockLootTableGenerator;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.*;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionConsumingBuilder;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.BinomialLootNumberProvider;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.item.EnchantmentPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.util.math.BlockPos;

import java.util.stream.IntStream;

public class ModLootTableGenerator extends FabricBlockLootTableProvider {
    protected static final LootCondition.Builder WITH_HAMMER = MatchToolLootCondition.builder(ItemPredicate.Builder.create().items(ModItems.STONE_HAMMER));

    public ModLootTableGenerator(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    public LootTable.Builder alternativeDropsWithHammer(Block normalDrop, ItemConvertible withHammer) {

        return BlockLootTableGenerator.drops(normalDrop,
                WITH_HAMMER.invert(),
                this.addSurvivesExplosionCondition(normalDrop,
                        ItemEntry.builder(withHammer)
                                .conditionally(TableBonusLootCondition
                                        .builder(Enchantments.FORTUNE, 0.1f, 0.15f, 0.25f, 1.0f))
                                .alternatively(ItemEntry.builder(normalDrop))));
    }

    private LootFunction.Builder rocksAmountFunc(int rocksAmount) {
        return SetCountLootFunction.builder(ConstantLootNumberProvider.create(rocksAmount))
                .conditionally(BlockStatePropertyLootCondition.builder(ModBlocks.ROCK).properties(StatePredicate.Builder.create()
                        .exactMatch(RockBlock.STONES, rocksAmount)));
    }

    public LootTable.Builder rockBlockDrops() {
        final var defaultEntry = ItemEntry.builder(ModBlocks.ROCK).apply(IntStream.rangeClosed(1, 3).boxed().toList(), this::rocksAmountFunc);
        return LootTable.builder().pool(LootPool.builder().with(defaultEntry));
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.ROCK, rockBlockDrops());
        addDrop(ModBlocks.PRIMITIVE_CAMPFIRE);
        ModBlocks.getAllStumps().forEach(this::addDrop);
        ModBlocks.getAllSharpeningStumps().forEach(this::addDrop);
        ModBlocks.getAllCraftingStumps().forEach(this::addDrop);
        ModBlocks.getAllDryingRacks().forEach(this::addDrop);
    }
}