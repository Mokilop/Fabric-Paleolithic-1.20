package mokilop.paleolithic.block.enums;

import net.minecraft.block.enums.Attachment;
import net.minecraft.util.StringIdentifiable;

public enum ComplexAttachment implements StringIdentifiable {
    FLOOR("floor", Attachment.FLOOR, 0),
    CEILING("ceiling", Attachment.CEILING, 0),
    WALL("wall", Attachment.SINGLE_WALL, 0),
    FLOOR_CLOSE("floor_close", Attachment.FLOOR, -1),
    CEILING_CLOSE("ceiling_close", Attachment.CEILING, -1),
    WALL_LEFT("wall_left", Attachment.SINGLE_WALL, -1),
    FLOOR_FAR("floor_far", Attachment.FLOOR, 1),
    CEILING_FAR("ceiling_far", Attachment.CEILING, 1),
    WALL_RIGHT("wall_right", Attachment.SINGLE_WALL, 1),
    WALL_UP("wall_up", Attachment.SINGLE_WALL, 1);


    private final String name;
    private final Attachment simpleAttachment;
    private final int offset;

    ComplexAttachment(String name, Attachment simpleAttachment, int offset) {
        this.name = name;
        this.simpleAttachment = simpleAttachment;
        this.offset = offset;
    }

    @Override
    public String asString() {
        return this.name;
    }

    public Attachment getSimpleAttachment() {
        return simpleAttachment;
    }

    public int getOffset() {
        return offset;
    }
}
