package mokilop.paleolithic.item.custom;

import mokilop.paleolithic.entity.custom.RockEntity;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class RockBlockItem extends BlockItem {

    public static final int BASE_LAUNCH_SPEED = 2;

    public RockBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity player)) {
            return;
        }
        int usedForTicks = this.getMaxUseTime(stack) - remainingUseTicks;
        float chargeProgress = BowItem.getPullProgress(usedForTicks);
        if (chargeProgress < .1f) {
            return;
        }
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        if (!world.isClient()) {
            RockEntity rockEntity = new RockEntity(world, user);
            rockEntity.setItem(stack);
            rockEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, BASE_LAUNCH_SPEED * chargeProgress, 1.0f);
            world.spawnEntity(rockEntity);
        }
        player.incrementStat(Stats.USED.getOrCreateStat(this));
        stack.decrement(player.isCreative() ? 0 : 1);
    }
}
