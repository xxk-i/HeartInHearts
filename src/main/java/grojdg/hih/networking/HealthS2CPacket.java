package grojdg.hih.networking;

import grojdg.hih.HeartInHeartsConstants;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class HealthS2CPacket {
    public static void send(ServerPlayerEntity serverPlayer, LivingEntity entity) {
        PacketByteBuf packetByteBuf = PacketByteBufs.create();

        // send Uuid of entity
        packetByteBuf.writeUuid(entity.getUuid());

        // send health
        packetByteBuf.writeFloat(entity.getHealth());

        ServerPlayNetworking.send(serverPlayer, HeartInHeartsConstants.HEALTH_PACKET_ID, packetByteBuf);
    }
}
