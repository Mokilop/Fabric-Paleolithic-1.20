package mokilop.paleolithic.block.enums;

import mokilop.paleolithic.item.ModItems;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;

public enum StoneSharpeningStationBlockMode implements StringIdentifiable {
    HATCHET_HEAD("hatchet_head", ModItems.STONE_HATCHET_HEAD),
    SPEAR_HEAD("spear_head", ModItems.STONE_SPEAR_HEAD),
    PICKAXE_HEAD_FRAGMENT("pickaxe_head_fragment", ModItems.STONE_PICKAXE_HEAD_FRAGMENT),
    LUMBER_AXE_HEAD_FRAGMENT("lumber_axe_head_fragment", ModItems.STONE_LUMBER_AXE_HEAD_FRAGMENT);

    private final String name;
    private final Text text;

    private final Item item;

    private StoneSharpeningStationBlockMode(String name, Item item) {
        this.name = name;
        this.text = Text.translatable("stone_sharpening_station.mode_info." + name);
        this.item = item;
    }

    @Override
    public String asString() {
        return this.name;
    }

    public Text asText() {
        return this.text;
    }

    public Item getItem() { return this.item; }
}
