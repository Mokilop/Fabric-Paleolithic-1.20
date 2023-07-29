package mokilop.paleolithic.item;

import mokilop.paleolithic.Paleolithic;
import mokilop.paleolithic.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static RegistryKey<ItemGroup> PALEOLITHIC_ITEMS =
            RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(Paleolithic.MOD_ID, "paleolithic_items"));
    public static RegistryKey<ItemGroup> PALEOLITHIC_BLOCKS =
            RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(Paleolithic.MOD_ID, "paleolithic_blocks"));

    public static void registerItemGroups() {
        Registry.register(Registries.ITEM_GROUP, PALEOLITHIC_BLOCKS, FabricItemGroup.builder()
                .displayName(Text.translatable("itemgroup.paleolithic_blocks"))
                .icon(() -> new ItemStack(ModBlocks.PRIMITIVE_CAMPFIRE.asItem())).build());
        Registry.register(Registries.ITEM_GROUP, PALEOLITHIC_ITEMS, FabricItemGroup.builder()
                .displayName(Text.translatable("itemgroup.paleolithic_items"))
                .icon(() -> new ItemStack(ModBlocks.ROCK.asItem())).build());
    }
}
