package grojdg.hih;

import grojdg.hih.callbacks.DamageCallback;
import grojdg.hih.callbacks.DeathCallback;
import grojdg.hih.callbacks.HealthCallback;
import grojdg.hih.callbacks.PlayerConnectCallback;
import grojdg.hih.event.DamageEvent;
import grojdg.hih.event.DeathEvent;
import grojdg.hih.event.HealthEvent;
import grojdg.hih.event.PlayerConnectEvent;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeartInHeartsMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("hih");

	// "hih" is a custom damage source defined in the datapack
	// we apply damage so that the player gets the "hurt" effect i.e. screen shake and sidestep
	@Override
	public void onInitialize() {
		DamageCallback.EVENT.register(DamageEvent::OnDamage);
		HealthCallback.EVENT.register(HealthEvent::onSetHealth);
		PlayerConnectCallback.EVENT.register(PlayerConnectEvent::onPlayerConnect);
		DeathCallback.EVENT.register(DeathEvent::onDeath);
	}
}