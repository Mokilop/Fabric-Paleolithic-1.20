package mokilop.paleolithic.block.entity.client;

import mokilop.paleolithic.block.custom.CraftingStumpBlock;
import mokilop.paleolithic.block.entity.CraftingStumpBlockEntity;
import mokilop.paleolithic.util.Helpers;
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
    private final ItemRenderer itemRenderer;
    public CraftingStumpBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(CraftingStumpBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Direction facing = entity.getCachedState().get(CraftingStumpBlock.FACING);
        float rotDeg = facing.getOpposite().asRotation();
        for (int i = 0; i < entity.size() - 1; i++) {
            ItemStack toRender = entity.getStack(i);
            matrices.push();
            matrices.translate(getXOffset(i, facing), getYOffset(entity, tickDelta), getZOffset(i, facing));
            matrices.scale(0.25f, 0.25f, 0.25f);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotDeg + CraftingStumpBlockEntity.getRandomRotationAmounts(entity)[i]));
            itemRenderer.renderItem(toRender, ModelTransformationMode.FIXED, getLightLevel(entity.getWorld(), entity.getPos()),
                    OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
            matrices.pop();
        }
        ItemStack extraItem = entity.getStack(9);
        matrices.push();
        float xOffset = 0.5f + 0.1f * facing.getOffsetX();
        float zOffset = 0.5f + 0.1f * facing.getOffsetZ();
        matrices.translate(xOffset, 0.465f, zOffset);
        matrices.multiply(RotationAxis.of(facing.getUnitVector()).rotationDegrees(225));
        matrices.scale(0.35f, 0.35f, 0.35f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotDeg));
        itemRenderer.renderItem(extraItem, ModelTransformationMode.FIXED, getLightLevel(entity.getWorld(), entity.getPos()),
                OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
        matrices.pop();
    }

    private float getXOffset(int slot, Direction f) {
        int xSlot = f.getAxis() == Direction.Axis.X ? slot / 3 : slot % 3;
        float temp = 0.1875f + xSlot * .3125f;
        return Helpers.isSE(f) ? temp : 1 - temp;
    }

    private float getZOffset(int slot, Direction f) {
        int zSlot = f.getAxis() == Direction.Axis.X ? slot % 3 : slot / 3;
        float temp = 0.1875f + zSlot * .3125f;
        return Helpers.isNE(f) ? 1 - temp : temp;
    }

    private float getYOffset(CraftingStumpBlockEntity e, float delta) {
        float base = 0.9375f;
        if (e.crafting) {
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
