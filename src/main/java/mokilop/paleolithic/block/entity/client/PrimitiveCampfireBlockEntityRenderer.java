package mokilop.paleolithic.block.entity.client;

import mokilop.paleolithic.block.custom.PrimitiveCampfireBlock;
import mokilop.paleolithic.block.entity.PrimitiveCampfireBlockEntity;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.LightType;
import net.minecraft.world.World;


public class PrimitiveCampfireBlockEntityRenderer implements BlockEntityRenderer<PrimitiveCampfireBlockEntity> {
    private final ItemRenderer itemRenderer;
    public PrimitiveCampfireBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(PrimitiveCampfireBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemStack itemStack = entity.getRenderStack();
        matrices.push();
        float yOffset = itemRenderer.getModels().getModel(itemStack).hasDepth() ? .325f : .425f;
        matrices.translate(.5f, yOffset, .5f);
        float s = .5f;
        matrices.scale(s,s,s);
        float rotation = -entity.getCachedState().get(PrimitiveCampfireBlock.FACING).getOpposite().asRotation();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation - 15));
        itemRenderer.renderItem(itemStack, ModelTransformationMode.FIXED, getLightLevel(entity.getWorld(), entity.getPos()),
                OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);

        matrices.pop();
    }

    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }
}
