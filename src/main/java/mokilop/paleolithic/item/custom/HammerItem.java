package mokilop.paleolithic.item.custom;

import net.minecraft.item.AxeItem;
import net.minecraft.item.ToolMaterial;

public class HammerItem extends AxeItem {
    public final int CRAFTING_EFFICIENCY;

    public HammerItem(ToolMaterial material, float attackDamage, float attackSpeed, int craftingEfficiency, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
        CRAFTING_EFFICIENCY = craftingEfficiency;
    }
}
