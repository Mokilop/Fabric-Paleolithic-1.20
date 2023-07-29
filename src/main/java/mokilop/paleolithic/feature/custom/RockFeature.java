package mokilop.paleolithic.feature.custom;

import com.mojang.serialization.Codec;
import mokilop.paleolithic.block.ModBlocks;
import mokilop.paleolithic.block.custom.RockBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class RockFeature extends Feature<RockFeatureConfig> {
    public RockFeature(Codec<RockFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<RockFeatureConfig> context) {
        int amountGenerated = 0;
        Random random = context.getRandom();
        StructureWorldAccess structureWorldAccess = context.getWorld();
        BlockPos originPos = context.getOrigin();
        RockFeatureConfig config = context.getConfig();
        if (random.nextInt(100) > config.chanceToSpawnPercentage()) return false;
        int maxCountInCluster = config.maxNumberOfPiles();
        int countInCluster = random.nextInt(maxCountInCluster) + 1;
        for (int i = 0; i < countInCluster; i++) {
            int xOffset = random.nextInt(config.spreadXZ()) - random.nextInt(config.spreadXZ());
            int zOffset = random.nextInt(config.spreadXZ()) - random.nextInt(config.spreadXZ());
            int currentX = originPos.getX() + xOffset;
            int currentZ = originPos.getZ() + zOffset;
            int surfaceY = structureWorldAccess.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, currentX, currentZ);
            BlockPos surfacePos = new BlockPos(currentX, surfaceY, currentZ);
            int maxStonesInPile = Math.min(3, config.maxInPile());
            BlockState blockState = ModBlocks.ROCK.getDefaultState()
                    .with(RockBlock.STONES, random.nextInt(maxStonesInPile) + 1)
                    .with(RockBlock.FACING, getRandomHorizontalDirection(random))
                    .with(RockBlock.WATERLOGGED, structureWorldAccess.getFluidState(surfacePos).getFluid() == Fluids.WATER);
            if (!structureWorldAccess.getBlockState(surfacePos.down())
                    .isSolidBlock(structureWorldAccess, surfacePos.down()) ||
                    structureWorldAccess.getBlockState(surfacePos).isFullCube(structureWorldAccess, surfacePos) ||
                    surfaceY >= structureWorldAccess.getTopY()) continue;
            structureWorldAccess.setBlockState(surfacePos, blockState, Block.NOTIFY_LISTENERS);
            amountGenerated++;
        }
        return amountGenerated > 0;
    }

    private Direction getRandomHorizontalDirection(Random r) {
        int randomDirectionNumber = r.nextInt(4);
        switch (randomDirectionNumber) {
            case 1:
                return Direction.WEST;
            case 2:
                return Direction.SOUTH;
            case 3:
                return Direction.EAST;
            default:
                return Direction.NORTH;
        }
    }
}
