package Jaxaar.BARSOverlay.Utils;

import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.PlayerReply;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentTranslation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.UUID;

public class APIRequestTranslator {

    private static Logger logger = LogManager.getLogger("bars_overlay_mod");
    private static Minecraft mc = Minecraft.getMinecraft();

    public static boolean validAPIKey = true;
    public static Long lastRequestLimitReached = 0L;
    public static HashMap<UUID, PlayerReply> playerCache = new HashMap<>();
    public static HashMap<UUID, Boolean> loadingPlayers = new HashMap<>();


    public static void fetchPlayerStats(HypixelAPI api, UUID uuid){
		api.getPlayerByUuid(uuid).whenComplete((reply, error) -> {
            if (error != null) {
                onErr(error);
                System.out.println("Err fetching: " + uuid.toString());
            }
            loadingPlayers.remove(uuid);
            logger.info("LOADED: " + reply.getPlayer().getName() + " - " + uuid.toString());
            playerCache.put(uuid, reply);
        });
    }

    public static void testAPIKey(HypixelAPI api){
		api.getPunishmentStats().whenComplete((reply, error) -> {
            if (error != null) {
                onErr(error);
            }
            logger.info("APIKey Valid");
            validAPIKey = true;
        });
    }


    public static void onErr(Throwable e){
        logger.info("Error w/ Hypixel API Req: " + e.getMessage());
        if(e.getMessage().contains("403")){
            logger.error("API-Key expired");
            validAPIKey = false;
        }
        else if (e.getMessage().contains("429")){
            logger.error("Request Limit reached");
            mc.thePlayer.addChatMessage(new ChatComponentTranslation("Your API-Key has reached the request limit, please wait a few minutes and then clear your player cache (Ctrl-Alt-Z)"));
            lastRequestLimitReached = System.currentTimeMillis()/1000;
        }
    }
}
