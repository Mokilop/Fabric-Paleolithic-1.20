package mokilop.paleolithic;

import mokilop.paleolithic.block.entity.ModBlockEntities;
import mokilop.paleolithic.block.entity.client.*;
import mokilop.paleolithic.block.entity.client.model.ModModelLoadingPlugin;
import mokilop.paleolithic.networking.ModMessages;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class PaleolithicClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelLoadingPlugin.register(new ModModelLoadingPlugin());
        ModMessages.registerS2CPackets();


        BlockEntityRendererFactories.register(ModBlockEntities.PRIMITIVE_CAMPFIRE, PrimitiveCampfireBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.STUMP, StumpBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.CRAFTING_STUMP, CraftingStumpBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.DRYING_RACK, DryingRackBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.SHARPENING_STUMP, SharpeningStumpBlockEntityRenderer::new);
        // BlockEntityRendererFactories.register(ModBlockEntities.GRINDSTONE, GrindstoneBlockEntityRenderer::new);

    }
}
