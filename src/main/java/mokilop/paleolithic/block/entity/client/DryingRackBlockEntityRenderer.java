package mokilop.paleolithic.block.entity.client;

import com.ibm.icu.text.Normalizer2;
import mokilop.paleolithic.Paleolithic;
import mokilop.paleolithic.block.ModBlocks;
import mokilop.paleolithic.block.custom.DryingRackBlock;
import mokilop.paleolithic.block.custom.StumpBlock;
import mokilop.paleolithic.block.entity.DryingRackBlockEntity;
import mokilop.paleolithic.block.enums.ComplexAttachment;
import mokilop.paleolithic.item.ModItemGroup;
import mokilop.paleolithic.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.Attachment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.data.client.Model;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class DryingRackBlockEntityRenderer implements BlockEntityRenderer<DryingRackBlockEntity> {
    public DryingRackBlockEntityRenderer(BlockEntityRendererFactory.Context context) {

    }
    @Override
    public void render(DryingRackBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        ItemStack itemStack = entity.getRenderStack();
        ComplexAttachment att = entity.getCachedState().get(DryingRackBlock.ATTACHMENT);
        Direction facing = entity.getCachedState().get(DryingRackBlock.FACING);
        boolean isPerpendicular = att.getSimpleAttachment() == Attachment.SINGLE_WALL && att != ComplexAttachment.WALL_UP;
        if(isPerpendicular)matrices.translate(facing.getAxis() == Direction.Axis.X ? -0.0625 * facing.getOffsetX() : 0, 0, facing.getAxis() == Direction.Axis.Z ? -0.0625 * facing.getOffsetZ() : 0);
        facing = isPerpendicular ? facing.rotateYClockwise() : facing;
        float offset = att.getOffset() * 0.375f;
        float xOffset = (facing.getAxis() == Direction.Axis.X ? offset : 0) * facing.getOpposite().getOffsetX();
        float zOffset = (facing.getAxis() == Direction.Axis.Z ? offset : 0) * facing.getOpposite().getOffsetZ();
        matrices.push();
        matrices.translate(0.5f + xOffset, 0.5f, 0.5f + zOffset);
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
}
