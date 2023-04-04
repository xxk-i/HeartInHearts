package grojdg.hih;

import grojdg.hih.networking.DamageS2CPacketReceiver;
import grojdg.hih.networking.HealthS2CPacketReceiver;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class HeartInHeartsClient implements ClientModInitializer {

	public static HeartInHeartsHudOverlay hudOverlay;

	@Override
	public void onInitializeClient() {
		hudOverlay = new HeartInHeartsHudOverlay();

		HudRenderCallback.EVENT.register(hudOverlay);

		ClientPlayNetworking.registerGlobalReceiver(HeartInHeartsConstants.DAMAGE_PACKET_ID, DamageS2CPacketReceiver::receive);
		ClientPlayNetworking.registerGlobalReceiver(HeartInHeartsConstants.HEALTH_PACKET_ID, HealthS2CPacketReceiver::receive);
	}

	public HeartInHeartsHudOverlay getHudOverlay() {
		return hudOverlay;
	}
}