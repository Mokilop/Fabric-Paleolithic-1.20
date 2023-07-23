package mokilop.paleolithic;

import mokilop.paleolithic.block.entity.ModBlockEntities;
import mokilop.paleolithic.block.entity.client.PrimitiveCampfireBlockEntityRenderer;
import mokilop.paleolithic.networking.ModMessages;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class PaleolithicClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModMessages.registerS2CPackets();


        BlockEntityRendererFactories.register(ModBlockEntities.PRIMITIVE_CAMPFIRE, PrimitiveCampfireBlockEntityRenderer::new);
    }
}
