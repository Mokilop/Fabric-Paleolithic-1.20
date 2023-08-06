package mokilop.paleolithic.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DryingRackBlockEntity extends BlockEntityWithDisplayableInventory{
    public DryingRackBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DRYING_RACK, pos, state, 1);
    }




    public static void tick(World world, BlockPos blockPos, BlockState blockState,
                            DryingRackBlockEntity entity){

    }
}
