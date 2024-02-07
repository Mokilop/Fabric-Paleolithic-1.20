package mokilop.paleolithic.block.entity.client;

import mokilop.paleolithic.block.custom.DryingRackBlock;
import mokilop.paleolithic.block.entity.DryingRackBlockEntity;
import mokilop.paleolithic.block.enums.ComplexAttachment;
import net.minecraft.block.enums.Attachment;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.data.client.TexturedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class DryingRackBlockEntityRenderer implements BlockEntityRenderer<DryingRackBlockEntity> {
    private final ItemRenderer itemRenderer;
    public DryingRackBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        itemRenderer = context.getItemRenderer();
    }
    @Override
    public void render(DryingRackBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemStack itemStack = entity.getRenderStack();
        ComplexAttachment att = entity.getCachedState().get(DryingRackBlock.ATTACHMENT);
        Direction facing = entity.getCachedState().get(DryingRackBlock.FACING);
        boolean isPerpendicular = att.getSimpleAttachment() == Attachment.SINGLE_WALL && att != ComplexAttachment.WALL_UP;
        if(isPerpendicular)matrices.translate(-0.0625 * facing.getOffsetX(), 0, -0.0625 * facing.getOffsetZ());
        facing = isPerpendicular ? facing.rotateYClockwise() : facing;
        float offset = att.getOffset() * 0.375f;
        float xOffset = offset * facing.getOpposite().getOffsetX();
        float zOffset = offset * facing.getOpposite().getOffsetZ();
        boolean hasDepth = itemRenderer.getModels().getModel(itemStack).hasDepth();

        matrices.push();
        matrices.translate(0.5f + xOffset, 0.5f + yOffset(hasDepth), 0.5f + zOffset);
        matrices.scale(0.625f,0.625f,0.625f);
        float rotation = -facing.getOpposite().asRotation();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));
        itemRenderer.renderItem(itemStack, ModelTransformationMode.FIXED, getLightLevel(entity.getWorld(), entity.getPos()),
                OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
        matrices.pop();
    }

    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }

    private float yOffset(boolean hasDepth){
        if(hasDepth)return 0.15f;
        return 0;
    }
}
