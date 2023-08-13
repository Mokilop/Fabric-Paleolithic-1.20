package mokilop.paleolithic.util;

import net.minecraft.util.math.Direction;

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
}
