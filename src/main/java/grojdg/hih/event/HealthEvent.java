package grojdg.hih.event;

import grojdg.hih.networking.HealthS2CPacket;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public class HealthEvent {

    public static ActionResult onSetHealth(float heath, LivingEntity entity) {
        if (entity.isPlayer()) {
            for (ServerPlayerEntity serverPlayer : PlayerLookup.all(entity.getServer())) {
                HealthS2CPacket.send(serverPlayer, entity);
            }
        }

        return ActionResult.PASS;
    }
}
