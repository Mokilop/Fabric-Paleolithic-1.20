package mokilop.paleolithic.block.entity.client;

import mokilop.paleolithic.block.custom.StumpBlock;
import mokilop.paleolithic.block.entity.StumpBlockEntity;
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
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class StumpBlockEntityRenderer implements BlockEntityRenderer<StumpBlockEntity> {

    private final ItemRenderer itemRenderer;
    public StumpBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(StumpBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemStack itemStack = entity.getRenderStack();
        matrices.push();
        boolean hasDepth = itemRenderer.getModels().getModel(itemStack).hasDepth();
        float yTranslate = hasDepth ? 0.6875f : 0.390625f;
        matrices.translate(0.5f, yTranslate, 0.5f);
        float scale = hasDepth ? 1.25f : 0.75f;
        matrices.scale(scale, scale, scale);
        Direction facing = entity.getCachedState().get(StumpBlock.FACING);
        float rotation = facing.getOpposite().asRotation();
        if(hasDepth){
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-rotation));
        }
        else{
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation));
        }
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
