package grojdg.hih.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;

public interface HealthCallback {
    Event<HealthCallback> EVENT = EventFactory.createArrayBacked(HealthCallback.class,
            (listeners) -> (health, entity) -> {
                for (HealthCallback listener : listeners) {
                    ActionResult result = listener.interact(health, entity);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult interact(float health, LivingEntity entity);
}
