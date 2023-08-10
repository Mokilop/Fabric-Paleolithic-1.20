package mokilop.paleolithic;

import mokilop.paleolithic.block.entity.ModBlockEntities;
import mokilop.paleolithic.block.entity.client.CraftingStumpBlockEntityRenderer;
import mokilop.paleolithic.block.entity.client.DryingRackBlockEntityRenderer;
import mokilop.paleolithic.block.entity.client.PrimitiveCampfireBlockEntityRenderer;
import mokilop.paleolithic.block.entity.client.StumpBlockEntityRenderer;
import mokilop.paleolithic.networking.ModMessages;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class PaleolithicClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModMessages.registerS2CPackets();


        BlockEntityRendererFactories.register(ModBlockEntities.PRIMITIVE_CAMPFIRE, PrimitiveCampfireBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.STUMP, StumpBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.CRAFTING_STUMP, CraftingStumpBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.DRYING_RACK, DryingRackBlockEntityRenderer::new);
    }
}
