package mokilop.paleolithic.util;

import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SoundEffect {
    private final SoundEvent soundEvent;
    private final SoundCategory soundCategory;
    private final float volume;
    private final float pitch;

    public SoundEffect(SoundEvent soundEvent, SoundCategory soundCategory, float volume, float pitch) {
        this.soundEvent = soundEvent;
        this.soundCategory = soundCategory;
        this.volume = volume;
        this.pitch = pitch;
    }

    public void play(World world, BlockPos pos) {
        world.playSound(null, pos, soundEvent, soundCategory, volume, pitch);
    }
}
