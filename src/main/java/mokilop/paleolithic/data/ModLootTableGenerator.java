package mokilop.paleolithic.data;

import mokilop.paleolithic.block.ModBlocks;
import mokilop.paleolithic.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.LocationCheckLootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.BinomialLootNumberProvider;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.util.math.BlockPos;

public class ModLootTableGenerator extends FabricBlockLootTableProvider {
    public ModLootTableGenerator(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.OAK_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.SPRUCE_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.ACACIA_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.BIRCH_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.CHERRY_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.DARK_OAK_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.JUNGLE_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.MANGROVE_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.STRIPPED_OAK_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.STRIPPED_SPRUCE_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.STRIPPED_ACACIA_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.STRIPPED_BIRCH_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.STRIPPED_CHERRY_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.STRIPPED_DARK_OAK_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.STRIPPED_JUNGLE_ROCK_SHARPENING_STATION );
        addDrop(ModBlocks.STRIPPED_MANGROVE_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.BAMBOO_ROCK_SHARPENING_STATION);
        addDrop(ModBlocks.STRIPPED_BAMBOO_SHARPENING_STATION);
        addDrop(ModBlocks.PRIMITIVE_OAK_CRAFTING_BENCH);
        addDrop(Blocks.GRASS, grassDrops(Blocks.GRASS));
        addDrop(Blocks.TALL_GRASS, tallGrassDrops(Blocks.TALL_GRASS, Blocks.GRASS));
        addDrop(Blocks.FERN, grassDrops(Blocks.FERN));
        addDrop(Blocks.LARGE_FERN, tallGrassDrops(Blocks.LARGE_FERN, Blocks.FERN));
        addDrop(ModBlocks.PRIMITIVE_CAMPFIRE);
        addDrop(ModBlocks.ACACIA_STUMP);
        addDrop(ModBlocks.BAMBOO_STUMP);
        addDrop(ModBlocks.BIRCH_STUMP);
        addDrop(ModBlocks.CHERRY_STUMP);
        addDrop(ModBlocks.DARK_OAK_STUMP);
        addDrop(ModBlocks.JUNGLE_STUMP);
        addDrop(ModBlocks.MANGROVE_STUMP);
        addDrop(ModBlocks.OAK_STUMP);
        addDrop(ModBlocks.SPRUCE_STUMP);
        addDrop(ModBlocks.STRIPPED_ACACIA_STUMP);
        addDrop(ModBlocks.STRIPPED_BAMBOO_STUMP);
        addDrop(ModBlocks.STRIPPED_BIRCH_STUMP);
        addDrop(ModBlocks.STRIPPED_CHERRY_STUMP);
        addDrop(ModBlocks.STRIPPED_DARK_OAK_STUMP);
        addDrop(ModBlocks.STRIPPED_JUNGLE_STUMP);
        addDrop(ModBlocks.STRIPPED_MANGROVE_STUMP);
        addDrop(ModBlocks.STRIPPED_OAK_STUMP);
        addDrop(ModBlocks.STRIPPED_SPRUCE_STUMP);
    }

    @Override
    public LootTable.Builder grassDrops(Block dropWithShears) {
        return dropsWithShears(dropWithShears, (LootPoolEntry.Builder)applyExplosionDecay(Blocks.GRASS,
                ((LeafEntry.Builder) ItemEntry.builder(ModItems.PLANT_FIBER)
                        .conditionally(RandomChanceLootCondition.builder(1f)))
                        .apply(SetCountLootFunction.builder(BinomialLootNumberProvider.create(2, 0.3f)))
                        .apply(ApplyBonusLootFunction.binomialWithBonusCount(Enchantments.FORTUNE, 0.2f, 1))));
    }

    @Override
    public LootTable.Builder tallGrassDrops(Block tallGrass, Block grass) {
        LootPoolEntry.Builder<?> builder = ((LeafEntry.Builder)ItemEntry.builder(grass).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2.0F))).conditionally(WITH_SHEARS)).alternatively(((LeafEntry.Builder)this.addSurvivesExplosionCondition(tallGrass, ItemEntry.builder(ModItems.PLANT_FIBER))).apply(SetCountLootFunction.builder(BinomialLootNumberProvider.create(3, 0.2f))).conditionally(RandomChanceLootCondition.builder(1F)));
        return LootTable.builder().pool(LootPool.builder().with(builder).conditionally(BlockStatePropertyLootCondition.builder(tallGrass).properties(StatePredicate.Builder.create().exactMatch(TallPlantBlock.HALF, DoubleBlockHalf.LOWER))).conditionally(LocationCheckLootCondition.builder(net.minecraft.predicate.entity.LocationPredicate.Builder.create().block(net.minecraft.predicate.BlockPredicate.Builder.create().blocks(new Block[]{tallGrass}).state(StatePredicate.Builder.create().exactMatch(TallPlantBlock.HALF, DoubleBlockHalf.UPPER).build()).build()), new BlockPos(0, 1, 0)))).pool(LootPool.builder().with(builder).conditionally(BlockStatePropertyLootCondition.builder(tallGrass).properties(StatePredicate.Builder.create().exactMatch(TallPlantBlock.HALF, DoubleBlockHalf.UPPER))).conditionally(LocationCheckLootCondition.builder(net.minecraft.predicate.entity.LocationPredicate.Builder.create().block(net.minecraft.predicate.BlockPredicate.Builder.create().blocks(new Block[]{tallGrass}).state(StatePredicate.Builder.create().exactMatch(TallPlantBlock.HALF, DoubleBlockHalf.LOWER).build()).build()), new BlockPos(0, -1, 0))));

    }
}
//ModItems.PLANT_FIBER, BinomialLootNumberProvider.create(3, 0.2f)))