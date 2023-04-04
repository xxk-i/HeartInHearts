package grojdg.hih.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public interface DeathCallback {

    Event<DeathCallback> EVENT = EventFactory.createArrayBacked(DeathCallback.class,
            (listeners) -> (source, player) -> {
            for (DeathCallback listener : listeners) {
                ActionResult result = listener.interact(source, player);

                if (result != ActionResult.PASS) {
                    return result;
                }
            }

            return ActionResult.PASS;
    });

    ActionResult interact(DamageSource source, ServerPlayerEntity player);
}
