package mokilop.paleolithic.block.entity.client.model;

import mokilop.paleolithic.Paleolithic;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.util.ModelIdentifier;

@Environment(EnvType.CLIENT)
public class ModModelLoadingPlugin implements ModelLoadingPlugin {
    public static final ModelIdentifier GRINDSTONE_MODEL = new ModelIdentifier(Paleolithic.MOD_ID, "grindstone", "");
    public static final ModelIdentifier GRINDSTONE_MODEL_INV = new ModelIdentifier(Paleolithic.MOD_ID, "grindstone", "inventory");

    @Override
    public void onInitializeModelLoader(Context pluginContext) {
        pluginContext.modifyModelOnLoad().register((original, context) -> {
            // This is called for every model that is loaded, so make sure we only target ours
            if (context.id().equals(GRINDSTONE_MODEL) || context.id().equals(GRINDSTONE_MODEL_INV)) {
                return new HandMillBlockModel();
            } else {
                // If we don't modify the model we just return the original as-is
                return original;
            }
        });
    }
}
