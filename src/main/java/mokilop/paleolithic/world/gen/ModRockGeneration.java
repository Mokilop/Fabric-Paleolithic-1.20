package mokilop.paleolithic.world.gen;

import mokilop.paleolithic.world.ModPlacedFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;

public class ModRockGeneration {
    public static void generateRocks() {
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld().and(BiomeSelectors.excludeByKey(BiomeKeys.DESERT)),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.ROCK_PLACED_KEY);
    }
}
