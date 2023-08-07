package mokilop.paleolithic.util;

import mokilop.paleolithic.data.ModItemTagProvider;
import mokilop.paleolithic.item.ModItems;
import net.fabricmc.fabric.api.registry.FuelRegistry;

public class ModRegistries {
    public static void registerModRegistryEntries(){
        registerFuels();
    }
    private static void registerFuels(){
        FuelRegistry reg = FuelRegistry.INSTANCE;
        reg.add(ModTags.Items.ROCK_SHARPENING_STATIONS, 200);
        reg.add(ModTags.Items.STUMPS, 200);
        reg.add(ModTags.Items.CRAFTING_STUMPS, 300);
        reg.add(ModTags.Items.DRYING_RACKS, 200);
        reg.add(ModItems.PLANT_FIBER, 20);
        reg.add(ModItems.PLANT_TWINE, 40);
    }
}
