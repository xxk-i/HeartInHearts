package grojdg.hih.networking;

import grojdg.hih.HeartInHeartsConstants;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class DamageS2CPacket {

    // send uuid of damaged player
    public static void send(ServerPlayerEntity serverPlayer, PlayerEntity damagedPlayer) {
        PacketByteBuf packetByteBuf = PacketByteBufs.create();

        // uuid
        packetByteBuf.writeUuid(damagedPlayer.getUuid());

        // send her off
        ServerPlayNetworking.send(serverPlayer, HeartInHeartsConstants.DAMAGE_PACKET_ID, packetByteBuf);
    }
}
