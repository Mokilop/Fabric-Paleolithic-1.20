package mokilop.paleolithic.feature;

import mokilop.paleolithic.Paleolithic;
import mokilop.paleolithic.feature.custom.RockFeature;
import mokilop.paleolithic.feature.custom.RockFeatureConfig;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.Feature;

public class ModFeatures {
    public static Feature ROCK = new RockFeature(RockFeatureConfig.CODEC);

    public static void registerModFeatures() {
        registerFeature("rock", ROCK);
    }

    public static Feature registerFeature(String name, Feature feature) {
        return Registry.register(Registries.FEATURE, new Identifier(Paleolithic.MOD_ID, name), feature);
    }

}
