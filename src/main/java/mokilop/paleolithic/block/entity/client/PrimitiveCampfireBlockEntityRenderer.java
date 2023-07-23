package mokilop.paleolithic.block.entity.client;

import mokilop.paleolithic.block.custom.PrimitiveCampfireBlock;
import mokilop.paleolithic.block.entity.PrimitiveCampfireBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.*;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.joml.Quaternionf;
import org.joml.Vector3f;


public class PrimitiveCampfireBlockEntityRenderer implements BlockEntityRenderer<PrimitiveCampfireBlockEntity> {
    public PrimitiveCampfireBlockEntityRenderer(BlockEntityRendererFactory.Context context){

    }

    @Override
    public void render(PrimitiveCampfireBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        ItemStack itemStack = entity.getRenderStack();
        matrices.push();
        matrices.translate(0.499f, 0.22f, 0.499f);
        switch(entity.getCachedState().get(PrimitiveCampfireBlock.FACING)){
            case NORTH -> matrices.multiply(RotationAxis.POSITIVE_Y.rotation(0));
            case WEST -> matrices.multiply(RotationAxis.POSITIVE_Y.rotation(1.5708f));
            case SOUTH -> matrices.multiply(RotationAxis.POSITIVE_Y.rotation(3.14159f));
            case EAST -> matrices.multiply(RotationAxis.POSITIVE_Y.rotation(4.71239f));
        }

        itemRenderer.renderItem(itemStack, ModelTransformationMode.GROUND, getLightLevel(entity.getWorld(), entity.getPos()),
                OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
        matrices.pop();
    }

    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }
}
