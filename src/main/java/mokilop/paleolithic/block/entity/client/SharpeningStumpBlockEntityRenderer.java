package mokilop.paleolithic.block.entity.client;

import mokilop.paleolithic.block.custom.SharpeningStumpBlock;
import mokilop.paleolithic.block.entity.SharpeningStumpBlockEntity;
import mokilop.paleolithic.util.Helpers;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

public class SharpeningStumpBlockEntityRenderer implements BlockEntityRenderer<SharpeningStumpBlockEntity> {
    private final ItemRenderer itemRenderer;
    public SharpeningStumpBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(SharpeningStumpBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        assert entity.hasWorld();
        ItemStack stack = entity.getRenderStack();
        Direction facing = entity.getCachedState().get(SharpeningStumpBlock.FACING);
        matrices.push();
        matrices.translate(.5f + .1f * facing.getOffsetX(), .6875f, .5f + .1f * facing.getOffsetZ());
        float scale = .4f;
        matrices.scale(scale, scale, scale);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-facing.getOpposite().asRotation()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
        itemRenderer.renderItem(stack, ModelTransformationMode.FIXED, Helpers.getLightLevel(entity.getWorld(), entity.getPos()),
                OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
        matrices.pop();
    }
}
