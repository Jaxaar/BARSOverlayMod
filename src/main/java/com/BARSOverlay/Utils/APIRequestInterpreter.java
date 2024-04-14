package com.BARSOverlay.Utils;

import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.PlayerReply;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletionException;

public class APIRequestInterpreter {

    public static HashMap<UUID, PlayerReply.Player> newlyLoadedPLayers = new HashMap();

    public static void fetchPlayerStats(HypixelAPI api, UUID uuid){
        api.getPlayerByUuid(uuid).whenComplete((reply, error) -> {
            if (error != null) {
                System.out.println("Err fetching: " + uuid.toString());
                throw new CompletionException(error);
            }
            System.out.println("LOADED: "  + uuid.toString());
            newlyLoadedPLayers.put(uuid, reply.getPlayer());
        });
    }



}
