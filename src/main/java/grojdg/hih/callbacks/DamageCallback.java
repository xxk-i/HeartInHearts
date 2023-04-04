package grojdg.hih.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.ActionResult;
import net.minecraft.entity.player.PlayerEntity;

public interface DamageCallback {

    Event<DamageCallback> EVENT = EventFactory.createArrayBacked(DamageCallback.class,
        (listeners) -> (source, amount, player) -> {
        for (DamageCallback listener : listeners) {
            ActionResult result = listener.interact(source, amount, player);

            if(result != ActionResult.PASS) {
                return result;
            }
        }

        return ActionResult.PASS;
    });

    ActionResult interact(DamageSource source, float amount, PlayerEntity player);
}
