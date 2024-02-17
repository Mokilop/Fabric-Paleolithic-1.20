package mokilop.paleolithic.event;

import mokilop.paleolithic.util.ModTags;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.ActionResult;

public class BlockBreakingHandler {
    public static void registerBlockBreakCheck() {
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            if (player.isCreative() || !world.getBlockState(pos).isIn(ModTags.Blocks.NOT_HAND_MINEABLE) || player.getMainHandStack().isIn(ItemTags.AXES)) {
                return ActionResult.PASS;
            }
            return ActionResult.SUCCESS;
        });
    }

    public static void register() {
        registerBlockBreakCheck();
    }
}
