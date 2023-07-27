package mokilop.paleolithic.item;

import mokilop.paleolithic.Paleolithic;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item PLANT_FIBER = registerItem("plant_fiber", new Item(new FabricItemSettings()));
    public static final Item PLANT_TWINE = registerItem("plant_twine", new Item(new FabricItemSettings()));
    public static final Item STONE_HAMMER = registerItem("stone_hammer",
            new AxeItem(ModToolMaterial.UNSHARPENED_ROCK, 3.0f, -2.4f, new FabricItemSettings()));
    public static final Item STONE_HATCHET_HEAD = registerItem("stone_hatchet_head", new Item(new FabricItemSettings()));
    public static final Item STONE_SPEAR_HEAD = registerItem("stone_spear_head", new Item(new FabricItemSettings()));
    public static final Item STONE_PICKAXE_HEAD_FRAGMENT = registerItem("stone_pickaxe_head_fragment", new Item(new FabricItemSettings()));
    public static final Item STONE_LUMBER_AXE_HEAD_FRAGMENT = registerItem("stone_lumber_axe_head_fragment", new Item(new FabricItemSettings()));
    public static final Item STONE_HATCHET = registerItem("stone_hatchet", new AxeItem(ToolMaterials.STONE, 4.0f, -2f, new FabricItemSettings()));
    private static Item registerItem(String name, Item item){
        addItemToGroup(ModItemGroup.PALEOLITHIC_ITEMS, item);
        return Registry.register(Registries.ITEM, new Identifier(Paleolithic.MOD_ID, name), item);
    }
    private static Item registerItem(String name, Item item, RegistryKey<ItemGroup> group){
        addItemToGroup(group, item);
        return Registry.register(Registries.ITEM, new Identifier(Paleolithic.MOD_ID, name), item);
    }

    private static void addItemToGroup(RegistryKey<ItemGroup> group, Item item){
        ItemGroupEvents.modifyEntriesEvent(group).register(entries -> entries.add(item));
    }

    public static void registerModItems(){
        Paleolithic.LOGGER.info("Registering items for " + Paleolithic.MOD_ID);
    }
}
