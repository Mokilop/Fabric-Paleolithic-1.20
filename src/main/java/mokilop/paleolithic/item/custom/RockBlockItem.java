package mokilop.paleolithic.item.custom;

import mokilop.paleolithic.entity.custom.RockEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class RockBlockItem extends BlockItem {
    public RockBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        if (!world.isClient()) {
            RockEntity rockEntity = new RockEntity(world, user);
            rockEntity.setItem(itemStack);
            rockEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 1.0f);
            world.spawnEntity(rockEntity);
            user.getItemCooldownManager().set(this, 30);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        itemStack.decrement(user.isCreative() ? 0 : 1);
        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity player)) {
            return;
        }
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        if (!world.isClient()) {
            RockEntity rockEntity = new RockEntity(world, user);
            rockEntity.setItem(stack);
            rockEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 1.0f);
            world.spawnEntity(rockEntity);
        }
        player.incrementStat(Stats.USED.getOrCreateStat(this));
        stack.decrement(player.isCreative() ? 0 : 1);
    }
}
