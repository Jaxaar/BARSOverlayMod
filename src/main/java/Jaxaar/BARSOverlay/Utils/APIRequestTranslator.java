package Jaxaar.BARSOverlay.Utils;

import com.google.gson.JsonObject;
import com.mojang.api.profiles.Profile;
import com.mojang.api.profiles.minecraft.HttpProfileRepository;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.http.HTTPQueryParams;
import net.hypixel.api.http.HypixelHttpResponse;
import net.hypixel.api.reply.*;
import net.hypixel.api.util.Utilities;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentTranslation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
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

    public static void fetchMojangPlayerData(/*HttpProfileRepository profiler,*/ String name, Consumer<UUID> onSuccess, Consumer<Throwable> onErr){
        HttpProfileRepository profiler = new HttpProfileRepository();
        CompletableFuture<UUID> playerUUID = CompletableFuture.supplyAsync(() -> {
            try{
                UUID uuid = profiler.findProfilesByNames(name)[0].getId();
                return uuid;
            }
            catch(Exception e){
                return null;
            }
        }).whenComplete((reply, error) -> {
            if (error != null && reply != null) {
                onErr.accept(error);
            }
            onSuccess.accept(reply);
        });
    }
}
