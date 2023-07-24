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
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class StumpBlockEntity extends BlockEntity implements ImplementedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private int progress = 0;
    private static int maxProgress = 3;

    public StumpBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STUMP, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    public boolean addItem(ItemStack item) {
        if(!inventory.get(0).isEmpty())return false;
        inventory.set(0, item.copyWithCount(1));
        return true;
    }

    public boolean removeItem(PlayerEntity player){
        if(inventory.get(0).isEmpty())return false;
        player.giveItemStack(inventory.get(0));
        inventory.set(0, ItemStack.EMPTY);
        return true;
    }

    public void setInventory(DefaultedList<ItemStack> inventory) {
        for(int i = 0; i<inventory.size(); i++){
            this.inventory.set(i, inventory.get(i));
        }
    }

    private static boolean hasChoppingRecipe(StumpBlockEntity entity){
        return getMatchForChopping(entity).isPresent();
    }

    private static Optional<StumpChoppingRecipe> getMatchForChopping(StumpBlockEntity entity){
        SimpleInventory inv = new SimpleInventory(entity.size());
        for(int i = 0; i< entity.size(); i++){
            inv.setStack(i, entity.getStack(i));
        }
        Paleolithic.LOGGER.info(String.valueOf(inv.getStack(0).getTranslationKey()));
        return entity.getWorld().getRecipeManager()
                .getFirstMatch(StumpChoppingRecipe.Type.INSTANCE, inv, entity.getWorld());
    }

    private static void craftItemFromChopping(StumpBlockEntity entity){
        Optional<StumpChoppingRecipe> match = getMatchForChopping(entity);
        if(match.isEmpty())return;
        ItemStack outputItems = getMatchForChopping(entity).get().getOutput(null);
        BlockPos pos = entity.pos;
        World world = entity.getWorld();
        ItemEntity outputEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5, outputItems, 0, 0, 0);
        entity.inventory.set(0, ItemStack.EMPTY);
        world.spawnEntity(outputEntity);
    }

    public static boolean chop(World world, BlockPos blockPos, BlockState blockState,StumpBlockEntity entity){
        if(world.isClient())return false;
        if(getMatchForChopping(entity).isEmpty()){
            Paleolithic.LOGGER.info("No recipe");
            Paleolithic.LOGGER.info(String.valueOf(world.isClient()));
            return false;
        }
        entity.progress++;
        Paleolithic.LOGGER.info(String.valueOf(entity.progress));
        if(entity.progress >= maxProgress){
            entity.progress = 0;
            craftItemFromChopping(entity);
            markDirty(world, blockPos, blockState);
            entity.markDirty();
        }
        markDirty(world, blockPos, blockState);
        return true;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("stump.progress", progress);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        super.readNbt(nbt);
        progress = nbt.getInt("stump.progress");
    }

    /*
    @Override
    public void markDirty() {
        if(!world.isClient()) {
            PacketByteBuf data = PacketByteBufs.create();
            data.writeInt(inventory.size());
            for (ItemStack itemStack : inventory) {
                data.writeItemStack(itemStack);
            }
            data.writeBlockPos(getPos());

            for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, getPos())) {
                ServerPlayNetworking.send(player, ModMessages.ITEM_SYNC, data);
            }
        }
        super.markDirty();
    }
    */
}
