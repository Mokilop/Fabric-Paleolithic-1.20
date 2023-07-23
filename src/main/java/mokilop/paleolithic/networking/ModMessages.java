package mokilop.paleolithic.networking;

import mokilop.paleolithic.Paleolithic;
import mokilop.paleolithic.networking.packet.ItemStackSyncS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

public class ModMessages {
    public static Identifier ITEM_SYNC = new Identifier(Paleolithic.MOD_ID, "item_sync");

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(ITEM_SYNC, ItemStackSyncS2CPacket::receive);
    }
    public static void registerC2SPackets() {
    }

}
