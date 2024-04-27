package Jaxaar.BARSOverlay.Utils;

import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.PlayerReply;
import net.hypixel.api.reply.PunishmentStatsReply;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentTranslation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public class APIRequestTranslator {

    public static void fetchPlayerStats(HypixelAPI api, UUID uuid, Consumer<PlayerReply> onSuccess, Consumer<Throwable> onErr){
        api.getPlayerByUuid(uuid).whenComplete((reply, error) -> {
            if (error != null) {
                onErr.accept(error);
            }
            onSuccess.accept(reply);
        });
    }

    public static void testAPIKey(HypixelAPI api, Consumer<PunishmentStatsReply> onSuccess, Consumer<Throwable> onErr){
        api.getPunishmentStats().whenComplete((reply, error) -> {
            if (error != null) {
                onErr.accept(error);
            }
            onSuccess.accept(reply);
        });
    }
}
