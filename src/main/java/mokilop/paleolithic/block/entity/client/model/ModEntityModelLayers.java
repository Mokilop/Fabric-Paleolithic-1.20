package mokilop.paleolithic.block.entity.client.model;

import com.google.common.collect.Sets;
import mokilop.paleolithic.Paleolithic;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.Set;


public class ModEntityModelLayers {
    private static final String MAIN = "main";
    private static final Set<EntityModelLayer> LAYERS = Sets.newHashSet();
    public static final EntityModelLayer GRINDSTONE = registerMain("grindstone");

    private static EntityModelLayer registerMain(String id) {
        return register(id, MAIN);
    }

    private static EntityModelLayer register(String id, String layer) {
        EntityModelLayer eml = create(id, layer);
        if (!LAYERS.add(eml)) {
            throw new IllegalStateException("Duplicate registration for " + eml);
        }
        return eml;
    }

    private static EntityModelLayer create(String id, String layer) {
        return new EntityModelLayer(new Identifier(Paleolithic.MOD_ID, id), layer);
    }
}
