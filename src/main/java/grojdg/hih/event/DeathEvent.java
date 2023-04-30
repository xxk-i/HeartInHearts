package grojdg.hih.event;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import grojdg.hih.HeartInHeartsMod;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public class DeathEvent {

    public static ActionResult onDeath(DamageSource source, ServerPlayerEntity player) {
        if (!source.getType().msgId().equals("outOfWorld")) {
            ServerCommandSource commandSource = player.getServer().getCommandSource();
            CommandManager commandManager = player.getServer().getCommandManager();
            CommandDispatcher commandDispatcher = commandManager.getDispatcher();

            try {
                commandDispatcher.execute("function hih:death/starttimer", commandSource);
                commandDispatcher.execute("title @a subtitle {\"text\": \"" + player.getGameProfile().getName() + " has sealed your fate...\", \"color\":\"red\"}", commandSource);
            }

            catch (CommandSyntaxException e) {
                HeartInHeartsMod.LOGGER.info("Death Command Dispatched Failed: " + e);
            }

        }

        return ActionResult.PASS;
    }
}
