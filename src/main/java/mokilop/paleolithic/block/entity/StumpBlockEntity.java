package mokilop.paleolithic.block.entity;

import mokilop.paleolithic.Paleolithic;
import mokilop.paleolithic.networking.ModMessages;
import mokilop.paleolithic.recipe.StumpChoppingRecipe;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class StumpBlockEntity extends BlockEntityWithDisplayableInventory {

    private int progress = 0;
    private static int maxProgress = 3;

    public StumpBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STUMP, pos, state, 1);
    }

    public boolean addItem(ItemStack item) {
        if (!inventory.get(0).isEmpty() || item.isEmpty()) return false;
        inventory.set(0, item.copyWithCount(1));
        progress = 0;
        markDirty();
        world.playSound(null, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1, 0.2f);
        return true;
    }

    public boolean removeItem(PlayerEntity player) {
        if (inventory.get(0).isEmpty()) return false;
        if(!player.isCreative()) player.giveItemStack(inventory.get(0));
        inventory.set(0, ItemStack.EMPTY);
        markDirty();
        world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 1, 1);
        return true;
    }

    private static boolean hasChoppingRecipe(StumpBlockEntity entity) {
        return getMatchForChopping(entity).isPresent();
    }

    private static Optional<StumpChoppingRecipe> getMatchForChopping(StumpBlockEntity entity) {
        SimpleInventory inv = new SimpleInventory(entity.size());
        for (int i = 0; i < entity.size(); i++) {
            inv.setStack(i, entity.getStack(i));
        }
        return entity.getWorld().getRecipeManager()
                .getFirstMatch(StumpChoppingRecipe.Type.INSTANCE, inv, entity.getWorld());
    }

    private static void craftItemFromChopping(StumpBlockEntity entity) {
        Optional<StumpChoppingRecipe> match = getMatchForChopping(entity);
        if (match.isEmpty()) return;
        ItemStack outputItems = getMatchForChopping(entity).get().getOutput(null);
        BlockPos pos = entity.pos;
        World world = entity.getWorld();
        ItemEntity outputEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5, outputItems, 0, 0, 0);
        entity.inventory.set(0, ItemStack.EMPTY);
        world.spawnEntity(outputEntity);
    }

    public static boolean chop(World world, BlockPos blockPos, BlockState blockState, StumpBlockEntity entity, boolean fullyCharged) {
        if (world.isClient()) return false;
        if (getMatchForChopping(entity).isEmpty()) {
            return false;
        }
        entity.progress++;
        if (entity.progress >= maxProgress || fullyCharged) {
            entity.progress = 0;
            craftItemFromChopping(entity);
            world.playSound(null,blockPos, SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, 1, 1.5f);
            world.playSound(null,blockPos, SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, 1, 1.5f);
            entity.markDirty();
            markDirty(world, blockPos, blockState);
        }
        return true;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("stump.progress", progress);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        progress = nbt.getInt("stump.progress");
    }

    public ItemStack getRenderStack() {
        return getStack(0);
    }
}
