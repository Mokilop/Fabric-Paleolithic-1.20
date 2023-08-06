package mokilop.paleolithic.block.entity.client;

import mokilop.paleolithic.block.custom.CraftingStumpBlock;
import mokilop.paleolithic.block.entity.CraftingStumpBlockEntity;
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
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class CraftingStumpBlockEntityRenderer implements BlockEntityRenderer<CraftingStumpBlockEntity> {

    public CraftingStumpBlockEntityRenderer(BlockEntityRendererFactory.Context context) {

    }

    @Override
    public void render(CraftingStumpBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        float rotDeg = entity.getCachedState().get(CraftingStumpBlock.FACING).getOpposite().asRotation();
        for (int i = 0; i < entity.size() - 1; i++){
            ItemStack toRender = entity.getStack(i);
            matrices.push();
            matrices.translate(getXOffset(i, entity), getYOffset(entity, tickDelta), getZOffset(i, entity));
            matrices.scale(0.25f, 0.25f, 0.25f);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotDeg + CraftingStumpBlockEntity.getRandomRotationAmounts(entity)[i]));
            itemRenderer.renderItem(toRender, ModelTransformationMode.FIXED, getLightLevel(entity.getWorld(), entity.getPos()),
                    OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
            matrices.pop();
        }
        ItemStack extraItem = entity.getStack(9);
        matrices.push();
        switch (entity.getCachedState().get(CraftingStumpBlock.FACING)) {
            case NORTH -> {
                matrices.translate(0.485, 0.485, 0.4);
                matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(225));
            }
            case SOUTH -> {
                matrices.translate(0.515, 0.485, 0.6);
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(225));
            }
            case WEST -> {
                matrices.translate(0.4, 0.485, 0.485);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(225));
            }
            case EAST -> {
                matrices.translate(0.6, 0.485, 0.515);
                matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(225));
            }
        }
        matrices.scale(0.35f, 0.35f, 0.35f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotDeg));
        itemRenderer.renderItem(extraItem, ModelTransformationMode.FIXED, getLightLevel(entity.getWorld(), entity.getPos()),
                OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
        matrices.pop();
    }
    private float getXOffset(int slot, CraftingStumpBlockEntity e){
        Direction f = e.getCachedState().get(CraftingStumpBlock.FACING);
        int xSlot = f == Direction.EAST || f == Direction.WEST ? slot / 3 : slot % 3;
        float temp = xSlot == 0 ? 0.1875f : xSlot == 1 ? 0.5f : 0.8125f;
        return f == Direction.NORTH || f == Direction.WEST ? 1 - temp : temp;
    }
    private float getZOffset(int slot, CraftingStumpBlockEntity e){
        Direction f = e.getCachedState().get(CraftingStumpBlock.FACING);
        int zSlot = f == Direction.EAST || f == Direction.WEST ? slot % 3 : slot / 3;
        float temp = zSlot == 0 ? 0.1875f : zSlot == 1 ? 0.5f : 0.8125f;
        return f == Direction.NORTH || f == Direction.EAST ? 1 - temp : temp;
    }
    private float getYOffset(CraftingStumpBlockEntity e, float delta){
        float base = 0.9375f;
        if(e.crafting){
            return base + (CraftingStumpBlockEntity.maxCraftingTicks + 1 - e.craftingTicks - delta) / (50f * CraftingStumpBlockEntity.maxCraftingTicks);
        }
        return base;
    }
    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }

}
