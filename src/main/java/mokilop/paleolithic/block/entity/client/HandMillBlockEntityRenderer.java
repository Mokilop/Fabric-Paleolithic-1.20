package mokilop.paleolithic.block.entity.client;

import mokilop.paleolithic.block.entity.HandMillBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

public class HandMillBlockEntityRenderer implements BlockEntityRenderer<HandMillBlockEntity> {

    public HandMillBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(HandMillBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
    }
}
