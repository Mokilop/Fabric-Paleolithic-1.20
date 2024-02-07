package mokilop.paleolithic.block.entity;

import com.sun.jna.platform.win32.WinDef;
import mokilop.paleolithic.item.custom.HammerItem;
import mokilop.paleolithic.recipe.SharpeningRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.block.SideShapeType;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class SharpeningStumpBlockEntity extends BlockEntityWithDisplayableInventory{
    private int progress;
    public SharpeningStumpBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SHARPENING_STUMP, pos, state, 1);
    }

    @Override
    public ItemStack getRenderStack() {
        return getStack(0);
    }

    public void sharpen(ItemStack mhs, World world, ServerPlayerEntity p) {
        SimpleInventory inv = new SimpleInventory(getStack(0));
        Optional<SharpeningRecipe> match = world.getRecipeManager()
                .getFirstMatch(SharpeningRecipe.Type.INSTANCE, inv, world);
        if(match.isEmpty())return;
        progress++;
        SharpeningRecipe recipe = match.get();
        if(progress >= recipe.sharpenTimes){
            ItemStack result = recipe.getOutput(world.getRegistryManager());
            progress = 0;
            mhs.damage(p.isCreative() ? 0 : 1, world.getRandom(), p);
            setStack(0, result);
            markDirty();
        }
    }
}
