package mokilop.paleolithic.sound;

import mokilop.paleolithic.Paleolithic;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static SoundEvent STUMP_SHATTER = registerSoundEvent("stump_shatter");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = new Identifier(Paleolithic.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerModSounds() {
        Paleolithic.LOGGER.info("Registering sounds for " + Paleolithic.MOD_ID);
    }
}
