package grojdg.hih.event;

import grojdg.hih.networking.HealthS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public class PlayerConnectEvent {

    public static ActionResult onPlayerConnect(ServerPlayerEntity player) {
        for (ServerPlayerEntity otherPlayer : player.getServer().getPlayerManager().getPlayerList()) {
            HealthS2CPacket.send(player, otherPlayer);
        }

        return ActionResult.PASS;
    }
}
