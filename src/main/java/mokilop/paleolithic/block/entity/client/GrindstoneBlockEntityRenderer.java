package mokilop.paleolithic.block.entity.client;

import mokilop.paleolithic.block.entity.GrindstoneBlockEntity;
import mokilop.paleolithic.block.entity.client.model.ModEntityModelLayers;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

public class GrindstoneBlockEntityRenderer implements BlockEntityRenderer<GrindstoneBlockEntity> {
    private final ItemRenderer itemRenderer;
    public static final SpriteIdentifier GRINDSTONE_TEXTURE = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("block/stone"));
    private final ModelPart grindstoneTop;

    public GrindstoneBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        ModelPart modelPart = ctx.getLayerModelPart(ModEntityModelLayers.GRINDSTONE);
        this.itemRenderer = ctx.getItemRenderer();
        grindstoneTop = modelPart.getChild("top");
    }

    @Override
    public void render(GrindstoneBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        grindstoneTop.roll = 3f;
        VertexConsumer vertexConsumer = GRINDSTONE_TEXTURE.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid);
        grindstoneTop.render(new MatrixStack(), vertexConsumer, light, overlay);
    }
}
