package grojdg.hih.mixin;

import grojdg.hih.callbacks.HealthCallback;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class HealthMixin {

    @Inject(method = "setHealth(F)V", at = @At(value = "TAIL"))
    private void onSetHealth(float health, CallbackInfo ci) {
        HealthCallback.EVENT.invoker().interact(health, (LivingEntity) (Object) this);
    }
}
