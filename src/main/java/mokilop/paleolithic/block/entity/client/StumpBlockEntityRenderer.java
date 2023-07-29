package mokilop.paleolithic.block.entity.client;

import mokilop.paleolithic.block.custom.StumpBlock;
import mokilop.paleolithic.block.entity.StumpBlockEntity;
import net.minecraft.client.MinecraftClient;
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

public class StumpBlockEntityRenderer implements BlockEntityRenderer<StumpBlockEntity> {

    public StumpBlockEntityRenderer(BlockEntityRendererFactory.Context context) {

    }

    @Override
    public void render(StumpBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        ItemStack itemStack = entity.getRenderStack();
        matrices.push();
        matrices.translate(0.499f, 0.7f, 0.499f);
        matrices.scale(1.3f, 1.3f, 1.3f);
        switch (entity.getCachedState().get(StumpBlock.FACING)) {
            case NORTH -> matrices.multiply(RotationAxis.POSITIVE_Y.rotation(0));
            case WEST -> matrices.multiply(RotationAxis.POSITIVE_Y.rotation(1.5708f));
            case SOUTH -> matrices.multiply(RotationAxis.POSITIVE_Y.rotation(3.14159f));
            case EAST -> matrices.multiply(RotationAxis.POSITIVE_Y.rotation(4.71239f));
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
