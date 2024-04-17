package Jaxaar.BARSOverlay.Utils;

import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.PlayerReply;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentTranslation;
import scala.Function0;
import scala.Function1;
import scala.runtime.AbstractFunction1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletionException;
import java.util.function.Function;


public class APIRequestInterpreter {

    public static final Minecraft mc = Minecraft.getMinecraft();

    public static HashMap<UUID, PlayerReply.Player> newlyLoadedPLayers = new HashMap();
    public static Boolean validAPIKey = true;
    public static Long lastRequestLimitReached = 0L;

    public static void fetchPlayerStats(HypixelAPI api, UUID uuid){
        api.getPlayerByUuid(uuid).whenComplete((reply, error) -> {
            if (error != null) {
                onErr(error);
                System.out.println("Err fetching: " + uuid.toString());
//                if(error.getMessage().contains("401")){
//                    System.out.println("API-Key expired");
//                }
//                throw new CompletionException(error);
            }
            System.out.println("LOADED: "  + uuid.toString());
            newlyLoadedPLayers.put(uuid, reply.getPlayer());
        });
    }

    public static void testAPIKey(HypixelAPI api){
        api.getPunishmentStats().whenComplete((reply, error) -> {
            if (error != null) {
                onErr(error);
                throw new CompletionException(error);
            }
            System.out.println("APIKey Valid");
            validAPIKey = true;
        });
    }

    public static void onErr(Throwable e){
        System.out.println("Error w/ Hypixel API Req: " + e.getMessage());
        if(e.getMessage().contains("403")){
            System.out.println("API-Key expired");
            validAPIKey = false;
        }
        else if (e.getMessage().contains("429")){
            System.out.println("Request Limit reached");
            mc.thePlayer.addChatMessage(new ChatComponentTranslation("Your API-Key has reached the request limit, please wait a few minutes and then clear your player cache (Ctrl-Alt-Z)"));
            lastRequestLimitReached = System.currentTimeMillis()/1000;
        }
    }
}
