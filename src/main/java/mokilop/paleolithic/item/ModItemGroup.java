package mokilop.paleolithic.item;

import mokilop.paleolithic.Paleolithic;
import mokilop.paleolithic.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static RegistryKey<ItemGroup> PALEOLITHIC =
            RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(Paleolithic.MOD_ID, "example"));

    public static void registerItemGroups() {
        Registry.register(Registries.ITEM_GROUP, PALEOLITHIC, FabricItemGroup.builder()
                .displayName(Text.translatable("itemgroup.paleolithic"))
                .icon(()->new ItemStack(ModBlocks.ROCK.asItem())).build());
    }
}
