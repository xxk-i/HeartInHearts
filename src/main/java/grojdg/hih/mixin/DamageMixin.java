package grojdg.hih.mixin;

import grojdg.hih.callbacks.DamageCallback;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class DamageMixin {

	@Inject(method = "applyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setHealth(F)V", shift = At.Shift.AFTER))
	private void onDamage(DamageSource source, float amount, CallbackInfo ci) {
		DamageCallback.EVENT.invoker().interact(source, amount, (PlayerEntity) (Object) this);
	}
}