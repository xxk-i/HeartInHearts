package grojdg.hih.networking;

import grojdg.hih.HeartInHeartsClient;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

import java.util.UUID;

public class HealthS2CPacketReceiver {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        UUID uuid = buf.readUuid();

        float health = buf.readFloat();

        HeartInHeartsClient.hudOverlay.setHealth(uuid, health);
    }
}
