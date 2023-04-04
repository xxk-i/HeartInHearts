package grojdg.hih.mixin;

import grojdg.hih.callbacks.PlayerConnectCallback;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerConnectMixin {

    @Inject(method = "onPlayerConnect", at = @At(value = "TAIL"))
    private void onOnPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        PlayerConnectCallback.EVENT.invoker().interact(player);
    }
}
