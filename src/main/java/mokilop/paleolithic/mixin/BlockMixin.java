package mokilop.paleolithic.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.enums.Instrument;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Blocks.class)
public class BlockMixin {
    @Inject(at = @At("HEAD"), method = "createLogBlock(Lnet/minecraft/block/MapColor;Lnet/minecraft/block/MapColor;)Lnet/minecraft/block/PillarBlock;", cancellable = true)
    private static void createLogBlock(MapColor topMapColor, MapColor sideMapColor, CallbackInfoReturnable<PillarBlock> cir) {
        PillarBlock r = new PillarBlock(AbstractBlock.Settings.create().mapColor(state -> state.get(PillarBlock.AXIS) == Direction.Axis.Y ? topMapColor : sideMapColor).instrument(Instrument.BASS).strength(2.0f).requiresTool().sounds(BlockSoundGroup.WOOD).burnable());
        cir.setReturnValue(r);
    }
}