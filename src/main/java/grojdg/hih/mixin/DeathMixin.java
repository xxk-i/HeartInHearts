package grojdg.hih.mixin;

import grojdg.hih.callbacks.DeathCallback;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class DeathMixin {

    @Inject(method = "onDeath", at = @At("TAIL"))
    private void onOnDeath(DamageSource source, CallbackInfo ci) {
        DeathCallback.EVENT.invoker().interact(source, (ServerPlayerEntity) (Object) this);
    }
}
