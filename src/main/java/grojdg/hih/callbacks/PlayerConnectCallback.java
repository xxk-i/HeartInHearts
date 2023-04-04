package grojdg.hih.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

import javax.swing.*;

public interface PlayerConnectCallback {
    Event<PlayerConnectCallback> EVENT = EventFactory.createArrayBacked(PlayerConnectCallback.class,
            (listeners) -> (player) -> {
                for (PlayerConnectCallback listener: listeners) {
                    ActionResult result = listener.interact(player);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult interact(ServerPlayerEntity player);
}
