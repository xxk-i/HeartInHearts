package grojdg.hih.networking;

import grojdg.hih.HeartInHeartsClient;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Util;

import java.util.UUID;

public class DamageS2CPacketReceiver {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        UUID damagedPlayer = buf.readUuid();

        HeartInHeartsClient.hudOverlay.setDamagedPlayer(damagedPlayer);
        HeartInHeartsClient.hudOverlay.setTimeLastDamaged(Util.getMeasuringTimeMs());
        HeartInHeartsClient.hudOverlay.setSecondUp(!HeartInHeartsClient.hudOverlay.getSecondUp());
    }
}
