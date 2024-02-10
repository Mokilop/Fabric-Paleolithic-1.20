package mokilop.paleolithic.item.custom;

import mokilop.paleolithic.util.ModTags;
import net.minecraft.item.AxeItem;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;

public class HammerItem extends MiningToolItem {
    public final int CRAFTING_EFFICIENCY;

    public HammerItem(ToolMaterial material, float attackDamage, float attackSpeed, int craftingEfficiency, Settings settings) {
        super(attackDamage, attackSpeed, material, ModTags.Blocks.HAMMER_MINEABLE, settings);
        CRAFTING_EFFICIENCY = craftingEfficiency;
    }
}
