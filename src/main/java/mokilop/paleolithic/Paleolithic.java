package mokilop.paleolithic;

import mokilop.paleolithic.block.ModBlocks;
import mokilop.paleolithic.block.entity.ModBlockEntities;
import mokilop.paleolithic.item.ModItemGroup;
import mokilop.paleolithic.item.ModItems;
import mokilop.paleolithic.networking.ModMessages;
import mokilop.paleolithic.recipe.ModRecipes;
import mokilop.paleolithic.sound.ModSounds;
import mokilop.paleolithic.util.ModLootTableModifiers;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static mokilop.paleolithic.feature.ModFeatures.registerModFeatures;
import static mokilop.paleolithic.world.gen.ModWorldGeneration.generateModWorldGen;

public class Paleolithic implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final String MOD_ID = "paleolithic";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        ModItemGroup.registerItemGroups();
        ModBlocks.registerModBlocks();
        ModItems.registerModItems();
        ModSounds.registerModSounds();
        ModRecipes.registerRecipes();
        registerModFeatures();
        ModLootTableModifiers.modifyLootTables();
        ModMessages.registerC2SPackets();
        ModBlockEntities.registerBlocksEntities();
        generateModWorldGen();
    }
}