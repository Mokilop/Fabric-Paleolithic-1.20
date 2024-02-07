package mokilop.paleolithic.util;

import net.minecraft.block.LeavesBlock;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Set;

public class Helpers {
    public static boolean isNS(Direction dir) {
        return dir.getHorizontal() % 2 == 0;
    }

    public static boolean isSE(Direction dir) {
        return dir.getId() % 2 == 1 && dir.getId() > 1;
    }

    public static boolean isNE(Direction dir) {
        return dir.getHorizontal() >= 2;
    }

    public static int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }
}
