package mokilop.paleolithic.world;

import mokilop.paleolithic.Paleolithic;
import mokilop.paleolithic.feature.ModFeatures;
import mokilop.paleolithic.feature.custom.RockFeatureConfig;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class ModConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> ROCK_KEY = registerKey("rock");

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {
        register(context, ROCK_KEY, ModFeatures.ROCK, new RockFeatureConfig(3, 3, 10, 35));
    }

    private static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier(Paleolithic.MOD_ID, name));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context,
                                                                                   RegistryKey<ConfiguredFeature<?, ?>> key,
                                                                                   F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
