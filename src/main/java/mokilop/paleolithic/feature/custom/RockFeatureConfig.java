package mokilop.paleolithic.feature.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.gen.feature.FeatureConfig;

public record RockFeatureConfig(int maxInPile, int maxNumberOfPiles, int spreadXZ, int chanceToSpawnPercentage) implements FeatureConfig {
    public static Codec<RockFeatureConfig> CODEC = RecordCodecBuilder.create(
            rockFeatureConfigInstance ->
                    rockFeatureConfigInstance.group(
                            Codecs.POSITIVE_INT.fieldOf("maxInPile").forGetter(RockFeatureConfig::maxInPile),
                            Codecs.POSITIVE_INT.fieldOf("maxNumberOfPiles").forGetter(RockFeatureConfig::maxNumberOfPiles),
                            Codecs.POSITIVE_INT.fieldOf("spreadXZ").forGetter(RockFeatureConfig::spreadXZ),
                            Codecs.POSITIVE_INT.fieldOf("chanceToSpawnPercentage").forGetter(RockFeatureConfig::chanceToSpawnPercentage)
                    ).apply(rockFeatureConfigInstance, RockFeatureConfig::new));


    public int maxInPile(){
        return  maxInPile;
    }
    public int maxNumberOfPiles(){
        return  maxNumberOfPiles;
    }
    public int spreadXZ(){
        return  spreadXZ;
    }
    public int chanceToSpawnPercentage(){
        return  chanceToSpawnPercentage;
    }

}
