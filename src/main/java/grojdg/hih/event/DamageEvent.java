package grojdg.hih.event;

import grojdg.hih.networking.DamageS2CPacket;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.Random;

public class DamageEvent {
    private static Random rd = new Random();

    public static ActionResult OnDamage(DamageSource source, float amount, PlayerEntity player) {
        if(!source.getName().equals("hih")) {
            MinecraftServer server = player.getServer();
            Collection<ServerPlayerEntity> serverPlayers = PlayerLookup.all(server);

            for (ServerPlayerEntity serverPlayer : serverPlayers) {
                if (!(serverPlayer.getName().equals(player.getName()))) {
                    // deal damage to everyone
                    Registry<DamageType> dmgRegistry = server.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE);
                    RegistryEntry<DamageType> hihType = dmgRegistry.getEntry(dmgRegistry.get(new Identifier("hih")));
                    serverPlayer.damage(new DamageSource(hihType, null, null), 0.001f);

                    // this strength number is roughly half that used in LivingEntity for tnt I think?
                    serverPlayer.takeKnockback(0.2000000059604645, rd.nextBoolean() ? -1.0 * Math.random() : 1 * Math.random(), rd.nextBoolean() ? -1.0 * Math.random() : 1.0 * Math.random());
                    serverPlayer.setHealth(serverPlayer.getHealth() - amount);
                }

                // send packet to everyone
                DamageS2CPacket.send(serverPlayer, player);
            }
        }

        return ActionResult.PASS;
    }
}
