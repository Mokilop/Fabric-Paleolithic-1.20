package mokilop.paleolithic.data;

import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;

import java.io.FileWriter;
import java.io.PrintWriter;

public class CustomAndBadGenerator {

    public static void generateCurrentModels(){
        String[] textureKeys = {"log", "log_top"};
        String[] textures = {"minecraft:block", "minecraft:block"};
        generateForAllWoodTypes("paleolithic:block/stump", textureKeys, textures);
    }

    public static void generateForAllWoodTypes(String parent, String[] textureKeys, String[] textures){
        // dark_oak_log
        for(int i = 0; i < Constants.WOOD_TYPES.length; i++){
            Block log = Constants.WOOD_TYPES[i];
            String woodName = Registries.BLOCK.getId(log).toString().replace("_log", "").replace("minecraft:", "");
            String name = woodName + "_" + parent.split("/")[1];
            String blocksPath = "D:\\JSON Generation\\block\\" + name + ".json";
            String blockstatesPath = "D:\\JSON Generation\\blockstates\\" + name + ".json";
            String itemsPath = "D:\\JSON Generation\\item\\" + name + ".json";
            JsonObject blocksJson = new JsonObject();
            JsonObject blockstatesJson = new JsonObject();
            JsonObject itemsJson = new JsonObject();
            JsonObject texturesJson = new JsonObject();
            JsonObject variantsJson = new JsonObject();
            JsonObject modelInBlstJson = new JsonObject();
            modelInBlstJson.addProperty("model", "paleolithic:block/" + name);
            variantsJson.add("", modelInBlstJson);

            for(int j = 0; j < textureKeys.length; j++){
                texturesJson.addProperty(textureKeys[j], textures[j] + "/" + woodName + "_" + textureKeys[j]);
            }
            blocksJson.addProperty("parent", parent);
            blocksJson.add("textures", texturesJson);
            itemsJson.addProperty("parent", "paleolithic:block/" + name);
            blockstatesJson.add("variants", variantsJson);

            try (PrintWriter out = new PrintWriter(new FileWriter(blocksPath))) {
                out.write(blocksJson.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try (PrintWriter out = new PrintWriter(new FileWriter(itemsPath))) {
                out.write(itemsJson.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try (PrintWriter out = new PrintWriter(new FileWriter(blockstatesPath))) {
                out.write(blockstatesJson.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
