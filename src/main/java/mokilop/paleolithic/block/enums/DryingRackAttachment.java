package mokilop.paleolithic.block.enums;

import net.minecraft.util.StringIdentifiable;

public enum DryingRackAttachment implements StringIdentifiable {
    FLOOR("floor"),
    CEILING("ceiling"),
    WALL("wall"),
    FLOOR_CLOSE("floor_close"),
    CEILING_CLOSE("ceiling_close"),
    WALL_LEFT("wall_left"),
    FLOOR_FAR("floor_far"),
    CEILING_FAR("ceiling_far"),
    WALL_RIGHT("wall_right"),
    WALL_UP("wall_up");



    private final String name;

    private DryingRackAttachment(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}
